package com.iticbcn;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    static final String DIR_ARRIBADA = System.getProperty("java.io.tmpdir");
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public void connectar() throws UnknownHostException, IOException {
        socket = new Socket(Servidor.HOST, Servidor.PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    // envia el fitxer llegit per consola amb ruta completa que s'envia al servidor per rebre byte[]
    public void rebreFitxers() {
        String nomFitxer = "";
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Introdueix el nom del fitxer a consultar amb ruta completa:");
            String path = sc.nextLine();
            nomFitxer = new File(path).getName();
            // a la recepció si hi ha algo que llegir, o té contingut, escriu en el directori temporal
        }
    }
    public void tancarConnexio() throws IOException {
        if (out != null) out.close();
        if (in != null) in.close();
        if(socket != null) socket.close();
    }
    public static void main(String[] args) throws UnknownHostException, IOException {
        Client cli = new Client();
        cli.connectar();
        cli.rebreFitxers();
        cli.tancarConnexio();
    }
}