package restaurant.command.command;

import java.util.UUID;

public class ModifyOrderItemCommand implements Command {
    private final UUID orderId;
    private final String dishName;
    private final int newQuantity;

    public ModifyOrderItemCommand(UUID orderId, String dishName, int newQuantity) {
        this.orderId = orderId;
        this.dishName = dishName;
        this.newQuantity = newQuantity;
    }

    public UUID getOrderId() { return orderId; }
    public String getDishName() { return dishName; }
    public int getNewQuantity() { return newQuantity; }
}