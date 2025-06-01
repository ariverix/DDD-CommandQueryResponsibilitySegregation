package restaurant.common.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(UUID orderId) {
        super("Заказ не найден: " + orderId);
    }
}
