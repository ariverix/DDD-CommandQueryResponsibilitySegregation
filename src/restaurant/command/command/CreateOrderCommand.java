package restaurant.command.command;

import java.util.UUID;

public class CreateOrderCommand implements Command {
    private final UUID orderId;
    private final String customerName;

    public CreateOrderCommand(UUID orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
}