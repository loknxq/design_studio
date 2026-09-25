package repository;

import exception.EntityNotFoundException;
import model.DesignOrder;
import model.OrderStatus;
import util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DesignOrderRepository {

    public void save(DesignOrder order) {
        String sql = "INSERT INTO design_orders (title, description, price, status, created_date, client_id, designer_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, order.getTitle());
            statement.setString(2, order.getDescription());
            statement.setBigDecimal(3, order.getPrice());
            statement.setString(4, order.getStatus().name());
            statement.setDate(5, Date.valueOf(order.getCreatedDate()));
            statement.setInt(6, order.getClientId());
            statement.setInt(7, order.getDesignerId());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения заказа: " + e.getMessage(), e);
        }
    }

    public List<DesignOrder> findAll() {
        String sql = "SELECT * FROM design_orders ORDER BY id";
        List<DesignOrder> result = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка чтения заказов: " + e.getMessage(), e);
        }
        return result;
    }

    public DesignOrder findById(int id) {
        String sql = "SELECT * FROM design_orders WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска заказа: " + e.getMessage(), e);
        }
        throw new EntityNotFoundException("Заказ с ID " + id + " не найден");
    }

    public void update(DesignOrder order) {
        String sql = "UPDATE design_orders SET title = ?, description = ?, price = ?, status = ?, " +
                "created_date = ?, client_id = ?, designer_id = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, order.getTitle());
            statement.setString(2, order.getDescription());
            statement.setBigDecimal(3, order.getPrice());
            statement.setString(4, order.getStatus().name());
            statement.setDate(5, Date.valueOf(order.getCreatedDate()));
            statement.setInt(6, order.getClientId());
            statement.setInt(7, order.getDesignerId());
            statement.setInt(8, order.getId());
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Заказ с ID " + order.getId() + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления заказа: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM design_orders WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new EntityNotFoundException("Заказ с ID " + id + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления заказа: " + e.getMessage(), e);
        }
    }

    private DesignOrder mapRow(ResultSet rs) throws SQLException {
        return new DesignOrder(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                OrderStatus.valueOf(rs.getString("status")),
                rs.getDate("created_date").toLocalDate(),
                rs.getInt("client_id"),
                rs.getInt("designer_id")
        );
    }
}