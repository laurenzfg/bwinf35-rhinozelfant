package de.laurenzgrote.bwinf35.rhinozoelefant.gui;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    public GUI() {
        super ("Rhinozelfant: Laurenz Grote");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1200, 700);

        Container container = this.getContentPane();
        container.add(getCenterPanel(), BorderLayout.CENTER);
        container.add(getSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private JPanel getCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 3));

        JPanel originalBild = new JPanel();
        originalBild.setBorder(BorderFactory.createTitledBorder("Originalbild"));

        ZwischenbildWahlPanel zwischenBild = new ZwischenbildWahlPanel();
        zwischenBild.setBorder(BorderFactory.createTitledBorder("Zwischenbilder"));

        JPanel endBild = new JPanel();
        endBild.setBorder(BorderFactory.createTitledBorder("Endbild"));

        centerPanel.add(originalBild);
        centerPanel.add(zwischenBild);
        centerPanel.add(endBild);

        return centerPanel;
    }

    private JPanel getSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1,2));

        JButton bildLaden = new JButton("Bilddatei laden");
        JButton bildExportieren = new JButton("Bilddatei exportieren");

        southPanel.add(bildLaden);
        southPanel.add(bildExportieren);

        return southPanel;
    }
}
