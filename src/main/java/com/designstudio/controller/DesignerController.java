package com.designstudio.controller;

import com.designstudio.model.Designer;
import com.designstudio.service.DesignerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class DesignerController {

    @FXML private TextField nameField;
    @FXML private TextField specField;
    @FXML private TextField emailField;
    @FXML private TextField searchField;

    @FXML private TableView<Designer> table;
    @FXML private TableColumn<Designer, Integer> idColumn;
    @FXML private TableColumn<Designer, String> nameColumn;
    @FXML private TableColumn<Designer, String> specColumn;
    @FXML private TableColumn<Designer, String> emailColumn;

    private final DesignerService service = new DesignerService();
    private final ObservableList<Designer> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        specColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        table.setItems(data);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                nameField.setText(val.getFullName());
                specField.setText(val.getSpecialization());
                emailField.setText(val.getEmail());
            }
        });

        searchField.textProperty().addListener((obs, old, val) -> applySearch(val));

        refresh();
    }

    @FXML
    private void onAdd() {
        try {
            Designer designer = new Designer(nameField.getText(), specField.getText(), emailField.getText());
            service.create(designer);
            refresh();
            clearForm();
            info("Дизайнер добавлен");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        Designer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("Выберите дизайнера");
            return;
        }
        try {
            selected.setFullName(nameField.getText());
            selected.setSpecialization(specField.getText());
            selected.setEmail(emailField.getText());
            service.update(selected);
            refresh();
            clearForm();
            info("Дизайнер обновлён");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        Designer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("Выберите дизайнера");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить дизайнера \"" + selected.getFullName() + "\"?",
                ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.delete(selected.getId());
                refresh();
                clearForm();
                info("Дизайнер удалён");
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
                .filter(d -> d.getFullName().toLowerCase().contains(lower)
                        || d.getSpecialization().toLowerCase().contains(lower)
                        || d.getEmail().toLowerCase().contains(lower))
                .toList());
    }

    private void clearForm() {
        nameField.clear();
        specField.clear();
        emailField.clear();
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private void error(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}