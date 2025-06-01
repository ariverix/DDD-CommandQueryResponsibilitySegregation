
import restaurant.api.console.RestaurantConsoleInterface;
import restaurant.api.facade.RestaurantFacade;
import restaurant.command.command.*;
import restaurant.command.handler.*;
import restaurant.command.repository.OrderRepository;
import restaurant.common.event.EventBus;

import restaurant.query.repository.OrderHistoryRepository;
import restaurant.query.repository.OrderStatisticsRepository;
import restaurant.query.repository.OrderViewRepository;
import restaurant.query.service.OrderQueryService;
import restaurant.query.service.QueryEventHandler;

public class RestaurantApplication {
    public static void main(String[] args) {
        EventBus eventBus = EventBus.getInstance();
        OrderRepository orderRepository = new OrderRepository();
        OrderViewRepository orderViewRepository = new OrderViewRepository();
        OrderStatisticsRepository statisticsRepository = new OrderStatisticsRepository();
        OrderHistoryRepository historyRepository = new OrderHistoryRepository();


        CommandBus commandBus = new CommandBus();
        commandBus.registerHandler(CreateOrderCommand.class,
                new CreateOrderCommandHandler(orderRepository, eventBus));
        commandBus.registerHandler(AddDishCommand.class,
                new AddDishCommandHandler(orderRepository, eventBus));
        commandBus.registerHandler(ModifyOrderItemCommand.class,
                new ModifyOrderItemCommandHandler(orderRepository, eventBus));
        commandBus.registerHandler(UpdateOrderStatusCommand.class,
                new UpdateOrderStatusCommandHandler(orderRepository, eventBus));
        commandBus.registerHandler(CompleteOrderCommand.class,
                new CompleteOrderCommandHandler(orderRepository, eventBus));

        QueryEventHandler queryEventHandler = new QueryEventHandler(
                orderViewRepository, statisticsRepository, historyRepository);
        eventBus.subscribe(queryEventHandler);

        OrderQueryService queryService = new OrderQueryService(
                orderViewRepository, statisticsRepository, historyRepository);
        RestaurantFacade facade = new RestaurantFacade(commandBus, queryService);

        RestaurantConsoleInterface console = new RestaurantConsoleInterface(facade);
        console.start();
    }
}