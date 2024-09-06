package com.github.shoccho.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import com.github.shoccho.apiCaller.ApiCaller;
import com.github.shoccho.model.ResponseData;
import com.github.shoccho.state.State;


public class RequestPanel extends SplitPane {
    private ComboBox<String> requestType;
    private TextField urlBar;
    private Button sendButton;
    private TabPane tabs;
    private TextArea responseTextArea;

    private Label statusCodeLabel;
    private final ApiCaller apiCaller;

    public RequestPanel(State state) {
        this.apiCaller = new ApiCaller(state);

        initComponents(state);
        setOrientation(Orientation.VERTICAL);
        setDividerPositions(0.5);
        VBox top = new VBox();
        top.getChildren().add(createTopPanel());
        top.getChildren().add(tabs);
        getItems().add(top);
        getItems().add(createResponsePanel());

        urlBar.setText(state.getUrl());
        urlBar.textProperty().addListener((obs, oldText, newText) -> state.saveUrl(newText));

        requestType.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                state.saveMethod(newValue);
            }
        });
        new FieldsManager(state, tabs);
        sendButton.setOnAction(actionEvent -> {
            state.saveToHistory();
            try {
                ResponseData response = apiCaller.callApi();
                statusCodeLabel.setText("Status Code: " + response.getCode());
                responseTextArea.setText(response.getMessage());

            } catch (Exception e) {
                responseTextArea.setText(e.getMessage());
            }
        });
    }

    private void initComponents(State state) {
        requestType = new ComboBox<>();
        requestType.getItems().addAll("GET", "POST", "PUT", "DELETE");
        requestType.setValue(state.getMethod());

        urlBar = new TextField();
        urlBar.setPrefWidth(500);
        sendButton = new Button("Send");
        tabs = new TabPane();

        responseTextArea = new TextArea();
        responseTextArea.setPrefColumnCount(80);
        responseTextArea.setEditable(false);

        statusCodeLabel = new Label("Status Code: ");
    }

    private HBox createTopPanel() {
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));
        topPanel.getChildren().addAll(
                requestType,
                new Label("URL:"),
                urlBar,
                sendButton
        );
        return topPanel;
    }

    private VBox createResponsePanel() {
        VBox responsePanel = new VBox();
        responsePanel.getChildren().add(statusCodeLabel);
VBox.setVgrow(responseTextArea, Priority.ALWAYS);
        responsePanel.getChildren().add(responseTextArea);
        return responsePanel;
    }

}
