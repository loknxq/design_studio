package com.designstudio.controller;

import com.designstudio.model.Client;
import com.designstudio.service.ClientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class ClientController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField searchField;

    @FXML private TableView<Client> table;
    @FXML private TableColumn<Client, Integer> idColumn;
    @FXML private TableColumn<Client, String> nameColumn;
    @FXML private TableColumn<Client, String> emailColumn;
    @FXML private TableColumn<Client, String> phoneColumn;

    private final ClientService service = new ClientService();
    private final ObservableList<Client> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        table.setItems(data);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                nameField.setText(val.getFullName());
                emailField.setText(val.getEmail());
                phoneField.setText(val.getPhone());
            }
        });

        searchField.textProperty().addListener((obs, old, val) -> applySearch(val));

        refresh();
    }

    @FXML
    private void onAdd() {
        try {
            Client client = new Client(nameField.getText(), emailField.getText(), phoneField.getText());
            service.create(client);
            refresh();
            clearForm();
            info("Клиент добавлен");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        Client selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("Выберите клиента");
            return;
        }
        try {
            selected.setFullName(nameField.getText());
            selected.setEmail(emailField.getText());
            selected.setPhone(phoneField.getText());
            service.update(selected);
            refresh();
            clearForm();
            info("Клиент обновлён");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        Client selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("Выберите клиента");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить клиента \"" + selected.getFullName() + "\"?",
                ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.delete(selected.getId());
                refresh();
                clearForm();
                info("Клиент удалён");
            } catch (Exception e) {
                error(e.getMessage());
            }
        }
    }

    @FXML
    private void onRefresh() {
        searchField.clear();
        refresh();
        clearForm();
    }

    @FXML
    private void onClear() {
        clearForm();
        table.getSelectionModel().clearSelection();
    }

    private void refresh() {
        data.setAll(service.getAll());
    }

    private void applySearch(String text) {
        if (text == null || text.isBlank()) {
            refresh();
            return;
        }
        String lower = text.toLowerCase();
        data.setAll(service.getAll().stream()
                .filter(c -> c.getFullName().toLowerCase().contains(lower)
                        || c.getEmail().toLowerCase().contains(lower)
                        || c.getPhone().toLowerCase().contains(lower))
                .toList());
    }

    private void clearForm() {
        nameField.clear();
        emailField.clear();
        phoneField.clear();
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private void error(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}