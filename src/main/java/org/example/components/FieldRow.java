package org.example.components;

import org.example.components.actions.DeleteField;
import org.example.components.actions.UpdateField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class FieldRow extends JPanel {
    private final String id;
    private String rowName;
    private String rowValue;
    private final JTextField nameField;
    private final JTextField valueField;
    private final String type;
    private final DeleteField deleteField;
    private final UpdateField updateField;

    public void setRowName(String name) {
        this.rowName = name;
    }

    public void setRowValue(String value) {
        this.rowValue = value;
    }


    public void updateState() {
        this.updateField.execute(this.type, this.id, this.rowName, this.rowValue);
    }

    public FieldRow(String id, String name, String value, String type, DeleteField deleteField, UpdateField updateField) {
        super();
        this.id = id;
        this.rowName = name;
        this.rowValue = value;
        this.nameField = new JTextField(this.rowName);
        this.valueField = new JTextField(this.rowValue);
        this.type = type;
        this.deleteField = deleteField;
        this.updateField = updateField;
        loadLayout();
    }

    public void loadLayout() {
        this.setLayout(new FlowLayout());
        nameField.setPreferredSize(new Dimension(200, 30));
        valueField.setPreferredSize(new Dimension(330, 30));
        JButton clearButton = new JButton("x");

        nameField.getDocument().addDocumentListener(new FieldRowListener(this, "name"));

        valueField.getDocument().addDocumentListener(new FieldRowListener(this, "value"));
        clearButton.addActionListener(this::clearField);

        this.add(nameField);
        this.add(valueField);
        this.add(clearButton);
    }

    public void clearField(ActionEvent _event) {
        this.deleteField.execute(this.type, this.id);
    }

    private static class FieldRowListener implements DocumentListener {

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
            this.row.updateState();
        }
    }
}
