package de.laurenzgrote.bwinf35.rhinozelfant.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


class ImagePanel extends JPanel {
    private final static int schwarz = Color.BLACK.getRGB();

    private Image backgroundImage;

    void setBackgroundImage(BufferedImage backgroundImage) {
        double maxWidth = getWidth() - 40;

        this.backgroundImage = backgroundImage.getScaledInstance((int) maxWidth, -1, Image.SCALE_DEFAULT);
        this.repaint();
    }
    void setBackgroundImage(boolean[][] swBild) {
        setBackgroundImage(schreibeSWBild(swBild));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (backgroundImage != null) {
            graphics.drawImage(backgroundImage, 20, 20, this);
        }
    }

    // Helper der aus dem S/W bool Array ein BufferedImage macht
    private static BufferedImage schreibeSWBild(boolean[][] daten) {
        BufferedImage ausgabebild = new BufferedImage(daten.length, daten[0].length, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < daten.length; x++) {
            for (int y = 0; y < daten[0].length; y++) {
                if (daten[x][y]) {
                    ausgabebild.setRGB(x, y, schwarz);
                }
            }
        }
        return ausgabebild;
    }
}
