package repository;

import exception.EntityNotFoundException;
import model.Designer;
import util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DesignerRepository {

    public void save(Designer designer) {
        String sql = "INSERT INTO designers (full_name, specialization, email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, designer.getFullName());
            statement.setString(2, designer.getSpecialization());
            statement.setString(3, designer.getEmail());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    designer.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения дизайнера: " + e.getMessage(), e);
        }
    }

    public List<Designer> findAll() {
        String sql = "SELECT * FROM designers ORDER BY id";
        List<Designer> result = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения дизайнеров: " + e.getMessage(), e);
        }
        return result;
    }

    public Designer findById(int id) {
        String sql = "SELECT * FROM designers WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска дизайнера: " + e.getMessage(), e);
        }
        throw new EntityNotFoundException("Дизайнер с ID " + id + " не найден");
    }

    public void update(Designer designer) {
        String sql = "UPDATE designers SET full_name = ?, specialization = ?, email = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, designer.getFullName());
            statement.setString(2, designer.getSpecialization());
            statement.setString(3, designer.getEmail());
            statement.setInt(4, designer.getId());
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Дизайнер с ID " + designer.getId() + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления дизайнера: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM designers WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Дизайнер с ID " + id + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления дизайнера: " + e.getMessage(), e);
        }
    }

    private Designer mapRow(ResultSet rs) throws SQLException {
        return new Designer(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("specialization"),
                rs.getString("email")
        );
    }
}