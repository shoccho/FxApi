package org.example.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.example.apiCaller.ApiCaller;
import org.example.model.ResponseData;
import org.example.state.State;


public class RequestPanel extends BorderPane {
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

        setTop(createTopPanel());
        setCenter(tabs);
        setBottom(createResponsePanel());

        urlBar.setText(state.getUrl());
        urlBar.textProperty().addListener((obs, oldText, newText) -> state.saveUrl(newText));

        requestType.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                state.saveMethod(newValue);
            }
        });
        FieldsManager fieldsManager = new FieldsManager(state, tabs);
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

    private BorderPane createResponsePanel() {
        BorderPane responsePanel = new BorderPane();
        responsePanel.setTop(statusCodeLabel);
        responsePanel.setCenter(new ScrollPane(responseTextArea));
        return responsePanel;
    }

}
