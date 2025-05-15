package com.iticbcn;

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
    public void servidorAEscoltar() {}
    // atura server socket
    public void pararServidor() {}
    // envia msg de grup amb el contingut msg_sortir, buida hashtable, surt del programa
    public void finalitzarXat() {

    }
    // afegeix client al hashtable i envia msg de grup amb el nom dient que entra.
    public void afegirClient(GestorClients gsClients) {

    }
    // si es valida trobant el nom en el hshtb, l'elimina de la taula
    public void eliminarClient(String nomCli) {

    }
    // l'envia a tots els clients el msg
    public void enviarMissatgeGrup(String msg) {}
    //  params, nom origen i desti
    public void enviarMissatgePersonal(String nomDestinatari, String nomRemitent, String msg) {}
    public static void main(String[] args) {
        ServidorXat servXat = new ServidorXat();
        servXat.servidorAEscoltar();
        while(servXat.sortir == false) {
            servXat.srvSocket.accept();
        }
    }
}
