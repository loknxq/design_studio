package repository;

import exception.EntityNotFoundException;
import model.Client;
import util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {

    public void save(Client client) {
        String sql = "INSERT INTO clients (full_name, email, phone) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, client.getFullName());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getPhone());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    client.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения клиента: " + e.getMessage(), e);
        }
    }

    public List<Client> findAll() {
        String sql = "SELECT * FROM clients ORDER BY id";
        List<Client> result = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения клиентов: " + e.getMessage(), e);
        }
        return result;
    }

    public Client findById(int id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска клиента: " + e.getMessage(), e);
        }
        throw new EntityNotFoundException("Клиент с ID " + id + " не найден");
    }

    public void update(Client client) {
        String sql = "UPDATE clients SET full_name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getFullName());
            statement.setString(2, client.getEmail());
            statement.setString(3, client.getPhone());
            statement.setInt(4, client.getId());
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Клиент с ID " + client.getId() + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления клиента: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Клиент с ID " + id + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления клиента: " + e.getMessage(), e);
        }
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }
}