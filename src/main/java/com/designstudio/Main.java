package com.designstudio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.designstudio.util.DatabaseInitializer;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            DatabaseInitializer.initialize();
        } catch (Exception e) {
            System.err.println("Ошибка подключения к БД: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setTitle("Дизайн-студия");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}