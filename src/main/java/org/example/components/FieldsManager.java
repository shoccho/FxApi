package org.example.components;

import org.example.state.State;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class FieldsManager {
    private final State state;

    public FieldsManager(State state) {
        this.state = state;
    }


    public void populateFields(JPanel queryTab, JPanel headersTab, JPanel bodyTab) {
        populateTab(queryTab, state.getState("queries"), "queries");
        populateTab(headersTab, state.getState("headers"), "headers");
        populateTab(bodyTab, state.getState("body"), "body");
    }

    public void populateTab(JPanel rootTab, JSONObject jsonObject, String type) {

        JPanel contentPanel = new JPanel();
        contentPanel.setAutoscrolls(true);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Single column layout
        if (jsonObject != null) {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {

                String key = keys.next();
                FieldRow newRow = new FieldRow(key, jsonObject.getString(key), type, state);
                contentPanel.add(newRow);
            }
        }
        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            FieldRow newFieldRow = new FieldRow("", "", type, state);

            contentPanel.add(newFieldRow);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);

        rootTab.setLayout(new BorderLayout());
        rootTab.add(contentPanel);
        rootTab.add(buttonPanel, BorderLayout.SOUTH);
    }
}
