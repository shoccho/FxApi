package org.example.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Objects;

public class FieldRow extends JPanel {
    private int row;
    private String rowName;
    private String rowValue;
    private JTextField nameField;
    private JTextField valueField;


    public String getRowName() {
        return this.rowName;
    }

    public void setRowName(String name) {
        this.rowName = name;
    }

    public String getRowValue() {
        return rowValue;
    }

    public void setRowValue(String value) {
        this.rowValue = value;
    }

    public FieldRow(int row, String name, String value) {
        super();
        this.row = row;
        this.rowName = name;
        this.rowValue = value;
        this.nameField = new JTextField(name);
        this.valueField = new JTextField(value);
        loadLayout();
    }

    public void loadLayout() {
        this.setLayout(new FlowLayout());
        nameField.setPreferredSize(new Dimension(200, 30));
        valueField.setPreferredSize(new Dimension(330, 30));
        JButton clearButton = new JButton("x");

        nameField.getDocument().addDocumentListener(new FieldRowListener(this, "name"));

        valueField.getDocument().addDocumentListener(new FieldRowListener(this, "value"));

        this.add(nameField);
        this.add(valueField);
        this.add(clearButton);
    }

    private class FieldRowListener implements DocumentListener {

        private final FieldRow row;
        private final String fieldType;

        private FieldRowListener(FieldRow row, String value) {
            this.row = row;
            this.fieldType = value;
        }


        @Override
        public void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update(e);
        }

        private void update(DocumentEvent e) {
            if (Objects.equals(fieldType, "value")) {
                this.row.setRowValue(this.row.valueField.getText());
            } else {
                this.row.setRowName(this.row.nameField.getText());
            }
        }
    }
}
