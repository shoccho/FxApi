package org.example.components;

import org.example.components.actions.DeleteField;
import org.example.components.actions.UpdateField;
import org.example.model.Parameter;
import org.example.state.State;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FieldsManager {
    private final State state;
    HashMap<String, JPanel> tabs;
    private final DeleteField clearRowAction;
    private final UpdateField updateFieldAction;

    public FieldsManager(State state, JPanel headersTab, JPanel queryTab, JPanel bodyTab) {
        this.state = state;
        this.tabs = new HashMap<>();
        this.tabs.put("headers", headersTab);
        this.tabs.put("queries", queryTab);
        this.tabs.put("body", bodyTab);

        this.clearRowAction = (key, id) -> {
            this.state.removeData(key, id);
            populateFields(new String[]{key});
        };
        this.
                populateFields(new String[]{"headers", "queries", "body"});
        updateFieldAction = state::updateState;
    }

    public void populateFields(String[] keys) {
        for (String key : keys) {
            populateTab(this.tabs.get(key), state.getState(key), key);
        }
    }

    public void populateTab(JPanel rootTab, ArrayList<Parameter> params, String type) {

        JPanel contentPanel = new JPanel();
        contentPanel.setAutoscrolls(true);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                FieldRow newRow = new FieldRow(i, params.get(i).getKey(), params.get(i).getValue(), type, clearRowAction, updateFieldAction);
                contentPanel.add(newRow);
            }
        }
        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            int length = this.state.getState(type).size();
            FieldRow newFieldRow = new FieldRow(length, "", "", type, clearRowAction, updateFieldAction);

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
