package org.example;

import org.example.apiCaller.ApiCaller;
import org.example.apiCaller.ApiResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
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


    public apiForm() {
        setTitle("kire api!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String url = urlBar.getText();
                Object method = requestType.getSelectedItem();
                System.out.println("url:" + url + "\n method:" + requestType.getSelectedItem());
                try {
                    ApiResponse response = ApiCaller.callApi(url, method.toString());
                    statusCodeLabel.setText("Status Code: " + response.getCode());
                    responseTextArea.setText(response.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //add into query
        String queries = "[{name:test}, {name:shoccho}]";
        populateTab(queryTab, queries);
        //add to headers
        String headers = "[{name:test}, {name:shoccho}]";
        populateTab(headersTab, headers);
        setContentPane(mainPanel);
    }

    public void populateTab(JPanel rootTab, String jsonString) {
        JSONArray jsonArray = new JSONArray(jsonString);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(jsonArray.length() + 1, 1, 10, 0));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1, 2, 10, 0));

            String name = jsonObject.keys().next();
            String value = jsonObject.getString(name);

            JTextField nameField = new JTextField(name);
            JTextField valueField = new JTextField(value);

            panel.add(nameField);
            panel.add(valueField);

            mainPanel.add(panel);
        }

        JPanel emptyPanel = new JPanel();
        JTextField nameField = new JTextField("");
        JTextField valueField = new JTextField("");
        emptyPanel.add(nameField);
        emptyPanel.add(valueField);

        mainPanel.add(emptyPanel);

        rootTab.setLayout(new GridLayout(1, 1));
        rootTab.add(mainPanel);
    }
}
