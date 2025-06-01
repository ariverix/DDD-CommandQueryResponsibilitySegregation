package restaurant.command.handler;


import restaurant.command.command.UpdateOrderStatusCommand;
import restaurant.command.model.CustomerOrder;
import restaurant.command.model.OrderStatus;
import restaurant.command.repository.OrderRepository;
import restaurant.common.event.EventBus;
import restaurant.common.event.OrderStatusChangedEvent;
import restaurant.common.exception.InvalidOrderStateException;

public class UpdateOrderStatusCommandHandler implements CommandHandler<UpdateOrderStatusCommand> {
    private final OrderRepository repository;
    private final EventBus eventBus;

    public UpdateOrderStatusCommandHandler(OrderRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    public void handle(UpdateOrderStatusCommand command) {
        CustomerOrder order = repository.findById(command.getOrderId());
        if (order == null) {
            throw new IllegalArgumentException("Заказ не найден с идентификатором: " + command.getOrderId());
        }

        String oldStatus = order.getStatus().toString();

        try {
            order.updateStatus(OrderStatus.valueOf(command.getNewStatus()));
            repository.save(order);

            eventBus.publish(new OrderStatusChangedEvent(
                    command.getOrderId(), oldStatus, command.getNewStatus()
            ));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный статус:" + command.getNewStatus(), e);
        } catch (InvalidOrderStateException e) {
            throw e;
        }
    }
}