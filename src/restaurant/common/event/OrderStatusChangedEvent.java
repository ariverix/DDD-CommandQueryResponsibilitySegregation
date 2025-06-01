package restaurant.common.event;

import java.util.UUID;

public class OrderStatusChangedEvent extends Event {
    private final UUID orderId;
    private final String oldStatus;
    private final String newStatus;

    public OrderStatusChangedEvent(UUID orderId, String oldStatus, String newStatus) {
        super();
        this.orderId = orderId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }
}