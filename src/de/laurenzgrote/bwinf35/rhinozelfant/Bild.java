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

        // Scan auf gleichfarbige Punkte in gleicher Spalte
        for (int x = bild.getMinX(); x < bild.getWidth(); x++) {
            for (int y = bild.getMinY(); y < bild.getHeight() - 1; y++) {
                // P(x|y) == P(x|y+1) --> Feld darunter daneben gleiche Farbe
                // Daher auch in der Schleife -1, das tiefste Feld in der Spalte hat keinen weiteren Nachbar
                if (sindFarbenGleich(x, y, x, y + 1)) {
                    // Beide stellen können als gleichfarbig gespeichert werden!
                    gleichfarbigeStellen[x][y] = true;
                    gleichfarbigeStellen[x][y + 1] = true;
                }
            }
        }
        // Scan auf gleichfarbige Punkte in gleicher Zeile
        for (int y = bild.getMinY(); y < bild.getHeight(); y++) {
            // P(x|y) == P(x + 1|y) --> Feld rechts daneben gleiche Farbe
            // Daher auch in der Schleife -1, das letzte Feld in der Zeile hat keinen weiteren Nachbar
            for (int x = bild.getMinX(); x < bild.getWidth() - 1; x++) {
                if (sindFarbenGleich(x, y, x + 1,y)) {
                    // Beide stellen können als gleichfarbig gespeichert werden!
                    gleichfarbigeStellen[x][y] = true;
                    gleichfarbigeStellen[x + 1][y] = true;
                }
            }
        }

        // DEBUGTIME?
        // Wenn ja jetzt eine Deepcopy der gleichfarbigen Bilder für die Kontrollgrafik
        // Sonst kann man sich die Zeit sparen ;-)
        if (debugflag) debugGleichfarbige = util.deepCopy(gleichfarbigeStellen);

        return gleichfarbigeStellen;
    }

    // Vergleicht die Farben zweier Felder
    private boolean sindFarbenGleich(int aX, int aY, int bX, int bY) {
        int rgbA = bild.getRGB(aX, aY);
        int rgbB = bild.getRGB(bX, bY);

        return (rgbA == rgbB);
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
