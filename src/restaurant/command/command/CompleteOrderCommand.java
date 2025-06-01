package restaurant.command.command;

import java.util.UUID;

public class CompleteOrderCommand implements Command {
    private final UUID orderId;

    public CompleteOrderCommand(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() { return orderId; }
}