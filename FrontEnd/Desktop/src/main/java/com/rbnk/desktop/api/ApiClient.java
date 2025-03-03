package com.rbnk.desktop.api;

import com.google.gson.Gson;
import com.rbnk.desktop.DTO.LoginRequest;
import com.rbnk.desktop.DTO.LoginResponse;
import com.rbnk.desktop.DTO.UserRegistrationRequest;
import com.rbnk.desktop.session.UserSession;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ApiClient {

    private final String BASEURL = "http://localhost:8080/api";
    private final HttpClient client;
    private final Gson gson;

    public ApiClient() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Attempts to log in a user
     * @param username The username
     * @param password The password
     * @return CompletableFuture that resolves to true if login succeeded, false otherwise
     */

    public CompletableFuture<Boolean> login(String username, String password) {
        LoginRequest lr = new LoginRequest(username, password);
        String requestBody = gson.toJson(lr);

        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(BASEURL + "/users/login")).
                header("Content-Type", "application/json").
                POST(HttpRequest.BodyPublishers.ofString(requestBody)).
                build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        LoginResponse loginResponse = gson.fromJson(response.body(), LoginResponse.class);

                        UserSession.getInstance().setUserDetails(
                                loginResponse.getId(),
                                loginResponse.getUsername(),
                                loginResponse.getFname(),
                                loginResponse.getSname(),
                                loginResponse.getEmail(),
                                loginResponse.getRoleId()
                        );
                        return true;
                    } else {
                        return false;
                    }
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    return false;
                });
    }


    public CompletableFuture<Boolean> register(String username,
                                               String firstname,
                                               String surname,
                                               String email,
                                               String password,
                                               Integer roleId) {
        UserRegistrationRequest ur = new UserRegistrationRequest(
                username, firstname, surname, email, password, roleId
        );

        ur.setProjects(null);

        String requestBody = gson.toJson(ur);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASEURL + "/users/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> response.statusCode() == 201)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return false;
                });
    }
}
