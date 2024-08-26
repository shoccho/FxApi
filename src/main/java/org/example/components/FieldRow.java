package org.example.components;

import org.example.components.actions.DeleteField;
import org.example.components.actions.UpdateField;
import org.example.state.State;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Objects;

public class FieldRow extends JPanel {
    private final String id;
    private String rowName;
    private String rowValue;
    private JTextField nameField;
    private JTextField valueField;
    private final State state; //TODO: make this an action, rows shouldn't have direct access to state
    private String type;
    private DeleteField deleteField;
    private UpdateField updateField;

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


    public void updateState() {

        this.state.updateState(this.type, this.id, this.rowName, this.rowValue);
    }

    public FieldRow(String id, String name, String value, String type, State state, DeleteField deleteField) {
        super();
        this.id = id;
        this.rowName = name;
        this.rowValue = value;
        this.nameField = new JTextField(this.rowName);
        this.valueField = new JTextField(this.rowValue);
        this.state = state;
        this.type = type;
        this.deleteField = deleteField;

        loadLayout();
    }

    public void loadLayout() {
        this.setLayout(new FlowLayout());
        nameField.setPreferredSize(new Dimension(200, 30));
        valueField.setPreferredSize(new Dimension(330, 30));
        JButton clearButton = new JButton("x");

        nameField.getDocument().addDocumentListener(new FieldRowListener(this, "name"));

        valueField.getDocument().addDocumentListener(new FieldRowListener(this, "value"));
        clearButton.addActionListener(e -> {
            clearField();
        });

        this.add(nameField);
        this.add(valueField);
        this.add(clearButton);
    }

    public void clearField() {
        this.state.removeData(this.type, this.id);
        this.deleteField.execute(this.type);
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
