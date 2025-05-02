package com.iticbcn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    static final int PORT = ServidorXat.PORT;
    static final String HOST = ServidorXat.HOST;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public void connecta() throws IOException {
        socket = new Socket(HOST, PORT);
        System.out.println("Client connectat a " + HOST + ":" + PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Flux d'entrada i sortida creat.");          // 2 msg previs
    }
    public void enviarMissatge(String msg) throws IOException { 
        System.out.println("Enviant missatge: " + msg);
        out.writeObject(msg); 
    }
    public void tancarClient() throws IOException {
        System.out.println("Tancant client...");
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
    }
    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        try {
            client.connecta();
            Thread cliXatTh = new FilLectorCX(client.in);
            cliXatTh.start();

            Scanner sc = new Scanner(System.in);
            String msg;
            while(!(msg = sc.nextLine()).equalsIgnoreCase(ServidorXat.MSG_SORTIR)) {
                client.enviarMissatge(msg);
                System.out.print("Missatge ('sortir' per tancar): ");
            }
            client.enviarMissatge(ServidorXat.MSG_SORTIR);          // 5 msg Finals
            sc.close();
            cliXatTh.join();
            client.tancarClient();
            System.out.println("Client tancat.");
            if (client.socket.isClosed()) {
                System.out.println("El servidor ha tancat la connexió.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}