package org.example.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.example.state.ApplicationState;
import org.example.state.State;
import org.example.ui.components.RequestPanel;

public class MainFrame extends Application {


    private final ApplicationState applicationState;

    public MainFrame(ApplicationState applicationState) {

        this.applicationState = applicationState;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Telegraph Client");

        ListView<String> listView = new ListView<>();
        listView.setPrefWidth(150);
        TabPane tabPane = new TabPane();

        Button addButton = new Button("+");
        addButton.setOnAction(e -> {
            applicationState.openTab(null);
            State state = applicationState.getOpenTabs().get(applicationState.getOpenTabs().size() - 1);
            addTab(tabPane, state.getTitle(), new RequestPanel(state));
        });
        try {
            applicationState.getOpenTabs().forEach(state -> {
                addTab(tabPane, "Request", new RequestPanel(state));
            });
        } catch (Exception e) {
        }
        BorderPane borderPane = new BorderPane();
        borderPane.setRight(addButton);
        borderPane.setLeft(listView);
        borderPane.setCenter(tabPane);

        Scene scene = new Scene(borderPane, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addTab(TabPane tabPane, String title, Region content) {
        Tab newTab = new Tab(title);
        newTab.setContent(content);

        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
    }
}
