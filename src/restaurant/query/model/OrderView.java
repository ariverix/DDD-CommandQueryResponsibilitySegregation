package restaurant.query.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderView {
    private final UUID orderId;
    private final String customerName;
    private final List<OrderItemView> items;
    private String status;
    private final LocalDateTime createdAt;

    public OrderView(UUID orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.status = "CREATED";
        this.createdAt = LocalDateTime.now();
    }

    public void addItem(String dishName, int quantity, double price) {
        items.add(new OrderItemView(dishName, quantity, price));
    }

    public void modifyItem(String dishName, int newQuantity) {
        if (newQuantity <= 0) {
            items.removeIf(item -> item.getDishName().equals(dishName));
        } else {
            items.stream()
                    .filter(item -> item.getDishName().equals(dishName))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(newQuantity));
        }
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(OrderItemView::getTotalPrice).sum();
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public List<OrderItemView> getItems() { return new ArrayList<>(items); }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static class OrderItemView {
        private final String dishName;
        private int quantity;
        private final double price;

        public OrderItemView(String dishName, int quantity, double price) {
            this.dishName = dishName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getDishName() { return dishName; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public double getTotalPrice() { return quantity * price; }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}