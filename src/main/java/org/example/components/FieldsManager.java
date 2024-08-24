package org.example.components;

import com.sun.net.httpserver.Headers;
import org.example.storage.Storage;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

import static java.util.Arrays.stream;

public class FieldsManager {
    private final Storage storage;
    private HashMap<String, String> headers;
    private HashMap<String, String> queries;

    public FieldsManager(Storage storage) {
        this.storage = storage;
        this.headers = new HashMap<>();
        this.queries = new HashMap<>();
        loadData();
    }

    public HashMap<String, String> loadData() {
        String queries = storage.readJSON("queries.json");
        if (queries == null) return null;
        Object queriesObj = JSONObject.stringToValue(queries);
        stream(queriesObj.getClass().getFields()).map(e -> {
            System.out.println("field" + e.toString());
            return null;
        });
        return null;
    }

    public void populateFields(JPanel queryTab, JPanel headersTab) {

        String queries = storage.readJSON("queries.json");
        populateTab(queryTab, queries);

        String headers = storage.readJSON("headers.json");

        populateTab(headersTab, headers);
    }

    public void populateTab(JPanel rootTab, String jsonString) {

        JPanel contentPanel = new JPanel();
        contentPanel.setAutoscrolls(true);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Single column layout
        if (jsonString != null) {

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String key = jsonObject.keys().next();
                FieldRow newRow = new FieldRow(i, key, jsonObject.getString(key));

                contentPanel.add(newRow);
            }
        }
        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            FieldRow newFieldRow = new FieldRow(0, "", "");

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
