package com.iticbcn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    static final int PORT = 7777;
    static final String HOST = "localhost";
    private ServerSocket srvSocket;
    private Socket clientSocket;
    public void connecta() {
        try {
            srvSocket = new ServerSocket(PORT);     // escolta la connexió
            System.out.println("Esperant connexions a " + HOST + ":" + PORT);
            clientSocket = srvSocket.accept();
            // mostra la IP del host del client connectat
            System.out.println("Client connectat: " + clientSocket.getInetAddress());
            repDades();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void repDades() {
        // només si el socket client segueix connectat
        if (!clientSocket.isClosed()) {
            // del socket del client es llegeixen els streams i tanca in al final del try
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String txt;
                while ((txt = in.readLine()) != null) {
                    System.out.println("Rebut: " + txt);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public void tanca() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close(); 
            if (srvSocket != null && !srvSocket.isClosed()) srvSocket.close();
            System.out.println("Servidor tancat.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Servidor srv = new Servidor();
        System.out.println("\nServidor en marxa a " + HOST + ":" + PORT);
        srv.connecta();
        srv.repDades();
        srv.tanca();
    }
}