package org.example;

import org.example.apiCaller.ApiCaller;
import org.example.apiCaller.ApiResponse;
import org.example.components.FieldsManager;
import org.example.storage.Storage;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
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

    public UI() {
        setTitle("kire api!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);

        this.storage = new Storage(".");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String url = urlBar.getText();
                Object method = requestType.getSelectedItem();
                System.out.println("url:" + url + "\n Method:" + requestType.getSelectedItem());
                try {
                    ApiResponse response = ApiCaller.callApi(url, method.toString());
                    statusCodeLabel.setText("Status Code: " + response.getCode());
                    responseTextArea.setText(response.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        FieldsManager fieldsManager = new FieldsManager(storage);
        fieldsManager.populateFields(queryTab, headersTab);
        setContentPane(mainPanel);
    }

}
