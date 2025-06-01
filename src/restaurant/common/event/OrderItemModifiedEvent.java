package restaurant.common.event;

import java.util.UUID;

public class OrderItemModifiedEvent extends Event {
    private final UUID orderId;
    private final String dishName;
    private final int oldQuantity;
    private final int newQuantity;

    public OrderItemModifiedEvent(UUID orderId, String dishName, int oldQuantity, int newQuantity) {
        super();
        this.orderId = orderId;
        this.dishName = dishName;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
    }

    public UUID getOrderId() { return orderId; }
    public String getDishName() { return dishName; }
    public int getOldQuantity() { return oldQuantity; }
    public int getNewQuantity() { return newQuantity; }
}