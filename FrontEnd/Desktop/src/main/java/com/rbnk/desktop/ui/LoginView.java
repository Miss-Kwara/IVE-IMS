package com.rbnk.desktop.ui;

import com.rbnk.desktop.MainApplication;
import com.rbnk.desktop.api.ApiClient;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LoginView extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private Label statusLabel;
    private ApiClient client;

    public LoginView(ApiClient client) {
        this.client = client;

        //layout
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(20);

        // Title
        Text title = new Text("Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Create the form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Username field
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);

        // Password field
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        // Login button
        loginButton = new Button("Login");
        registerButton = new Button("Sign Up");
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().add(loginButton);
        buttonBox.getChildren().add(registerButton);
        grid.add(buttonBox, 1, 2);

        // Status label for displaying messages
        statusLabel = new Label();
        statusLabel.setWrapText(true);

        // Add components to the layout
        getChildren().addAll(title, grid, statusLabel);

        // Set up action handlers
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        loginButton.setOnAction(e -> handleLogin());

        passwordField.setOnAction(e -> handleLogin());

        registerButton.setOnAction(e -> goToSignUp());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty()) {
            statusLabel.setText("Please enter your username");
        }

        if (password.isEmpty()) {
            statusLabel.setText("Please enter your password");
        }

        client.login(username, password).thenAccept(success -> {
            Platform.runLater(() -> {
                if (success) {
                    statusLabel.setText("Login Successful");

                    // TO-DO : Navigate to Dashboards
                } else {
                    statusLabel.setText("Login Failed, please try again");
                    loginButton.setDisable(false);
                }
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> {
                statusLabel.setText("Error: " + ex.getMessage());
                loginButton.setDisable(false);
            });
            return null;
        });
    }

    private void goToSignUp() {
        MainApplication.getInstance().showRegistrationView();
    }
}
