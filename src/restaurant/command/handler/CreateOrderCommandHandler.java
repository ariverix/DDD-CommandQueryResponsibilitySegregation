package restaurant.command.handler;


import restaurant.command.command.CreateOrderCommand;
import restaurant.command.model.CustomerOrder;
import restaurant.command.repository.OrderRepository;
import restaurant.common.event.EventBus;
import restaurant.common.event.OrderCreatedEvent;

public class CreateOrderCommandHandler implements CommandHandler<CreateOrderCommand> {
    private final OrderRepository repository;
    private final EventBus eventBus;

    public CreateOrderCommandHandler(OrderRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    public void handle(CreateOrderCommand command) {
        CustomerOrder order = new CustomerOrder(command.getOrderId(), command.getCustomerName());
        repository.save(order);
        eventBus.publish(new OrderCreatedEvent(command.getOrderId(), command.getCustomerName()));
    }
}