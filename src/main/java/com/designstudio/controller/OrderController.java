package com.designstudio.controller;

import com.designstudio.model.Client;
import com.designstudio.model.DesignOrder;
import com.designstudio.model.Designer;
import com.designstudio.model.OrderStatus;
import com.designstudio.service.ClientService;
import com.designstudio.service.DesignOrderService;
import com.designstudio.service.DesignerService;
import com.designstudio.util.ExcelExporter;
import com.designstudio.util.ExcelExporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class OrderController {

    @FXML private TextField titleField;
    @FXML private TextField descField;
    @FXML private TextField priceField;
    @FXML private ComboBox<OrderStatus> statusBox;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Client> clientBox;
    @FXML private ComboBox<Designer> designerBox;

    @FXML private TextField searchField;
    @FXML private ComboBox<OrderStatus> filterStatusBox;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private ComboBox<String> sortBox;

    @FXML private TableView<DesignOrder> table;
    @FXML private TableColumn<DesignOrder, Integer> idColumn;
    @FXML private TableColumn<DesignOrder, String> titleColumn;
    @FXML private TableColumn<DesignOrder, String> descColumn;
    @FXML private TableColumn<DesignOrder, BigDecimal> priceColumn;
    @FXML private TableColumn<DesignOrder, OrderStatus> statusColumn;
    @FXML private TableColumn<DesignOrder, LocalDate> dateColumn;
    @FXML private TableColumn<DesignOrder, String> clientColumn;
    @FXML private TableColumn<DesignOrder, String> designerColumn;

    private final DesignOrderService orderService = new DesignOrderService();
    private final ClientService clientService = new ClientService();
    private final DesignerService designerService = new DesignerService();

    private final ObservableList<DesignOrder> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        designerColumn.setCellValueFactory(new PropertyValueFactory<>("designerName"));

        statusColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(OrderStatus item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getLabel());
            }
        });

        table.setItems(data);

        statusBox.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        filterStatusBox.setItems(FXCollections.observableArrayList(OrderStatus.values()));

        sortBox.setItems(FXCollections.observableArrayList(
                "Цена (по возрастанию)",
                "Цена (по убыванию)",
                "Дата (по возрастанию)",
                "Дата (по убыванию)"
        ));

        loadCombos();

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null) {
                titleField.setText(val.getTitle());
                descField.setText(val.getDescription());
                priceField.setText(val.getPrice().toString());
                statusBox.setValue(val.getStatus());
                datePicker.setValue(val.getCreatedDate());
                clientBox.getItems().stream()
                        .filter(c -> c.getId() == val.getClientId())
                        .findFirst().ifPresent(clientBox::setValue);
                designerBox.getItems().stream()
                        .filter(d -> d.getId() == val.getDesignerId())
                        .findFirst().ifPresent(designerBox::setValue);
            }
        });

        searchField.textProperty().addListener((obs, old, val) -> applyFilters());

        refresh();
    }

    private void loadCombos() {
        clientBox.setItems(FXCollections.observableArrayList(clientService.getAll()));
        designerBox.setItems(FXCollections.observableArrayList(designerService.getAll()));
    }

    @FXML
    private void onAdd() {
        try {
            DesignOrder order = new DesignOrder(
                    titleField.getText(),
                    descField.getText(),
                    parsePrice(priceField.getText()),
                    statusBox.getValue(),
                    datePicker.getValue(),
                    clientBox.getValue() == null ? 0 : clientBox.getValue().getId(),
                    designerBox.getValue() == null ? 0 : designerBox.getValue().getId()
            );
            orderService.create(order);
            refresh();
            clearForm();
            info("Заказ добавлен");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        DesignOrder selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("Выберите заказ");
            return;
        }
        try {
            selected.setTitle(titleField.getText());
            selected.setDescription(descField.getText());
            selected.setPrice(parsePrice(priceField.getText()));
            selected.setStatus(statusBox.getValue());
            selected.setCreatedDate(datePicker.getValue());
            selected.setClientId(clientBox.getValue() == null ? 0 : clientBox.getValue().getId());
            selected.setDesignerId(designerBox.getValue() == null ? 0 : designerBox.getValue().getId());
            orderService.update(selected);
            refresh();
            clearForm();
            info("Заказ обновлён");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        DesignOrder selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("Выберите заказ");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить заказ \"" + selected.getTitle() + "\"?",
                ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                orderService.delete(selected.getId());
                refresh();
                clearForm();
                info("Заказ удалён");
            } catch (Exception e) {
                error(e.getMessage());
            }
        }
    }

    @FXML
    private void onRefresh() {
        searchField.clear();
        filterStatusBox.setValue(null);
        minPriceField.clear();
        maxPriceField.clear();
        sortBox.setValue(null);
        refresh();
        clearForm();
    }

    @FXML
    private void onClear() {
        clearForm();
        table.getSelectionModel().clearSelection();
    }

    @FXML
    private void onApplyFilters() {
        applyFilters();
    }

    @FXML
    private void onExport() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Сохранить заказы");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel файл", "*.xlsx"));
        chooser.setInitialFileName("orders.xlsx");
        File file = chooser.showSaveDialog(table.getScene().getWindow());
        if (file != null) {
            try {
                ExcelExporter.exportOrders(data, file.getAbsolutePath());
                info("Данные экспортированы");
            } catch (Exception e) {
                error(e.getMessage());
            }
        }
    }

    private void refresh() {
        data.setAll(orderService.getAll());
    }

    private void applyFilters() {
        List<DesignOrder> result = orderService.getAll();

        String search = searchField.getText();
        if (search != null && !search.isBlank()) {
            String lower = search.toLowerCase();
            result = result.stream()
                    .filter(o -> o.getTitle().toLowerCase().contains(lower)
                            || o.getDescription().toLowerCase().contains(lower))
                    .toList();
        }

        OrderStatus status = filterStatusBox.getValue();
        if (status != null) {
            result = result.stream().filter(o -> o.getStatus() == status).toList();
        }

        String minText = minPriceField.getText();
        String maxText = maxPriceField.getText();
        if (minText != null && !minText.isBlank() && maxText != null && !maxText.isBlank()) {
            try {
                BigDecimal min = new BigDecimal(minText);
                BigDecimal max = new BigDecimal(maxText);
                result = result.stream()
                        .filter(o -> o.getPrice().compareTo(min) >= 0 && o.getPrice().compareTo(max) <= 0)
                        .toList();
            } catch (NumberFormatException ignored) {
            }
        }

        String sort = sortBox.getValue();
        if (sort != null) {
            switch (sort) {
                case "Цена (по возрастанию)" ->
                        result = result.stream().sorted((a, b) -> a.getPrice().compareTo(b.getPrice())).toList();
                case "Цена (по убыванию)" ->
                        result = result.stream().sorted((a, b) -> b.getPrice().compareTo(a.getPrice())).toList();
                case "Дата (по возрастанию)" ->
                        result = result.stream().sorted((a, b) -> a.getCreatedDate().compareTo(b.getCreatedDate())).toList();
                case "Дата (по убыванию)" ->
                        result = result.stream().sorted((a, b) -> b.getCreatedDate().compareTo(a.getCreatedDate())).toList();
            }
        }

        data.setAll(result);
    }

    private BigDecimal parsePrice(String text) {
        if (text == null || text.isBlank()) {
            throw new RuntimeException("Введите цену");
        }
        try {
            return new BigDecimal(text.trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Некорректная цена");
        }
    }

    private void clearForm() {
        titleField.clear();
        descField.clear();
        priceField.clear();
        statusBox.setValue(null);
        datePicker.setValue(null);
        clientBox.setValue(null);
        designerBox.setValue(null);
    }

    private void info(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private void error(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}