package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection connection = DatabaseManager.getConnection()) {
            runScript(connection, "database.sql");
            if (isClientsEmpty(connection)) {
                runScript(connection, "data.sql");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации БД: " + e.getMessage(), e);
        }
    }

    private static boolean isClientsEmpty(Connection connection) {
        String sql = "SELECT COUNT(*) FROM clients";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка проверки данных: " + e.getMessage(), e);
        }
        return false;
    }

    private static void runScript(Connection connection, String fileName) {
        try {
            String script = readResource(fileName);
            try (Statement statement = connection.createStatement()) {
                statement.execute(script);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка выполнения скрипта " + fileName + ": " + e.getMessage(), e);
        }
    }

    private static String readResource(String fileName) throws Exception {
        try (InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("Файл не найден в resources: " + fileName);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }
}