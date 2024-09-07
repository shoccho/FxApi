package com.github.shoccho;

import javafx.application.Application;
import javafx.stage.Stage;
import com.github.shoccho.state.ApplicationState;
import com.github.shoccho.storage.DBConnection;
import com.github.shoccho.storage.OpenTabsDao;
import com.github.shoccho.ui.MainFrame;
import com.github.shoccho.state.State;
import com.github.shoccho.storage.RequestDAO;

public class Main extends Application {

    private RequestDAO requestDAO;
    private OpenTabsDao openTabsDao;
    private ApplicationState applicationState;
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