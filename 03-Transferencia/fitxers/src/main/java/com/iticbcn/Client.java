package com.iticbcn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
    public void rebreFitxers() {}
    public void tancarConnexio() {}
    public static void main(String[] args) {}
}