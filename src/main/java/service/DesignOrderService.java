package service;

import exception.BusinessException;
import model.DesignOrder;
import model.OrderStatus;
import repository.ClientRepository;
import repository.DesignOrderRepository;
import repository.DesignerRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DesignOrderService {
    private final DesignOrderRepository orderRepository = new DesignOrderRepository();
    private final ClientRepository clientRepository = new ClientRepository();
    private final DesignerRepository designerRepository = new DesignerRepository();

    public void create(DesignOrder order) {
        validate(order);
        orderRepository.save(order);
    }

    public List<DesignOrder> getAll() {
        return orderRepository.findAll();
    }

    public DesignOrder getById(int id) {
        return orderRepository.findById(id);
    }

    public void update(DesignOrder order) {
        validate(order);
        DesignOrder old = orderRepository.findById(order.getId());
        if (old.getStatus() == OrderStatus.DONE && order.getStatus() != OrderStatus.DONE) {
            throw new BusinessException("Нельзя изменить статус завершённого заказа");
        }
        orderRepository.update(order);
    }

    public void delete(int id) {
        orderRepository.delete(id);
    }

    public List<DesignOrder> searchByTitle(String text) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getTitle().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<DesignOrder> searchByDescription(String text) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<DesignOrder> filterByStatus(OrderStatus status) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<DesignOrder> filterByPriceRange(BigDecimal min, BigDecimal max) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getPrice().compareTo(min) >= 0 && o.getPrice().compareTo(max) <= 0)
                .collect(Collectors.toList());
    }

    public List<DesignOrder> sortByPrice() {
        return orderRepository.findAll().stream()
                .sorted(Comparator.comparing(DesignOrder::getPrice))
                .collect(Collectors.toList());
    }

    public List<DesignOrder> sortByDate() {
        return orderRepository.findAll().stream()
                .sorted(Comparator.comparing(DesignOrder::getCreatedDate))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getStatistics() {
        List<DesignOrder> all = orderRepository.findAll();
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("Всего клиентов", (long) clientRepository.findAll().size());
        stats.put("Всего дизайнеров", (long) designerRepository.findAll().size());
        stats.put("Всего заказов", (long) all.size());
        stats.put("Новых заказов", all.stream().filter(o -> o.getStatus() == OrderStatus.NEW).count());
        stats.put("В работе", all.stream().filter(o -> o.getStatus() == OrderStatus.IN_PROGRESS).count());
        stats.put("Завершённых", all.stream().filter(o -> o.getStatus() == OrderStatus.DONE).count());
        stats.put("Отменённых", all.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count());
        return stats;
    }

    private void validate(DesignOrder order) {
        if (order.getTitle() == null || order.getTitle().isBlank()) {
            throw new BusinessException("Название заказа не может быть пустым");
        }
        if (order.getDescription() == null || order.getDescription().isBlank()) {
            throw new BusinessException("Описание заказа не может быть пустым");
        }
        if (order.getPrice() == null || order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Цена заказа должна быть больше нуля");
        }
        if (order.getStatus() == null) {
            throw new BusinessException("Статус заказа обязателен");
        }
        if (order.getCreatedDate() == null) {
            throw new BusinessException("Дата создания заказа обязательна");
        }
        clientRepository.findById(order.getClientId());
        designerRepository.findById(order.getDesignerId());
    }
}