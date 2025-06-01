package restaurant.query.repository;

import restaurant.query.model.OrderHistoryView;
import java.util.*;
import java.util.stream.Collectors;

public class OrderHistoryRepository {
    private final List<OrderHistoryView> history = new ArrayList<>();

    public void save(OrderHistoryView historyItem) {
        history.add(historyItem);
    }

    public List<OrderHistoryView> findByOrderId(UUID orderId) {
        return history.stream()
                .filter(item -> item.getOrderId().equals(orderId))
                .collect(Collectors.toList());
    }
}