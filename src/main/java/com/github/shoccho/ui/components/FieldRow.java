package com.github.shoccho.ui.components;

import com.github.shoccho.ui.components.actions.DeleteField;
import com.github.shoccho.ui.components.actions.UpdateField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class FieldRow extends HBox {
    private final Integer id;
    private String rowName;
    private String rowValue;
    private final TextField nameField;
    private final TextField valueField;
    private final String type;
    private final DeleteField deleteField;
    private final UpdateField updateField;

    public FieldRow(Integer id, String name, String value, String type, DeleteField deleteField, UpdateField updateField) {
        this.id = id;
        this.rowName = name;
        this.rowValue = value;
        this.nameField = new TextField(this.rowName);
        this.valueField = new TextField(this.rowValue);
        this.type = type;
        this.deleteField = deleteField;
        this.updateField = updateField;

        loadLayout();
    }

    public void setRowName(String name) {
        this.rowName = name;
    }

    public void setRowValue(String value) {
        this.rowValue = value;
    }

    public void updateState() {
        this.updateField.execute(this.type, this.id, this.rowName, this.rowValue);
    }

    private void loadLayout() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        nameField.setPrefWidth(200);
        valueField.setPrefWidth(330);

        Button clearButton = new Button("x");

        nameField.textProperty().addListener(new FieldRowListener("name"));
        valueField.textProperty().addListener(new FieldRowListener("value"));
        clearButton.setOnAction(e -> clearField());

        this.getChildren().addAll(nameField, valueField, clearButton);
    }

    private void clearField() {
        this.deleteField.execute(this.type, this.id);
    }

    private class FieldRowListener implements ChangeListener<String> {
        private final String fieldType;

        private FieldRowListener(String fieldType) {
            this.fieldType = fieldType;
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (fieldType.equals("value")) {
                setRowValue(valueField.getText());
            } else {
                setRowName(nameField.getText());
            }
            updateState();
        }
    }
}
