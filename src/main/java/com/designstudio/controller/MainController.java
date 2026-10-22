package com.designstudio.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    public void initialize() {
        showClients();
    }

    @FXML
    private void showClients() {
        loadView("/fxml/clients.fxml");
    }

    @FXML
    private void showDesigners() {
        loadView("/fxml/designers.fxml");
    }

    @FXML
    private void showOrders() {
        loadView("/fxml/orders.fxml");
    }

    @FXML
    private void showStatistics() {
        loadView("/fxml/statistics.fxml");
    }

    private void loadView(String path) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(path));
            contentPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}