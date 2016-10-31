package de.laurenzgrote.bwinf35.rhinozelfant.gui;

import de.laurenzgrote.bwinf35.rhinozelfant.Bild;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame {

    private static final FileFilter filter = new FileNameExtensionFilter("PNG-Dateien",  "png");


    private Bild bild;
    private BufferedImage ursprungsbild;

    private ImagePanel originalBild, zwischenbildWahlPanel, endBild;

    private JButton bildLaden, bildExportieren;

    public GUI() {
        // Titel der GUI setzen
        super ("Rhinozelfant: Laurenz Grote");

        // Was muss das muss!
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 800);

        // In das JFrame kommt ein Bilderpanel + Knopfpanel rein
        Container container = this.getContentPane();
        container.add(getCenterPanel(), BorderLayout.CENTER);
        container.add(getSouthPanel(), BorderLayout.SOUTH);

        // GUI gerendert, anzeigen!
        this.setVisible(true);
    }

    private JPanel getCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 3));

        originalBild = new ImagePanel();
        originalBild.setBorder(BorderFactory.createTitledBorder("Originalbild"));

        zwischenbildWahlPanel = new ImagePanel();
        zwischenbildWahlPanel.setBorder(BorderFactory.createTitledBorder("Zwischenbilder"));

        endBild = new ImagePanel();
        endBild.setBorder(BorderFactory.createTitledBorder("Endbild"));

        centerPanel.add(originalBild);
        centerPanel.add(zwischenbildWahlPanel);
        centerPanel.add(endBild);

        return centerPanel;
    }

    private JPanel getSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1,2));

        bildLaden = new JButton("Bilddatei laden");
        bildExportieren = new JButton("Bilddatei exportieren");

        southPanel.add(bildLaden);
        southPanel.add(bildExportieren);

        registriereOeffnenListener();
        registriereExportierenListener();

        return southPanel;
    }

    private void registriereOeffnenListener() {
        bildLaden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                final JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(filter);
                int returnVal = jFileChooser.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File quelle = jFileChooser.getSelectedFile();
                    try {
                        ursprungsbild = ImageIO.read(quelle);
                        originalBild.setBackgroundImage(ursprungsbild);
                        bild = new Bild(quelle, true); // In der GUI brauchen wir DEBUG
                        zwischenbildWahlPanel.setBackgroundImage(bild.getDebugGleichfarbige());
                        endBild.setBackgroundImage(bild.getGefiltertesBild());
                    } catch (IOException e) {
                        System.err.println("I/O-Error beim Laden der Bilddatei!");
                    }
                } else {
                    System.err.println("Bitte Quelldatei angeben; Ladevorgang abgebrochen!");
                }
            }
        });
    }
    private void registriereExportierenListener() {
        bildExportieren.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File ziel;

                final JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(filter);

                int returnVal = jFileChooser.showSaveDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        ziel = jFileChooser.getSelectedFile();
                        ImageIO.write(bild.getGefiltertesBild(), "png", ziel);
                    } catch (IOException e) {
                        System.err.println("I/O-Error beim Speichern der Bilddatei!");
                    }
                } else {
                    System.err.println("Bitte Zielatei angeben; Speichern abgebrochen");
                }
            }
        });
    }
}
