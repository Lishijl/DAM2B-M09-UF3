package com.iticbcn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    static final int PORT = 9999;
    static final String HOST = "localhost";
    private ServerSocket srvSocket;
    private Socket clientSocket;
    // server escolta pel port i retorna el clientSocket acceptat
    public Socket connectar() throws IOException {
        srvSocket = new ServerSocket(PORT);
        return srvSocket.accept();
    }
    // pot tenir multi connexó amb clients, però es tanca només el rebut i 
    // el servidor només tanca abans de sortir
    public void tancarConnexio(Socket cliSocket) throws IOException {
        if (cliSocket != null) cliSocket.close();
    }
    // reb nomFitxer per enviar al client - llegeix si té contingut
    // conversió en byte[] i enviar per xarxa
    public void enviarFitxers() {}
    public static void main(String[] args) throws Throwable {
        Servidor serv = new Servidor();
        Socket cliSock = serv.connectar();
        serv.enviarFitxers();
        serv.tancarConnexio(cliSock);
    }
}