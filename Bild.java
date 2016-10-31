package de.laurenzgrote.bwinf35.rhinozelfant;

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

    // Wenn ohne Debugflag aufgerufen wird, kein Debugmodus!
    public Bild(File file) throws IOException {
        this (file, false);
    }

    public Bild(File file, boolean debugflag) throws IOException {
        bild = ImageIO.read(file);
        this.debugflag = debugflag;

        // START Zeitmessung
        long beginn = System.currentTimeMillis();

        // S/W-Bild aus gelichfarbigen Stellen erstellen
        boolean[][] gleichfarbigeStellen = scanneAufGleicheFelder();

        // S/W-Bild durchsuchen lassen
        RhinozelfantSucher rhinozelfantSucher = new RhinozelfantSucher(gleichfarbigeStellen);

        // Rhinozelfanten weiß färben
        faerbeStellen(rhinozelfantSucher.getRhinozelfantenFelder());

        long laufzeit = System.currentTimeMillis() - beginn;
        System.out.println(laufzeit);
    }

    // Suche nach gleichfarbigen Stellen
    private boolean[][] scanneAufGleicheFelder() {
        // Erstellung eines Arrays für 2D-Bild
        boolean[][] gleichfarbigeStellen = new boolean[bild.getWidth()][bild.getHeight()];

        for (int x = 1; x < bild.getWidth() - 1; x++) {
            for (int y = 1; y < bild.getHeight() - 1; y++) {
                   gleichfarbigeStellen[x][y] = istHautschuppe(x, y);
            }
        }

        // DEBUGTIME?
        // Wenn ja jetzt eine Deepcopy der gleichfarbigen Bilder für die Kontrollgrafik
        // Sonst kann man sich die Zeit sparen ;-)
        if (debugflag) debugGleichfarbige = util.deepCopy(gleichfarbigeStellen);

        return gleichfarbigeStellen;
    }

    private boolean istHautschuppe (int x, int y) {
        int rgbFeld = bild.getRGB(x, y);
        int gleichfarbigeNachbarfelder = -1; // Das gleiche Feld wird markiert werden
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                int rgbNachbar = bild.getRGB(i, j);
                if (rgbNachbar == rgbFeld) gleichfarbigeNachbarfelder++;
            }
        }
        return (gleichfarbigeNachbarfelder > 2);
    }
    // Färbt die Stelle im Bild weiß
    private BufferedImage faerbeStellen(Set<int[]> stellen) {
        BufferedImage ausgabebild = bild;

        for (int[] koordinate : stellen) {
            ausgabebild.setRGB(koordinate[0], koordinate[1], weiss);
        }
        return ausgabebild;
    }

    public BufferedImage getGefiltertesBild() {
        return bild;
    }

    public boolean[][] getDebugGleichfarbige() {
        // Wenn die Kopie außerhalb des Debuggings abgerufen wird,
        // ist das eigentlich kein katastrophaler Fehler
        // der zum Abbruch zwingt, jedoch wollte ich nicht noch eine extra
        // Exception einführen.
        if (debugGleichfarbige == null) {
            System.err.println("Bild: Deuginformation wurden abgerufen, obwohl sich das Programm nicht im Debugmodus befindet");
            // Radikale Lösung, der Fehler dürfte zur Laufzeit bei korrekter Programmierung nicht auftreten!
            System.exit(-1);
        }
        return debugGleichfarbige;
    }
}
