package com.github.shoccho.ui;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.github.shoccho.model.Request;
import com.github.shoccho.state.ApplicationState;
import com.github.shoccho.state.State;
import com.github.shoccho.ui.components.RequestPanel;

public class MainFrame extends Application {

    private final ApplicationState applicationState;

    public MainFrame(ApplicationState applicationState) {

        this.applicationState = applicationState;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FxApi");

        VBox requestHistory = new VBox(10);

        requestHistory.setPrefWidth(150);
        TabPane tabPane = new TabPane();
        Label title = new Label("History");
        ObservableList<Request> historyList = applicationState.getHistory();

        BorderPane historyView = new BorderPane();
        historyView.setTop(title);
        historyView.setCenter(requestHistory);

        historyList.addListener((ListChangeListener<Request>) observable -> {
            requestHistory.getChildren().clear();
            for (Request request : historyList) {
                Label itemLabel = new Label(request.getTitleForUI());
                itemLabel.setOnMouseClicked(event -> {
                    applicationState.openTab(request.getId());
                });
                requestHistory.getChildren().add(itemLabel);
            }
        });

        historyList.forEach(request -> {
            Label itemLabel = new Label(request.getTitleForUI());
            itemLabel.setOnMouseClicked(event -> {
                applicationState.openTab(request.getId());
                State state = applicationState.getOpenTabs().get(applicationState.getOpenTabs().size() - 1);
                addTab(tabPane, state.getTitle(), state);
            });
            requestHistory.getChildren().add(itemLabel);
        });

        Button addButton = new Button("+");
        addButton.setOnAction(e -> {
            applicationState.openTab(null);

        });
        ObservableList<State> openTabs = applicationState.getOpenTabs();
        openTabs.addListener((ListChangeListener<? super State>) change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(state -> {
                    addTab(tabPane, state.getTitle(), state);
                });
            }
        });
        try {
            applicationState.getOpenTabs().forEach(state -> {
                addTab(tabPane, state.getTitle(), state);
            });
        } catch (Exception e) {
        }
        BorderPane borderPane = new BorderPane();
        borderPane.setRight(addButton);
        borderPane.setLeft(historyView);
        borderPane.setCenter(tabPane);

        Scene scene = new Scene(borderPane, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addTab(TabPane tabPane, String title, State state) {
        Tab newTab = new Tab(title);
        Region content = new RequestPanel(state);
        newTab.setContent(content);
        newTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                applicationState.removeOpenTab(state.getRequest().getId());
            }
        });

        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
    }
}
