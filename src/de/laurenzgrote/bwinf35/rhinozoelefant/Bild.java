package de.laurenzgrote.bwinf35.rhinozoelefant;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Bild{
    private final int weiss = Color.WHITE.getRGB();

    private BufferedImage bild;

    private boolean debugflag = false;

    private boolean[][] debugGleichfarbige = null;

    public Bild(File file) throws IOException {
        this (file, false);
    }

    public Bild(File file, boolean debugflag) throws IOException {
        bild = ImageIO.read(file);
        this.debugflag = debugflag;

        boolean[][] gleichfarbigeStellen = scanneAufGleicheFelder();

        RhinozoelefantSucher rhinozoelefantSucher = new RhinozoelefantSucher(gleichfarbigeStellen);
        faerbeStellen(rhinozoelefantSucher.getRhinozoelefantenFelder());
    }


    private boolean[][] scanneAufGleicheFelder() {
        boolean[][] gleichfarbigeStellen = new boolean[bild.getWidth()][bild.getHeight()];

        // Zeilenweiser Scan
        for (int x = bild.getMinX(); x < bild.getWidth(); x++) {
            for (int y = bild.getMinY(); y < bild.getHeight() - 1; y++) {
                if (sindFarbenGleich(x, y, x, y + 1)) {
                    gleichfarbigeStellen[x][y] = true;
                    gleichfarbigeStellen[x][y + 1] = true;
                }
            }
        }
        // Spaltenweiser Scan
        for (int y = bild.getMinY(); y < bild.getHeight(); y++) {
            for (int x = bild.getMinX(); x < bild.getWidth() - 1; x++) {
                if (sindFarbenGleich(x, y, x + 1,y)) {
                    gleichfarbigeStellen[x][y] = true;
                    gleichfarbigeStellen[x + 1][y] = true;
                }
            }
        }

        // DEBUGTIME?
        if (debugflag) debugGleichfarbige = util.deepCopy(gleichfarbigeStellen);

        return gleichfarbigeStellen;
    }

    private BufferedImage faerbeStellen(Set<int[]> stellen) {
        BufferedImage ausgabebild = bild;

        for (int[] koordinate : stellen) {
            ausgabebild.setRGB(koordinate[0], koordinate[1], weiss);
        }
        return ausgabebild;
    }


    private boolean sindFarbenGleich(int aX, int aY, int bX, int bY) {
        int rgbA = bild.getRGB(aX, aY);
        int rgbB = bild.getRGB(bX, bY);

        return (rgbA == rgbB);
    }

    public BufferedImage getGefiltertesBild() {
        return bild;
    }

    public boolean[][] getDebugGleichfarbige() {
        if (debugGleichfarbige == null) {
            System.err.println("Bild: Deuginformation wurden abgerufen, obwohl sich das Programm nicht im Debugmodus befindet");
            // Radikale Lösung, der Fehler dürfte zur Laufzeit bei korrekter Programmierung nicht auftreten!
            System.exit(-1);
        }
        return debugGleichfarbige;
    }
}
