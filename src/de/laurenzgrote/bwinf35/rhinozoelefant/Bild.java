package de.laurenzgrote.bwinf35.rhinozoelefant;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Bild{
    private final int schwarz = Color.BLACK.getRGB();
    private final int weiss = Color.WHITE.getRGB();

    private BufferedImage originalbild;
    private BufferedImage ungefiltertesBild, gefiltertesBild;

    public Bild(File file) throws IOException {
        originalbild = ImageIO.read(file);

        boolean[][] gleichfarbigeStellen = scanneAufGleicheFelder();
        ungefiltertesBild = schreibeSWBild(gleichfarbigeStellen);

        RhinozoelefantSucher rhinozoelefantSucher = new RhinozoelefantSucher(gleichfarbigeStellen);
        gefiltertesBild = faerbeStellen(rhinozoelefantSucher.getRhinozoelefantenFelder());
    }

    private boolean[][] scanneAufGleicheFelder() {
        boolean[][] gleichfarbigeStellen = new boolean[originalbild.getWidth()][originalbild.getHeight()];

        // Zeilenweiser Scan
        for (int x = originalbild.getMinX(); x < originalbild.getWidth(); x++) {
            for (int y = originalbild.getMinY(); y < originalbild.getHeight() - 1; y++) {
                if (sindFarbenGleich(x, y, x, y + 1)) {
                    gleichfarbigeStellen[x][y] = true;
                    gleichfarbigeStellen[x][y + 1] = true;
                }
            }
        }
        // Spaltenweiser Scan
        for (int y = originalbild.getMinY(); y < originalbild.getHeight(); y++) {
            for (int x = originalbild.getMinX(); x < originalbild.getWidth() - 1; x++) {
                if (sindFarbenGleich(x, y, x + 1,y)) {
                    gleichfarbigeStellen[x][y] = true;
                    gleichfarbigeStellen[x + 1][y] = true;
                }
            }
        }

        return gleichfarbigeStellen;
    }

    private BufferedImage faerbeStellen(Set<int[]> stellen) {
        BufferedImage ausgabebild = originalbild;

        for (int[] koordinate : stellen) {
            ausgabebild.setRGB(koordinate[0], koordinate[1], weiss);
        }
        return ausgabebild;
    }

    private BufferedImage schreibeSWBild(boolean[][] daten) {
        BufferedImage ausgabebild = new BufferedImage(originalbild.getWidth(), originalbild.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < daten.length; x++) {
            for (int y = 0; y < daten[0].length; y++) {
                if (daten[x][y]) {
                    ausgabebild.setRGB(x, y, schwarz);
                }
            }
        }
        return ausgabebild;
    }

    private boolean sindFarbenGleich(int aX, int aY, int bX, int bY) {
        int rgbA = originalbild.getRGB(aX, aY);
        int rgbB = originalbild.getRGB(bX, bY);

        return (rgbA == rgbB);
    }
    public BufferedImage getOriginalbild() {
        return originalbild;
    }

    public BufferedImage getUngefiltertesBild() {
        return ungefiltertesBild;
    }

    public BufferedImage getGefiltertesBild() {
        return gefiltertesBild;
    }
}
