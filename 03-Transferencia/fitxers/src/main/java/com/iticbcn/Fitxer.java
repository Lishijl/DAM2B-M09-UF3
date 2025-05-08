package com.iticbcn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Fitxer {
    private final String nom;
    private byte[] contingut;
    public Fitxer(String nom) {
        this.nom = nom;
    }
    public byte[] getContingut() throws IOException {
        File fitxer = new File(nom);
        contingut = null;
        if (fitxer.exists() && fitxer.isFile()) {
            Path path = fitxer.toPath();
            contingut = Files.readAllBytes(path);
        }
        return contingut;
    }
}