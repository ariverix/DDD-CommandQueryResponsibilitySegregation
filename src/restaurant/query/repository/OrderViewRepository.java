package restaurant.query.repository;

import restaurant.query.model.OrderView;
import java.util.*;
import java.util.stream.Collectors;

public class OrderViewRepository {
    private final Map<UUID, OrderView> orders = new HashMap<>();

    public void save(OrderView order) {
        orders.put(order.getOrderId(), order);
    }

    public OrderView findById(UUID orderId) {
        return orders.get(orderId);
    }

    public List<OrderView> findAll() {
        return new ArrayList<>(orders.values());
    }

    public List<OrderView> findByStatus(String status) {
        return orders.values().stream()
                .filter(order -> order.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public void delete(UUID orderId) {
        orders.remove(orderId);
    }

    public boolean exists(UUID orderId) {
        return orders.containsKey(orderId);
    }
}