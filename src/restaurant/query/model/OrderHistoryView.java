package restaurant.query.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderHistoryView {
    private UUID historyId;
    private UUID orderId;
    private String customerName;
    private String action;
    private String details;
    private LocalDateTime timestamp;

    public OrderHistoryView(UUID orderId, String customerName, String action, String details) {
        this.historyId = UUID.randomUUID();
        this.orderId = orderId;
        this.customerName = customerName;
        this.action = action;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getHistoryId() { return historyId; }
    public UUID getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }
}