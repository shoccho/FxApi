package org.example.UI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.example.state.State;
import org.example.storage.DBUtil;

public class MainFrame extends Application {

    private final DBUtil db;

    public MainFrame(DBUtil db) {
        this.db = db;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Main Frame");

        ListView<String> listView = new ListView<>();
        listView.setPrefWidth(150);
        TabPane tabPane = new TabPane();

        Button addButton = new Button("+");
        addButton.setOnAction(e -> addTab(tabPane, "Tab ", new RequestPanel(new State(null, db))));

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
