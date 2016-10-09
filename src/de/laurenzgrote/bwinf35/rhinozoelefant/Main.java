package de.laurenzgrote.bwinf35.rhinozoelefant;

import de.laurenzgrote.bwinf35.rhinozoelefant.gui.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

public class Main {

    public Main() throws IOException {
    }
    public void verarbeiteBild(File quelle, File ziel) throws IOException {
        Bild zuVerarbeitendesBild = new Bild(quelle);
        ImageIO.write(zuVerarbeitendesBild.getGefiltertesBild(), "png", ziel);
    }
    public void bildeingabeMitGUI() throws IOException {
//        final FileFilter filter = new FileNameExtensionFilter("Bilder",
//                "gif", "png", "jpg");
//
//        File quelle = null;
//        File ziel = null;
//
//        final JFileChooser jFileChooser = new JFileChooser();
//        jFileChooser.setFileFilter(filter);
//        int returnVal = jFileChooser.showOpenDialog(null);
//
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            quelle = jFileChooser.getSelectedFile();
//        } else {
//            System.err.println("Bitte Quelldatei angeben!");
//            System.exit(-1);
//        }
//
//        returnVal = jFileChooser.showSaveDialog(null);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            ziel = jFileChooser.getSelectedFile();
//        } else {
//            System.err.println("Bitte Zielatei angeben!");
//            System.exit(-1);
//        }
//
//        verarbeiteBild(quelle, ziel);

        GUI gui = new GUI();
    }
    public static void main(String[] args) throws IOException {
        Main main = new Main();

        if (args.length != 0) {
            if (args[0].equals("--cli")) {
                File quelle = new File(args[1]);
                File ziel = new File(args[2]);

                main.verarbeiteBild(quelle, ziel);
            } else if (args[0].equals("--endlessgui")) {
                while (true) {
                    main.bildeingabeMitGUI();
                }
            }
        } else {
            main.bildeingabeMitGUI();
        }
    }
}
