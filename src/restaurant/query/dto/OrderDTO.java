package restaurant.query.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderDTO {
    private final UUID orderId;
    private final String customerName;
    private final List<OrderItemDTO> items;
    private final String status;
    private final double totalAmount;
    private final LocalDateTime createdAt;

    public OrderDTO(UUID orderId, String customerName, List<OrderItemDTO> items,
                    String status, double totalAmount, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = items;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public List<OrderItemDTO> getItems() { return items; }
    public String getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static class OrderItemDTO {
        private final String dishName;
        private final int quantity;
        private final double price;
        private final double totalPrice;

        public OrderItemDTO(String dishName, int quantity, double price, double totalPrice) {
            this.dishName = dishName;
            this.quantity = quantity;
            this.price = price;
            this.totalPrice = totalPrice;
        }

        public String getDishName() { return dishName; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public double getTotalPrice() { return totalPrice; }
    }
}