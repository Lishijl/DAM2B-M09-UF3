package com.iticbcn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {
    static final int PORT = 9999;
    static final String HOST = "localhost";
    static final String MSG_SORTIR = "sortir";
    private Hashtable<String, GestorClients> hashtable = new Hashtable<>();
    private boolean sortir = false;
    private ServerSocket srvSocket;

    private Socket cliSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    // port i host anteriors
    public void servidorAEscoltar() throws IOException {
        srvSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
    }
    // atura server socket
    public void pararServidor() throws IOException {
        if (srvSocket != null && !srvSocket.isClosed()) srvSocket.close();
    }
    // envia msg de grup amb el contingut msg_sortir, buida hashtable, surt del programa
    public void finalitzarXat() throws IOException {
        enviarMissatgeGrup(Missatge.getMissatgeSortirTots(MSG_SORTIR));
        hashtable.clear();
        System.out.println("Tancant tots els clients.");
        pararServidor();
    }
    // afegeix client al hashtable i envia msg de grup amb el nom dient que entra.
    public void afegirClient(GestorClients gsClients) throws IOException {
        hashtable.put(gsClients.getNom(), gsClients);
        System.out.println(gsClients.getNom() + " connectat.");
        enviarMissatgeGrup(Missatge.getMissatgeGrup("Entra: " + gsClients.getNom()));
    }
    // si es valida trobant el nom en el hshtb, l'elimina de la taula
    public void eliminarClient(String nomCli) {
        if (hashtable.containsKey(nomCli)) {
            hashtable.remove(nomCli);
        }
    }
    // l'envia a tots els clients el msg
    public void enviarMissatgeGrup(String msg) throws IOException {
        for (GestorClients gc: hashtable.values()) {
            gc.enviarMissatge(gc.getNom(), msg);
        }
    }
    //  params, nom origen i desti
    public void enviarMissatgePersonal(String nomDestinatari, String nomRemitent, String msg) throws IOException {
        GestorClients cli = hashtable.get(nomDestinatari);
        if (cli != null) {
            System.out.println("Missatge personal per (" + nomDestinatari + ") de (" + nomRemitent + "): " + msg);
            cli.enviarMissatge(nomRemitent, msg);
        } else {
            System.out.println("No s'ha trobat destinatari.");
        }
    }
    public static void main(String[] args) throws IOException {
        ServidorXat servXat = new ServidorXat();
        servXat.servidorAEscoltar();
        while(true) {
            servXat.cliSocket = servXat.srvSocket.accept();
            System.out.println("Client connectat: " + servXat.cliSocket.getInetAddress());
            GestorClients gestorClients = new GestorClients(servXat.cliSocket, servXat);
            gestorClients.start();
        }
    }
}
