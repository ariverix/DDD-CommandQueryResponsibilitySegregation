package restaurant.query.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderHistoryDTO {
    private final UUID orderId;
    private final String customerName;
    private final String action;
    private final String details;
    private final LocalDateTime timestamp;

    public OrderHistoryDTO(UUID orderId, String customerName, String action,
                           String details, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }
}