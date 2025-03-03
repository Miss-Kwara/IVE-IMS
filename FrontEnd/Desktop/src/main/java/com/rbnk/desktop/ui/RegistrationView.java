package com.rbnk.desktop.ui;

import com.rbnk.desktop.api.ApiClient;
import com.rbnk.desktop.MainApplication;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class RegistrationView extends VBox {
    private TextField usernameField;
    private TextField firstnameField;
    private TextField surnameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField roleIdField; // New field for role ID
    private Button registerButton;
    private Button backButton;
    private Label statusLabel;
    private ApiClient client;

    public RegistrationView(ApiClient client) {
        this.client = client;

        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
        setSpacing(20);

        Text title = new Text("Register");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Username
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);

        // First name
        Label firstnameLabel = new Label("First Name:");
        firstnameField = new TextField();
        firstnameField.setPromptText("Enter your first name");
        grid.add(firstnameLabel, 0, 1);
        grid.add(firstnameField, 1, 1);

        // Surname
        Label surnameLabel = new Label("Surname:");
        surnameField = new TextField();
        surnameField.setPromptText("Enter your surname");
        grid.add(surnameLabel, 0, 2);
        grid.add(surnameField, 1, 2);

        // Email
        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        grid.add(emailLabel, 0, 3);
        grid.add(emailField, 1, 3);

        // Password
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        grid.add(passwordLabel, 0, 4);
        grid.add(passwordField, 1, 4);

        // Confirm Password
        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        grid.add(confirmPasswordLabel, 0, 5);
        grid.add(confirmPasswordField, 1, 5);

        // Role ID field
        Label roleIdLabel = new Label("Role ID:");
        roleIdField = new TextField();
        roleIdField.setPromptText("Enter your role ID");
        grid.add(roleIdLabel, 0, 6);
        grid.add(roleIdField, 1, 6);

        // Buttons
        registerButton = new Button("Register");
        backButton = new Button("Back");
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(registerButton, backButton);
        grid.add(buttonBox, 1, 7);

        statusLabel = new Label();
        statusLabel.setWrapText(true);

        getChildren().addAll(title, grid, statusLabel);

        setupEventHandlers();
    }

    private void setupEventHandlers() {
        registerButton.setOnAction(e -> handleRegister());
        backButton.setOnAction(e -> goBack());
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String firstname = firstnameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String roleIdText = roleIdField.getText().trim();

        if(username.isEmpty() || firstname.isEmpty() || surname.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() || roleIdText.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        if(!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match.");
            return;
        }

        int roleId;
        try {
            roleId = Integer.parseInt(roleIdText);
        } catch (NumberFormatException e) {
            statusLabel.setText("Role ID must be a valid number.");
            return;
        }

        client.register(username, firstname, surname, email, password, roleId)
                .thenAccept(success -> {
                    Platform.runLater(() -> {
                        if(success) {
                            statusLabel.setText("Registration successful! Please log in.");
                            goBack();
                        } else {
                            statusLabel.setText("Registration failed. Please try again.");
                        }
                    });
                }).exceptionally(ex -> {
                    Platform.runLater(() -> statusLabel.setText("Error: " + ex.getMessage()));
                    return null;
                });
    }

    private void goBack() {
        MainApplication.getInstance().showLoginView();
    }
}
