package org.example.components;

import org.example.state.State;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

public class FieldsManager {
    private final State state;
    HashMap<String, JPanel> tabs;
    private Action clearRowAction;

    public FieldsManager(State state, JPanel headersTab, JPanel queryTab, JPanel bodyTab) {
        this.state = state;
        this.tabs = new HashMap<>();
        this.tabs.put("headers", headersTab);
        this.tabs.put("queries", queryTab);
        this.tabs.put("body", bodyTab);

        this.clearRowAction = new Action() {
            @Override
            public void execute(String key) {
                populateFields(new String[]{key});
            }
        };
    }

    //TODO: make this an array pls
    public void populateFields(String[] keys) {
        for (String key : keys) {
            populateTab(this.tabs.get(key), state.getState(key), key);
        }
    }

    public void populateTab(JPanel rootTab, JSONObject jsonObject, String type) {

        JPanel contentPanel = new JPanel();
        contentPanel.setAutoscrolls(true);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Single column layout
        if (jsonObject != null) {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {

                String key = keys.next();
                FieldRow newRow = new FieldRow(key, jsonObject.getString(key), type, state, clearRowAction);
                contentPanel.add(newRow);
            }
        }
        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            FieldRow newFieldRow = new FieldRow("", "", type, state, clearRowAction);

            contentPanel.add(newFieldRow);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        rootTab.removeAll();
        rootTab.setLayout(new BorderLayout());
        rootTab.add(contentPanel);
        rootTab.add(buttonPanel, BorderLayout.SOUTH);
        rootTab.revalidate();
        rootTab.repaint();
    }
}
