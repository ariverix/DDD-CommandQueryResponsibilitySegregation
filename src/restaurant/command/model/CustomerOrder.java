package restaurant.command.model;

import restaurant.common.exception.InvalidOrderStateException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CustomerOrder {
    private final UUID orderId;
    private final String customerName;
    private final List<OrderItem> items;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private long version = 0;

    public CustomerOrder(UUID orderId, String customerName) {
        this.orderId = Objects.requireNonNull(orderId, "Идентификатор заказа не может быть null");
        this.customerName = Objects.requireNonNull(customerName, "Имя клиента не может быть null");
        if (customerName.isBlank()) {
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        }
        this.items = new ArrayList<>();
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void addItem(String dishName, int quantity, double price) {
        validateItemParameters(dishName, quantity, price);

        if (!canModifyItems()) {
            throw new InvalidOrderStateException(
                    String.format("Невозможно добавить товары в заказ в статусе: %s. Допустимые статусы: CREATED, CONFIRMED", status));
        }

        items.add(new OrderItem(dishName, quantity, price));
        updateModificationTime();
        incrementVersion();
    }

    public void modifyItem(String dishName, int newQuantity) {
        Objects.requireNonNull(dishName, "Имя блюда не может быть null");

        if (!canModifyItems()) {
            throw new InvalidOrderStateException(
                    String.format("Невозможно изменить элементы в заказе со статусом: %s. Разрешенные статусы: CREATED, CONFIRMED", status));
        }

        if (newQuantity <= 0) {
            items.removeIf(item -> item.getDishName().equals(dishName));
        } else {
            items.stream()
                    .filter(item -> item.getDishName().equals(dishName))
                    .findFirst()
                    .ifPresentOrElse(
                            item -> item.setQuantity(newQuantity),
                            () -> { throw new IllegalArgumentException("Блюдо не найдено: " + dishName); }
                    );
        }
        updateModificationTime();
        incrementVersion();
    }

    public int getItemQuantity(String dishName) {
        Objects.requireNonNull(dishName, "Имя блюда не может быть null");
        return items.stream()
                .filter(item -> item.getDishName().equals(dishName))
                .findFirst()
                .map(OrderItem::getQuantity)
                .orElse(0);
    }

    public boolean canBeCompleted() {
        return status == OrderStatus.READY;
    }

    public void updateStatus(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus, "Статус заказа не может быть равен null");

        if (!status.canTransitionTo(newStatus)) {
            throw new InvalidOrderStateException(
                    String.format("Невозможно изменить статус с %s to %s", status, newStatus));
        }

        this.status = newStatus;
        updateModificationTime();
        incrementVersion();
    }

    public void complete() {
        if (!canBeCompleted()) {
            throw new InvalidOrderStateException(
                    String.format("Невозможно выполнить заказ в статусе: %s. Заказ должен находиться в статусе READY", status));
        }
        this.status = OrderStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        updateModificationTime();
        incrementVersion();
    }

    public double getTotalAmount() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    public long getVersion() {
        return version;
    }

    private void incrementVersion() {
        this.version++;
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }

    private boolean canModifyItems() {
        return status == OrderStatus.CREATED || status == OrderStatus.CONFIRMED;
    }

    private void updateModificationTime() {
        this.updatedAt = LocalDateTime.now();
    }

    private void validateItemParameters(String dishName, int quantity, double price) {
        Objects.requireNonNull(dishName, "Имя блюда не может быть null");
        if (dishName.isBlank()) {
            throw new IllegalArgumentException("Имя блюда не может быть пустым");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Цена должна быть положительной");
        }
    }

    @Override
    public String toString() {
        return String.format("Order[id=%s, customer=%s, status=%s, items=%d, total=%.2f, version=%d]",
                orderId, customerName, status, items.size(), getTotalAmount(), version);
    }
}