package restaurant.command.handler;

import restaurant.command.command.AddDishCommand;
import restaurant.command.model.CustomerOrder;
import restaurant.command.repository.OrderRepository;
import restaurant.common.event.DishAddedEvent;
import restaurant.common.event.EventBus;

public class AddDishCommandHandler implements CommandHandler<AddDishCommand> {
    private final OrderRepository repository;
    private final EventBus eventBus;

    public AddDishCommandHandler(OrderRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    public void handle(AddDishCommand command) {
        CustomerOrder order = repository.findById(command.getOrderId());
        order.addItem(command.getDishName(), command.getQuantity(), command.getPrice());
        repository.save(order);
        eventBus.publish(new DishAddedEvent(
                command.getOrderId(), command.getDishName(),
                command.getQuantity(), command.getPrice()
        ));
    }
}