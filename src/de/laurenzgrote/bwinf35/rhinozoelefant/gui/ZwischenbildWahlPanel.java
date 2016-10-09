package de.laurenzgrote.bwinf35.rhinozoelefant.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ZwischenbildWahlPanel extends JPanel implements ItemListener{
    JPanel zwischenbildPanel;
    final static String GLEICHFARBIGE = "1: Gleichfarbige Felder";
    final static String LINIEN = "2: Linien";
    final static String RECHTECKE = "3: Rechtecke";
    final static String ANATOMIE = "4: Anatomie";

    public ZwischenbildWahlPanel() {
        this.setLayout(new BorderLayout());

        JPanel comboBoxPane = new JPanel();
        String comboBoxItems[] = {GLEICHFARBIGE, LINIEN, RECHTECKE, ANATOMIE};
        JComboBox jComboBox = new JComboBox(comboBoxItems);
        jComboBox.setEditable(false);
        jComboBox.addItemListener(this);
        comboBoxPane.add(jComboBox);

        zwischenbildPanel = new JPanel();

        this.add(zwischenbildPanel, BorderLayout.CENTER);
        this.add(comboBoxPane, BorderLayout.SOUTH);
    }

    public void itemStateChanged(ItemEvent evt) {
        // Whatever LOL
    }
}
