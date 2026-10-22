package com.designstudio.controller;

import com.designstudio.service.DesignOrderService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Map;

public class StatisticsController {

    @FXML private GridPane grid;

    private final DesignOrderService service = new DesignOrderService();

    @FXML
    public void initialize() {
        refresh();
    }

    @FXML
    private void onRefresh() {
        refresh();
    }

    private void refresh() {
        grid.getChildren().clear();
        Map<String, String> stats = service.getStatistics();

        int row = 0;
        for (Map.Entry<String, String> entry : stats.entrySet()) {
            Label name = new Label(entry.getKey());
            name.getStyleClass().add("stat-name");
            Label value = new Label(entry.getValue());
            value.getStyleClass().add("stat-value");
            grid.add(name, 0, row);
            grid.add(value, 1, row);
            row++;
        }
    }
}