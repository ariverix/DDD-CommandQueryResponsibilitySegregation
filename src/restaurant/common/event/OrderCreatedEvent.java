package restaurant.common.event;

import java.util.UUID;

public class OrderCreatedEvent extends Event {
    private final UUID orderId;
    private final String customerName;

    public OrderCreatedEvent(UUID orderId, String customerName) {
        super();
        this.orderId = orderId;
        this.customerName = customerName;
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
}
