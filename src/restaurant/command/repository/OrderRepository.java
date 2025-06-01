package restaurant.command.repository;


import restaurant.command.model.CustomerOrder;
import restaurant.common.exception.OrderNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderRepository {
    private final Map<UUID, CustomerOrder> orders = new HashMap<>();

    public void save(CustomerOrder order) {
        orders.put(order.getOrderId(), order);
    }

    public CustomerOrder findById(UUID orderId) {
        CustomerOrder order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }

    public boolean exists(UUID orderId) {
        return orders.containsKey(orderId);
    }
}