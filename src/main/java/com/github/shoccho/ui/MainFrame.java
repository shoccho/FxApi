package com.github.shoccho.ui;

import com.github.shoccho.model.Request;
import com.github.shoccho.state.ApplicationState;
import com.github.shoccho.state.State;
import com.github.shoccho.ui.components.RequestPanel;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static java.lang.Math.max;

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
        Label titleLabel = new Label("History");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);
        ObservableList<Request> historyList = applicationState.getHistory();

        VBox historyView = new VBox();
        ScrollPane scrollPane = new ScrollPane(requestHistory);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        historyView.getChildren().addAll(titleLabel, scrollPane);

        historyList.addListener((ListChangeListener<Request>) observable -> {
            requestHistory.getChildren().clear();
            for (Request request : historyList) {
                Label itemLabel = new Label(request.getTitle());
                itemLabel.setOnMouseClicked(event -> applicationState.openTab(request.getId()));
                requestHistory.getChildren().add(itemLabel);
            }
        });

        for (Request request : historyList) {
            Label itemLabel = new Label(request.getTitle());
            itemLabel.setOnMouseClicked(event -> applicationState.openTab(request.getId()));
            requestHistory.getChildren().add(itemLabel);
        }

        Button addButton = new Button("+");
        addButton.setOnAction(e -> applicationState.openTab(null));
        ObservableList<State> openTabs = applicationState.getOpenTabs();
        openTabs.addListener((ListChangeListener<? super State>) change -> {
            while (change.next()) {
                change.getAddedSubList().forEach(state -> addTab(tabPane, state.getTitle(), state));
                change.next();
            }
        });

        applicationState.getOpenTabs().forEach(state -> addTab(tabPane, state.getTitle(), state));
        tabPane.getTabs().add(newTabButton(tabPane));

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(historyView, tabPane);
        splitPane.setDividerPositions(0.25);

        Scene scene = new Scene(splitPane, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab newTabButton(TabPane tabPane) {
        Tab addTab = new Tab("+");
        addTab.setClosable(false);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == addTab) {
                applicationState.openTab(null);
            }
        });
        return addTab;
    }

    private void addTab(TabPane tabPane, String title, State state) {
        Tab newTab = new Tab(title);
        Region content = new RequestPanel(state);
        newTab.setContent(content);
        newTab.setOnClosed(event -> applicationState.removeOpenTab(state.getRequest().getId()));

        tabPane.getTabs().add(max(0,tabPane.getTabs().size() - 1), newTab);
        tabPane.getSelectionModel().select(newTab);
    }
}
