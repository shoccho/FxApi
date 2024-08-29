package org.example;

import org.example.state.State;
import org.example.storage.Storage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Storage storage = new Storage(".");
        State state = new State(storage, 0);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RequestPanel(state);
            }
        });
    }
}