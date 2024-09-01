package org.example.UI;

import org.example.apiCaller.ApiCaller;
import org.example.components.FieldsManager;
import org.example.model.Request;
import org.example.model.ResponseData;
import org.example.state.State;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.IOException;

public class RequestPanel extends JPanel {
    private JComboBox<String> requestType;
    private JTextField urlBar;
    private JButton sendButton;
    private JTabbedPane tabs;
    private JTextArea responseTextArea;
    private JPanel bodyTab;
    private JPanel queryTab;
    private JPanel headersTab;
    private JLabel statusCodeLabel;

    private final ApiCaller apiCaller;
    private final State state;

    FieldsManager fieldsManager;

    public RequestPanel(State state) {
        this.state = state;
        this.apiCaller = new ApiCaller(state);

        initComponents();

        setLayout(new BorderLayout());
        add(createTopPanel(), BorderLayout.NORTH);
        add(createTabsPanel(), BorderLayout.CENTER);
        add(createResponsePanel(), BorderLayout.SOUTH);

        fieldsManager = new FieldsManager(state, headersTab, queryTab, bodyTab);

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

        requestType.addActionListener(e -> state.saveMethod(requestType.getSelectedItem().toString()));

        sendButton.addActionListener(actionEvent -> {
            try {
                ResponseData response = apiCaller.callApi();
                statusCodeLabel.setText("Status Code: " + response.getCode());
                responseTextArea.setText(response.getMessage());
            } catch (IOException e) {
                responseTextArea.setText(e.getMessage());
            }
        });
    }

    private void initComponents() {
        requestType = new JComboBox<>(new String[]{"GET", "POST", "PUT", "DELETE"});
        urlBar = new JTextField(30);
        sendButton = new JButton("Send");
        tabs = new JTabbedPane();
        responseTextArea = new JTextArea(10, 30);
        responseTextArea.setEditable(false);
        statusCodeLabel = new JLabel("Status Code: ");

        bodyTab = new JPanel();
        queryTab = new JPanel();
        headersTab = new JPanel();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.add(requestType);
        topPanel.add(new JLabel("URL:"));
        topPanel.add(urlBar);
        topPanel.add(sendButton);
        return topPanel;
    }

    private JPanel createTabsPanel() {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabs.addTab("Headers", headersTab);
        tabs.addTab("Queries", queryTab);
        tabs.addTab("Body", bodyTab);

        tabPanel.add(tabs, BorderLayout.CENTER);
        return tabPanel;
    }

    private JPanel createResponsePanel() {
        JPanel responsePanel = new JPanel(new BorderLayout());
        responsePanel.add(statusCodeLabel, BorderLayout.NORTH);
        responsePanel.add(new JScrollPane(responseTextArea), BorderLayout.CENTER);
        return responsePanel;
    }
}
