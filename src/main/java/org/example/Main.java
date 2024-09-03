package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.state.ApplicationState;
import org.example.storage.DBConnection;
import org.example.storage.OpenTabsDao;
import org.example.ui.MainFrame;
import org.example.state.State;
import org.example.storage.RequestDAO;

public class Main extends Application {

    private RequestDAO requestDAO;
    private OpenTabsDao openTabsDao;
    private ApplicationState applicationState;
    private State state;
    private DBConnection dbConnection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        dbConnection = new DBConnection();
        requestDAO = new RequestDAO(dbConnection);
        openTabsDao = new OpenTabsDao(dbConnection);
        applicationState = new ApplicationState(requestDAO, openTabsDao);
        MainFrame mainFrame = new MainFrame(applicationState);
        mainFrame.start(primaryStage);
    }
}