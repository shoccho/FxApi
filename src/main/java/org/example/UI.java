package org.example;

import org.example.apiCaller.ApiCaller;
import org.example.apiCaller.ApiResponse;
import org.example.components.FieldsManager;
import org.example.state.State;
import org.example.storage.Storage;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UI extends JFrame {
    private JComboBox requestType;
    private JTextField urlBar;
    private JButton sendButton;
    private JTabbedPane tabs;
    private JTextArea responseTextArea;
    private JPanel bodyTab;
    private JPanel queryTab;
    private JPanel headersTab;
    private JPanel mainPanel;
    private JLabel statusCodeLabel;

    private Storage storage;
    private ApiCaller apiCaller;

    public UI() {
        setTitle("kire api!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);

        this.storage = new Storage(".");

        State state = new State(storage);
        this.apiCaller = new ApiCaller(state);

        FieldsManager fieldsManager = new FieldsManager(state, headersTab, queryTab, bodyTab);
        fieldsManager.populateFields(new String[]{"headers", "queries", "body"});
        setContentPane(mainPanel);


        urlBar.setText(state.getUrl());
        urlBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                state.saveUrl(urlBar.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                state.saveUrl(urlBar.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                state.saveUrl(urlBar.getText());
            }
        });
        requestType.addActionListener(e -> {
            state.saveMethod(requestType.getSelectedItem().toString());
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    ApiResponse response = apiCaller.callApi();
                    statusCodeLabel.setText("Status Code: " + response.getCode());
                    responseTextArea.setText(response.getMessage());
                } catch (IOException e) {
                    responseTextArea.setText(e.getMessage());
                }
            }
        });
    }

}
