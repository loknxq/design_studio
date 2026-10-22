package com.designstudio.service;

import com.designstudio.exception.BusinessException;
import com.designstudio.model.DesignOrder;
import com.designstudio.model.OrderStatus;
import com.designstudio.repository.ClientRepository;
import com.designstudio.repository.DesignOrderRepository;
import com.designstudio.repository.DesignerRepository;

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

    public List<DesignOrder> search(String text) {
        if (text == null || text.isBlank()) {
            return getAll();
        }
        String lower = text.toLowerCase();
        return orderRepository.findAll().stream()
                .filter(o -> o.getTitle().toLowerCase().contains(lower)
                        || o.getDescription().toLowerCase().contains(lower))
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

    public List<DesignOrder> sortByPrice(boolean ascending) {
        Comparator<DesignOrder> comparator = Comparator.comparing(DesignOrder::getPrice);
        if (!ascending) comparator = comparator.reversed();
        return orderRepository.findAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<DesignOrder> sortByDate(boolean ascending) {
        Comparator<DesignOrder> comparator = Comparator.comparing(DesignOrder::getCreatedDate);
        if (!ascending) comparator = comparator.reversed();
        return orderRepository.findAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public Map<String, String> getStatistics() {
        List<DesignOrder> all = orderRepository.findAll();
        Map<String, String> stats = new LinkedHashMap<>();

        stats.put("Всего клиентов", String.valueOf(clientRepository.findAll().size()));
        stats.put("Всего дизайнеров", String.valueOf(designerRepository.findAll().size()));
        stats.put("Всего заказов", String.valueOf(all.size()));

        stats.put("Новых заказов", String.valueOf(
                all.stream().filter(o -> o.getStatus() == OrderStatus.NEW).count()));
        stats.put("Заказов в работе", String.valueOf(
                all.stream().filter(o -> o.getStatus() == OrderStatus.IN_PROGRESS).count()));
        stats.put("Завершённых заказов", String.valueOf(
                all.stream().filter(o -> o.getStatus() == OrderStatus.DONE).count()));
        stats.put("Отменённых заказов", String.valueOf(
                all.stream().filter(o -> o.getStatus() == OrderStatus.CANCELLED).count()));
        BigDecimal total = all.stream()
                .map(DesignOrder::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("Общая сумма заказов", total.toString());

        BigDecimal avg = all.isEmpty()
                ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(all.size()), 2, java.math.RoundingMode.HALF_UP);
        stats.put("Средняя цена заказа", avg.toString());

        BigDecimal min = all.stream().map(DesignOrder::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal max = all.stream().map(DesignOrder::getPrice).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        stats.put("Минимальная цена", min.toString());
        stats.put("Максимальная цена", max.toString());

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