package com.iticbcn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean sortir = false;
    public void connecta() throws IOException {
        socket = new Socket(ServidorXat.HOST, ServidorXat.PORT);
        System.out.println("\nClient connectat a " + ServidorXat.HOST + ":" + ServidorXat.PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Flux d'entrada i sortida creat.");
    }
    public void enviarMissatge(String msg) throws IOException {
        System.out.println("Enviant missatge: " + msg);
        out.writeObject(msg);
        out.flush();
    }
    // tanca streams i socket
    public void tancarClient() throws IOException {
        System.out.println("Tancant client...");
        if (in != null) in.close();
        System.out.println("Flux d'entrada tancat.");
        if (out != null) out.close();
        System.out.println("Flux de sortida tancat.");
        if (socket != null) socket.close();
    }
    // metode d'execució
    public void llegeix() throws IOException, ClassNotFoundException {
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("DEBUG: Iniciant rebuda de missatges...");
            while (!sortir) {
                String msgCru = (String) in.readObject();
                if (msgCru == null) continue;
                String codi = Missatge.getCodiMissatge(msgCru);
                if (codi == null) continue;
                String[] parts = Missatge.getPartsMissatge(msgCru);
                switch (codi) {
                    case Missatge.CODI_SORTIR_TOTS:
                        sortir = true;
                        break;
                    case Missatge.CODI_MSG_PERSONAL:
                        if (parts.length >= 3) {
                            String remitent = parts[1];
                            String msg = parts[2];
                            System.out.println("Missatge de (" + remitent + "): " + msg);
                        }
                        break;
                    case Missatge.CODI_MSG_GRUP:
                        if (parts.length >= 2) {
                            System.out.println("Missatge de grup: " + parts[1]);
                        } else {
                            System.out.println("Error rebent missatge. Sortint...");
                        }
                        break;
                    default:
                        System.out.println("Missatge desconegut rebut.");
                }
            }
            tancarClient();
    }

    // mostra opcions de comandes disponibles
    public void ajuda() {
        System.out.println("---------------------");
        System.out.println("Comandes disponibles:");
        System.out.println("1.- Conectar al servidor (primer pass obligatori)");
        System.out.println("2.- Enviar missatge personal");
        System.out.println("3.- Enviar missatge al grup");
        System.out.println("4.- (o línia en blanc)-> Sortir del client");
        System.out.println("5.- Finalitzar tothom");
        System.out.println("---------------------");
    }
    // donat un Scanner, un missatge i un flag obligatori, llegeixi una linea de consola CORRECTAMENT
    public String getLinea(Scanner sc, boolean obligatori) {
        String linea = "";
        do {
            linea = sc.nextLine().trim();
            if (!obligatori || !linea.isEmpty()) break;
        } while (true);
        return linea;
    }
    public static void main(String[] args) throws IOException {
        // connecta
        ClientXat cli = new ClientXat();
        cli.connecta();
        new Thread(() -> {
            try {
                cli.llegeix();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }).start();
        while (!cli.sortir) { 
            cli.ajuda();
            Scanner sc = new Scanner(System.in);
            String linia = cli.getLinea(sc, cli.sortir);
            if (linia.isEmpty()) {
                cli.enviarMissatge(Missatge.getMissatgeSortirClient("Adéu"));
                cli.sortir = true;
                break;
            } 
            String msg = "";
            switch (linia) {
                case "1":
                    System.out.print("Introdueix el nom: ");
                    String nom = cli.getLinea(sc, cli.sortir);
                    cli.enviarMissatge(Missatge.getMissatgeConectar(nom));
                    break;
                case "2":
                    System.out.print("Destinatari: ");
                    String destinatari = cli.getLinea(sc, cli.sortir);
                    System.out.print("Missatge a enviar: ");
                    msg = cli.getLinea(sc, cli.sortir);
                    cli.enviarMissatge(Missatge.getMissatgePersonal(destinatari, msg));
                    break;
                case "3":
                    System.out.print("Missatge a enviar: ");
                    msg = cli.getLinea(sc, cli.sortir);  
                    cli.enviarMissatge(Missatge.getMissatgeGrup(msg));
                    break;
                case "4":
                    cli.enviarMissatge(Missatge.getMissatgeSortirClient("Adéu"));
                    cli.sortir = true;
                    break;
                case "5":
                    cli.enviarMissatge(Missatge.getMissatgeSortirTots("Adéu"));
                    cli.sortir = true;
                    break;
                default:
                    throw new AssertionError();
            }
        }
        cli.tancarClient();
    }
}
