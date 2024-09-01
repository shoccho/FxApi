package org.example;

import org.example.UI.MainFrame;
import org.example.UI.RequestPanel;
import org.example.state.State;
import org.example.storage.DBUtil;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DBUtil db = new DBUtil();
        State state = new State(null, db);

        SwingUtilities.invokeLater(() -> new MainFrame(db));
    }
}