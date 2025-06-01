package restaurant.common.event;

import java.util.UUID;

public class DishAddedEvent extends Event {
    private final UUID orderId;
    private final String dishName;
    private final int quantity;
    private final double price;

    public DishAddedEvent(UUID orderId, String dishName, int quantity, double price) {
        super();
        this.orderId = orderId;
        this.dishName = dishName;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getOrderId() { return orderId; }
    public String getDishName() { return dishName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}