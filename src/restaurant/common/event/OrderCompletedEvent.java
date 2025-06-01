package restaurant.common.event;

import java.util.UUID;

public class OrderCompletedEvent extends Event {
    private final UUID orderId;
    private final double totalAmount;

    public OrderCompletedEvent(UUID orderId, double totalAmount) {
        super();
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }

    public UUID getOrderId() { return orderId; }
    public double getTotalAmount() { return totalAmount; }
}