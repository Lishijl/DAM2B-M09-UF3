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
            String msg;
            int inici = 0;
            while ((msg = (String) in.readObject()) != null) {
                if (inici == 0) {
                    msg = "Fil de lectura iniciat";
                    inici++;
                }
                msg = "Rebut" + msg;
                System.out.println("Missatge ('sortir' per tancar): " + msg);
                if (msg.equalsIgnoreCase(ServidorXat.MSG_SORTIR)) break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}