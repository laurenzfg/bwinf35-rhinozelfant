package de.laurenzgrote.bwinf35.rhinozoelefant;

import de.laurenzgrote.bwinf35.rhinozoelefant.gui.GUI;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Main {

    public Main() {
    }
    private void verarbeiteBild(File quelle, File ziel) throws IOException {
        Bild zuVerarbeitendesBild = new Bild(quelle);
        ImageIO.write(zuVerarbeitendesBild.getGefiltertesBild(), "png", ziel);
    }
    private void bildeingabeMitGUI() throws IOException {
        new GUI();
    }
    public static void main(String[] args) throws IOException {
        Main main = new Main();

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
