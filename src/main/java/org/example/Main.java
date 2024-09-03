package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.state.ApplicationState;
import org.example.storage.OpenTabsDao;
import org.example.ui.MainFrame;
import org.example.state.State;
import org.example.storage.RequestDAO;

public class Main extends Application {

    private RequestDAO requestDAO;
    private OpenTabsDao openTabsDao;
    private ApplicationState applicationState;
    private State state;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize DBUtil and State here
        requestDAO = new RequestDAO();
        openTabsDao = new OpenTabsDao();
        applicationState = new ApplicationState(requestDAO, openTabsDao);
        // Create and show the main frame
        MainFrame mainFrame = new MainFrame(applicationState);
        mainFrame.start(primaryStage);
    }
}