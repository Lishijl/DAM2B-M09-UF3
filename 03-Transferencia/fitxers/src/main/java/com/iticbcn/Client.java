package com.iticbcn;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {
    static final String DIR_ARRIBADA = System.getProperty("java.io.tmpdir");
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public void connectar() throws UnknownHostException, IOException {
        System.out.println("\nConnectant a -> " + Servidor.HOST + ":" + Servidor.PORT);
        socket = new Socket(Servidor.HOST, Servidor.PORT);
        System.out.println("Connexio acceptada: " + socket.getInetAddress());       // l'adreça del remot, servidor localhost/127.0.0.1
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    public void rebreFitxers() throws IOException, ClassNotFoundException {
        String nomFitxer = "";
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Nom del fitxer a rebre ('sortir' per sortir): ");
            while (true) { 
            String path = sc.nextLine();
            if (path.equalsIgnoreCase("sortir")) break;
                nomFitxer = path;
                out.writeObject(nomFitxer);
                out.flush();
                String desti = DIR_ARRIBADA + Paths.get(nomFitxer).getFileName().toString();
                int tamany = in.readInt();
                if (tamany > 0) {
                    System.out.println("Nom del fitxer a guardar: " + desti);
                    Object contingutObj = in.readObject();
                    if (contingutObj instanceof byte[] contingutByte) {
                        Files.write(Paths.get(DIR_ARRIBADA, Paths.get(nomFitxer).getFileName().toString()), contingutByte);
                        System.out.println("Fitxer rebut i guardat com: " + desti);
                    }
                }
            }
        }
    }
    public void tancarConnexio() throws IOException {
        System.out.println("Sortint...");
        if (out != null) out.close();
        if (in != null) in.close();
        if (socket != null) socket.close();
    }
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        Client cli = new Client();
        cli.connectar();
        cli.rebreFitxers();
        cli.tancarConnexio();
        System.out.println("Connexio tancada.");
    }
}