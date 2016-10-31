package de.laurenzgrote.bwinf35.rhinozelfant;

import de.laurenzgrote.bwinf35.rhinozelfant.gui.GUI;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {

    public Main() {
    }

    // Funktion zur CLI-Bearbeitung
    private void verarbeiteBild(File quelle, File ziel){
        try {
            Bild zuVerarbeitendesBild = new Bild(quelle);
            ImageIO.write(zuVerarbeitendesBild.getGefiltertesBild(), "png", ziel);
        } catch (IOException e) {
            System.err.printf("I/O Fehler bei Verarbeitung von %s mit Ausgabe nach %s", quelle.toString(), ziel.toString());
        }

    }
    private void bildeingabeMitGUI() {
        new GUI();
    }

    public static void main(String[] args){
        Main main = new Main();

        // CLI-Argument-Handler
        if (args.length != 0) {
            if (args[0].equals("--cli")) {
                File quelle = new File(args[1]);
                File ziel = new File(args[2]);

                main.verarbeiteBild(quelle, ziel);
            }
        } else {
            main.bildeingabeMitGUI();
        }
    }
}
