package com.iticbcn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GestorClients extends Thread {
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServidorXat srvXat;
    private String nom;
    private boolean sortir = false;
    public GestorClients(Socket client, ServidorXat srvXat) {
        this.client = client;
        this.srvXat = srvXat;
    }

    public String getNom() {
        return nom;
    }
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            while (!sortir) {
                String msg = (String) in.readObject();
                if (msg != null) {
                    processaMissatge(msg);
                }
            }
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (client != null) client.close();
                srvXat.eliminarClient(nom);
            } catch (IOException e) {
                System.out.println("Error tancant connexió per client " + nom);
            }
        }
    }
    public void enviarMissatge(String nomRemitent, String msg) throws IOException {
        out.writeObject(msg);
        out.flush();
    }
    public void processaMissatge(String txt) throws IOException {
        // extreu codi, si és Missatge.CODI_CONECTAR al client
        String codi = Missatge.getCodiMissatge(txt);
        String[] parts = Missatge.getPartsMissatge(txt);
        switch (codi) {
            case Missatge.CODI_CONECTAR:
                nom = parts[1];
                srvXat.afegirClient(this);
                break;
            case Missatge.CODI_SORTIR_CLIENT:
                srvXat.eliminarClient(nom);
                sortir = true;
                break;
            case Missatge.CODI_SORTIR_TOTS:
                srvXat.finalitzarXat();
                sortir = true;
                break;
            case Missatge.CODI_MSG_PERSONAL:
                String desti = parts[1];
                String msg = parts[2];
                srvXat.enviarMissatgePersonal(desti, nom, msg);
                break;
            case Missatge.CODI_MSG_GRUP:
                srvXat.enviarMissatgeGrup(parts[1]);
                break;
            default:
                System.out.println("CODI incorrecte: " + codi);
        }
    }
}
