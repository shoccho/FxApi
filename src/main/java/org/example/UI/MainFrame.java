package org.example.UI;

import org.example.state.State;
import org.example.storage.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    public MainFrame(DBUtil db) {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 600);
        this.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();

        JList<String> list = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new Dimension(150, 0));
        this.add(listScrollPane, BorderLayout.WEST);

        JTabbedPane tabbedPane = new JTabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);

        JButton addButton = new JButton("+");
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel newPanel = new RequestPanel(new State(null, db));
                tabbedPane.addTab("Tab " , newPanel);
                tabbedPane.setSelectedComponent(newPanel);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout( new BorderLayout());
        buttonPanel.add(addButton, BorderLayout.EAST);
        this.add(buttonPanel, BorderLayout.NORTH);

        this.setVisible(true);
    }

}
