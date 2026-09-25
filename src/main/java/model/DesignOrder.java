package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DesignOrder {
    private int id;
    private String title;
    private String description;
    private BigDecimal price;
    private OrderStatus status;
    private LocalDate createdDate;
    private int clientId;
    private int designerId;

    public DesignOrder() {
    }

    public DesignOrder(String title, String description, BigDecimal price,
                       OrderStatus status, LocalDate createdDate,
                       int clientId, int designerId) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.createdDate = createdDate;
        this.clientId = clientId;
        this.designerId = designerId;
    }

    public DesignOrder(int id, String title, String description, BigDecimal price,
                       OrderStatus status, LocalDate createdDate,
                       int clientId, int designerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.status = status;
        this.createdDate = createdDate;
        this.clientId = clientId;
        this.designerId = designerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getDesignerId() {
        return designerId;
    }

    public void setDesignerId(int designerId) {
        this.designerId = designerId;
    }

    @Override
    public String toString() {
        return "DesignOrder{id=" + id + ", title='" + title + "', price=" + price +
                ", status=" + status + ", createdDate=" + createdDate +
                ", clientId=" + clientId + ", designerId=" + designerId + "}";
    }
}