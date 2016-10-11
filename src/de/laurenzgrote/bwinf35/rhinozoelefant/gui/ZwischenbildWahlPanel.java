package de.laurenzgrote.bwinf35.rhinozoelefant.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ZwischenbildWahlPanel extends JPanel implements ItemListener{
    private ImagePanel imagePanel;
    private final static String GLEICHFARBIGE = "1: Gleichfarbige Felder";
    private final static String LINIEN = "2: Linien";
    private final static String RECHTECKE = "3: Rechtecke";
    private final static String ANATOMIE = "4: Anatomie";

    private String aktiverEintrag = GLEICHFARBIGE;

    private boolean[][] gleichfarbigeBild;
    private boolean[][] linienBild;
    private boolean[][] rechteckeBild;
    private boolean[][] anatomieBild;

    public ZwischenbildWahlPanel() {
        this.setLayout(new BorderLayout());

        JPanel comboBoxPane = new JPanel();
        String comboBoxItems[] = {GLEICHFARBIGE, LINIEN, RECHTECKE, ANATOMIE};
        JComboBox<String> jComboBox = new JComboBox<>(comboBoxItems);
        jComboBox.setEditable(false);
        jComboBox.addItemListener(this);
        comboBoxPane.add(jComboBox);

        imagePanel = new ImagePanel();

        this.add(imagePanel, BorderLayout.CENTER);
        this.add(comboBoxPane, BorderLayout.SOUTH);
    }

    public void itemStateChanged(ItemEvent evt) {
        aktiverEintrag = (String) evt.getItem();
        updateImagePanel();
    }

    private void updateImagePanel() {
        if (aktiverEintrag.equals(GLEICHFARBIGE)) {
            imagePanel.setBackgroundImage(gleichfarbigeBild);
        } else if (aktiverEintrag.equals(LINIEN)) {
            imagePanel.setBackgroundImage(linienBild);
        } else if (aktiverEintrag.equals(RECHTECKE)) {
            imagePanel.setBackgroundImage(rechteckeBild);
        } else if (aktiverEintrag.equals(ANATOMIE)) {
            imagePanel.setBackgroundImage(anatomieBild);
        } else {
            System.err.println("ZwischenbildWahlPanel: Illegaler Status der Combobox");
        }
    }

    public void setGleichfarbigeBild(boolean[][] gleichfarbigeBild) {
        this.gleichfarbigeBild = gleichfarbigeBild;
        if (aktiverEintrag == GLEICHFARBIGE) updateImagePanel();
    }

    public void setLinienBild(boolean[][] linienBild) {
        this.linienBild = linienBild;
    }

    public void setRechteckeBild(boolean[][] rechteckeBild) {
        this.rechteckeBild = rechteckeBild;
    }

    public void setAnatomieBild(boolean[][] anatomieBild) {
        this.anatomieBild = anatomieBild;
    }
}
