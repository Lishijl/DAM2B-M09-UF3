package com.iticbcn;

import java.io.IOException;
import java.io.ObjectInputStream;

public class FilLectorCX extends Thread {
    private final ObjectInputStream in;
    public FilLectorCX(ObjectInputStream in) {
        this.in = in;
    }
    @Override
    public void run() {
        try {
            System.out.println("Missatge ('sortir' per tancar): Fil de lectura iniciat");
            String msg;
            while ((msg = (String) in.readObject()) != null) {
                if (msg.equalsIgnoreCase(ServidorXat.MSG_SORTIR)) break;
                System.out.println("Rebut: " + msg);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}