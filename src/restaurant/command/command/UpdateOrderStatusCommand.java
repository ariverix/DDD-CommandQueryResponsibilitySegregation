package restaurant.command.command;

import java.util.UUID;

public class UpdateOrderStatusCommand implements Command {
    private final UUID orderId;
    private final String newStatus;

    public UpdateOrderStatusCommand(UUID orderId, String newStatus) {
        this.orderId = orderId;
        this.newStatus = newStatus;
    }

    public UUID getOrderId() { return orderId; }
    public String getNewStatus() { return newStatus; }
}