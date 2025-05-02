package com.iticbcn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorXat {
    static final int PORT = 9999;
    static final String HOST = "localhost";
    static final String MSG_SORTIR = "sortir";
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void iniciarServidor() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
    }
    public void pararServidor() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null) clientSocket.close(); 
        if (serverSocket != null) serverSocket.close();
    }
    // reb Streams i obté el nom Client
    public String getNom(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException { 
        out.writeObject("Escriu el teu nom:");
        return (String) in.readObject();
    }
    public static void main(String[] args) {
        ServidorXat servidor = new ServidorXat();
        try {
            servidor.iniciarServidor();
            servidor.clientSocket = servidor.serverSocket.accept();
            System.out.println("Client connectat: " + servidor.clientSocket.getInetAddress());
            servidor.out = new ObjectOutputStream(servidor.clientSocket.getOutputStream());
            servidor.in = new ObjectInputStream(servidor.clientSocket.getInputStream());
            String cliNom = servidor.getNom(servidor.out, servidor.in);
            System.out.println("Nom rebut: " + cliNom);

            Thread servXatTh = new FilServidorXat(servidor.in);
            System.out.println("Fil de xat creat.");
            servXatTh.start();
            System.out.println("Fil de " + cliNom + " iniciat");     // 5 msg previs
            try (BufferedReader consola = new BufferedReader(new InputStreamReader(System.in))) {
                String msg;
                while(!(msg = consola.readLine()).equalsIgnoreCase(MSG_SORTIR)) {
                    servidor.out.writeObject(msg);
                }
                servXatTh.join();           // 3 msg finals
                servidor.out.writeObject(MSG_SORTIR);
                servidor.pararServidor();
                System.out.println("Servidor aturat.");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}