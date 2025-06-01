package restaurant.command.handler;

import restaurant.command.command.CompleteOrderCommand;
import restaurant.command.model.CustomerOrder;
import restaurant.command.repository.OrderRepository;
import restaurant.common.event.EventBus;
import restaurant.common.event.OrderCompletedEvent;
import restaurant.common.event.OrderStatusChangedEvent;

public class CompleteOrderCommandHandler implements CommandHandler<CompleteOrderCommand> {
    private final OrderRepository repository;
    private final EventBus eventBus;

    public CompleteOrderCommandHandler(OrderRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    public void handle(CompleteOrderCommand command) {
        CustomerOrder order = repository.findById(command.getOrderId());
        if (order == null) {
            throw new IllegalArgumentException("Заказ не найден с идентификатором: " + command.getOrderId());
        }

        if (!order.canBeCompleted()) {
            throw new IllegalStateException("Заказ не может быть выполнен из текущего состояния: " + order.getStatus());
        }

        String oldStatus = order.getStatus().toString();
        order.complete();
        repository.save(order);

        eventBus.publish(new OrderStatusChangedEvent(command.getOrderId(), oldStatus, order.getStatus().toString()));

        eventBus.publish(new OrderCompletedEvent(command.getOrderId(), order.getTotalAmount()));
    }
}