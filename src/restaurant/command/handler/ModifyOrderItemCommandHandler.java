package restaurant.command.handler;

import restaurant.command.command.ModifyOrderItemCommand;
import restaurant.command.model.CustomerOrder;
import restaurant.command.repository.OrderRepository;
import restaurant.common.event.EventBus;
import restaurant.common.event.OrderItemModifiedEvent;

public class ModifyOrderItemCommandHandler implements CommandHandler<ModifyOrderItemCommand> {
    private final OrderRepository repository;
    private final EventBus eventBus;

    public ModifyOrderItemCommandHandler(OrderRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    public void handle(ModifyOrderItemCommand command) {
        CustomerOrder order = repository.findById(command.getOrderId());
        int oldQuantity = order.getItemQuantity(command.getDishName());
        order.modifyItem(command.getDishName(), command.getNewQuantity());
        repository.save(order);

        eventBus.publish(new OrderItemModifiedEvent(
                command.getOrderId(), command.getDishName(),
                oldQuantity, command.getNewQuantity()
        ));
    }
}