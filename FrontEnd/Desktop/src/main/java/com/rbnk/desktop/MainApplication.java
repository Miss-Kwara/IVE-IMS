package com.rbnk.desktop;

import com.rbnk.desktop.api.ApiClient;
import com.rbnk.desktop.ui.LoginView;
import com.rbnk.desktop.ui.RegistrationView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private static MainApplication instance;
    private Stage primaryStage;
    private ApiClient client;

    public static MainApplication getInstance() {
        return instance;
    }

    public ApiClient getApiClient() {
        return client;
    }

    public void showLoginView() {
        LoginView loginView = new LoginView(client);
        Scene scene = new Scene(loginView, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showRegistrationView() {
        RegistrationView registrationView = new RegistrationView(client);
        Scene scene = new Scene(registrationView, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        this.primaryStage = primaryStage;
        client = new ApiClient();

        primaryStage.setTitle("Desktop Inventory Management System");
        showLoginView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
