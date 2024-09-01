package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.UI.MainFrame;
import org.example.state.State;
import org.example.storage.DBUtil;

public class Main extends Application {

    private DBUtil db;
    private State state;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize DBUtil and State here
        db = new DBUtil();

        // Create and show the main frame
        MainFrame mainFrame = new MainFrame(db);
        mainFrame.start(primaryStage);
    }
}