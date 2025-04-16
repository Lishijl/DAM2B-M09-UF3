package com.iticbcn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    static final int PORT = Servidor.PORT;
    static final String HOST = Servidor.HOST;
    private Socket socket;
    private PrintWriter out;
    public void conecta() {
        try {
            socket = new Socket(HOST, PORT);        // client que es connecta unidireccionalment
            out = new PrintWriter(socket.getOutputStream(), true);      // autoflush true
            System.out.println("Connectat a servidor en " + HOST + ":" + PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void tanca() {
        try {
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void envia(String msg) {
        if (out != null) out.println(msg);
        System.out.println("Enviat al servidor: " + msg);
    }
    // connecta, envia prova1, envia prova2, envia adeu!, espera pulsació d'ENTER i tanca
    public static void main(String[] args) {
        Client cli = new Client();
        cli.conecta();
        cli.envia("Prova d'enviament 1");
        cli.envia("Prova d'enviament 2");
        cli.envia("Adéu!");
        System.out.println("Prem Enter per tancar el client...");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            if (br.readLine().isEmpty()) cli.tanca();
            System.out.println("Client tancat");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}