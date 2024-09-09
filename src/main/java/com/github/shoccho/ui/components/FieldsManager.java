package com.github.shoccho.ui.components;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.github.shoccho.ui.components.actions.DeleteField;
import com.github.shoccho.ui.components.actions.UpdateField;
import com.github.shoccho.model.Parameter;
import com.github.shoccho.state.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FieldsManager {

    private final State state;
    private final HashMap<String, Tab> tabs;
    private final DeleteField clearRowAction;
    private final UpdateField updateFieldAction;
    private final ComboBox<String> bodyType;

    public FieldsManager(State state, TabPane tabPane) {
        this.state = state;
        this.tabs = new HashMap<>();

        Tab headersTab = new Tab("Headers");
        Tab queriesTab = new Tab("Queries");
        Tab bodyTab = new Tab("Body");
        bodyType = new ComboBox<>();

        bodyType.getItems().addAll("form data", "raw");
        bodyType.setValue(state.getBodyType());
        bodyType.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                state.setBodyType(newValue);
                populateFields(new String[]{"body"});

            }
        });
        this.tabs.put("headers", headersTab);
        this.tabs.put("queries", queriesTab);
        this.tabs.put("body", bodyTab);

        tabPane.getTabs().addAll(headersTab, queriesTab, bodyTab);

        this.clearRowAction = (key, id) -> {
            this.state.removeData(key, id);
            populateFields(new String[]{key});
        };
        this.updateFieldAction = state::updateState;

        populateFields(new String[]{"headers", "queries", "body"});
    }

    public void populateFields(String[] keys) {
        for (String key : keys) {
            populateTab(this.tabs.get(key), state.getState(key), key);
        }
    }

    public void populateTab(Tab tab, ArrayList<Parameter> params, String type) {
        VBox contentPanel = new VBox();
        contentPanel.setSpacing(5);
        if (type.equals("body")) {
            contentPanel.getChildren().add(bodyType);
            tab.setContent(contentPanel);
        }
        if (type.equals("body") && state.getBodyType().equals("raw")) {
            TextArea rawBodyArea = new TextArea();
            rawBodyArea.setPrefColumnCount(40);
            VBox.setVgrow(rawBodyArea, Priority.ALWAYS);

            rawBodyArea.setText(state.getRawBody());
            rawBodyArea.setStyle("-fx-background-color:red;");
            contentPanel.getChildren().add(rawBodyArea);
        } else {
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    FieldRow newRow = new FieldRow(i, params.get(i).getKey(), params.get(i).getValue(), type, clearRowAction, updateFieldAction);
                    contentPanel.getChildren().add(newRow);
                }
            }

            Button addButton = new Button("+");
            addButton.setOnAction(e -> {
                int length = this.state.getState(type).size();

//TODO: use observable list
                this.state.getState(type).add(new Parameter("", ""));
                FieldRow newFieldRow = new FieldRow(length, "", "", type, clearRowAction, updateFieldAction);
                contentPanel.getChildren().add(newFieldRow);
            });

            HBox buttonPanel = new HBox();
            HBox.setHgrow(buttonPanel, Priority.ALWAYS);
            buttonPanel.setStyle("-fx-alignment: top-right; -fx-padding: 5px");
            buttonPanel.getChildren().add(addButton);

            HBox borderPane = new HBox();
            VBox.setVgrow(contentPanel, Priority.ALWAYS);
            borderPane.getChildren().add(contentPanel);
            borderPane.getChildren().add(buttonPanel);

            tab.setContent(borderPane);
        }
    }
}
