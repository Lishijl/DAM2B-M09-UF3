package com.iticbcn;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    static final int PORT = 9999;
    static final String HOST = "localhost";
    private ServerSocket srvSocket;
    private Socket cliSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public Socket connectar() throws IOException {
        System.out.println("\nAcceptant connexions en -> " + HOST + ":" + PORT);
        srvSocket = new ServerSocket(PORT);
        System.out.println("Esperant connexio...");
        return srvSocket.accept();
    }
    // pot tenir multi connexó amb clients, però es tanca només el rebut i 
    // el servidor només tanca abans de sortir
    public void tancarConnexio(Socket cliSocket) throws IOException {
        System.out.println("Tancant connexió amb el client: " + cliSocket.getLocalAddress());
        if (cliSocket != null) cliSocket.close();
    }
    public void enviarFitxers() throws ClassNotFoundException, IOException {
        System.out.println("Esperant el nom del fitxer del client...");
        while (true) {
            try {
                String ruta = (String) in.readObject();
                System.out.println("Nomfitxer rebut: " + ruta);
                Fitxer fitxer = new Fitxer(ruta);
                byte[] contingut = fitxer.getContingut();
                if (contingut != null) {
                    System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
                    out.writeInt(contingut.length);
                    out.writeObject(contingut);
                    out.flush();
                    System.out.println("Fitxer enviat al client: " + ruta);
                } else {
                    out.writeInt(0);
                    out.flush();
                }
            } catch (EOFException eof) {
                System.out.println("Error llegint el fitxer del client: null");
                break;
            } catch (ClassNotFoundException cnfe) {
                System.err.println("Error de tipus rebut: " + cnfe.getMessage());
            }
        }
        System.out.println("Nom del fitxer buit o nul. Sortint...");
    }
    public static void main(String[] args) throws Throwable {
        Servidor serv = new Servidor();
        serv.cliSocket = serv.connectar();
        System.out.println("Connexio acceptada: " + serv.cliSocket.getInetAddress());
        serv.out = new ObjectOutputStream(serv.cliSocket.getOutputStream());
        serv.in = new ObjectInputStream(serv.cliSocket.getInputStream());
        serv.enviarFitxers();
        serv.tancarConnexio(serv.cliSocket);
    }
}