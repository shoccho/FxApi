package org.example;

import org.example.apiCaller.ApiCaller;
import org.example.apiCaller.ApiResponse;
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

public class apiForm extends JFrame {
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

    public apiForm() {
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
        populateFields();

        setContentPane(mainPanel);
    }

    public void populateFields() {
        String queries = storage.readJSON("queries.json");
//        if (queries != null) {
        populateTab(queryTab, queries);
//        }

        String headers = storage.readJSON("headers.json");
//        if (headers != null) {
        populateTab(headersTab, headers);
//        }
    }

    public void populateTab(JPanel rootTab, String jsonString) {


        JPanel contentPanel = new JPanel();
        contentPanel.setAutoscrolls(true);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Single column layout
        if (jsonString != null) {

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                JPanel panel = new JPanel();
                panel.setLayout(new FlowLayout());

                String name = jsonObject.keys().next();
                String value = jsonObject.getString(name);

                JTextField nameField = new JTextField(name);
                JTextField valueField = new JTextField(value);
                nameField.setPreferredSize(new Dimension(200, 30));
                valueField.setPreferredSize(new Dimension(330, 30));
                JButton clearButton = new JButton("x");

                nameField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(e.getActionCommand());
                    }
                });

                panel.add(nameField);
                panel.add(valueField);
                panel.add(clearButton);

                contentPanel.add(panel);
            }
        }
        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            JPanel newPanel = new JPanel();
            newPanel.setLayout(new FlowLayout());

            JTextField newNameField = new JTextField();

            JTextField newValueField = new JTextField();
            newNameField.setPreferredSize(new Dimension(200, 30));
            newValueField.setPreferredSize(new Dimension(330, 30));

            JButton clearButton = new JButton("x");
            newPanel.add(newNameField);
            newPanel.add(newValueField);
            newPanel.add(clearButton);

            newNameField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {

                    storage.saveJSON("{name:test}", "fart");
                }

                @Override
                public void removeUpdate(DocumentEvent e) {

                    storage.saveJSON("{name:test}", "fart");
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    storage.saveJSON("{name:test}", "fart");
                }
            });
            contentPanel.add(newPanel);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);

        rootTab.setLayout(new BorderLayout());
        rootTab.add(contentPanel);
//        rootTab.add(scrollPane, BorderLayout.CENTER);
        rootTab.add(buttonPanel, BorderLayout.SOUTH);
    }
}
