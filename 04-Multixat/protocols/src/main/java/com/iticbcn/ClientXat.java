package com.iticbcn;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientXat {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    // flag
    public void connecta() {}
    // tanca streams i socket
    public void tancarClient() {}
    // metode d'execució

    // mostra opcions de comandes disponibles
    public void ajuda() {}
    // donat un Scanner, un missatge i un flag obligatori, llegeixi una linea de consola CORRECTAMENT
    public void getLinea() {}
    public void main(String[] args) {
        // connecta
        // inicia fil de llegir
        // mostra ajuda
        // si rep linea buida sortir = true
        // cas contrari
        // 1 llegeix el nom el codifica
        // 2 demana destinaari, demana missatge i codifica el missatge
        // 3 demana msg i codifica
        // 4 codifica missatge de sortir client i Sortir
        // 5 codifica el missatge de sortir tots i Sortir
        // envia missatge codificat
        // mentre no sortir
        // netejar, tancar i sortir
    }
}
