package com.iticbcn;

import java.io.IOException;
import java.io.ObjectInputStream;

public class FilServidorXat extends Thread {
    private final ObjectInputStream in;
    public FilServidorXat(ObjectInputStream in) {
        this.in = in;
    }
    @Override
    public void run() {
        try {
            String msg;
            while ((msg = (String) in.readObject()) != null) {
                System.out.println("Missatge (\'sortir\') per tancar: Rebut: " + msg);
                if (msg.equalsIgnoreCase(ServidorXat.MSG_SORTIR)) break;
            }
            System.out.println("Fil de xat finalitzat.");
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}