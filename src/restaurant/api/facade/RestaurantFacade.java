package restaurant.api.facade;

import restaurant.command.command.*;
import restaurant.command.handler.CommandBus;
import restaurant.query.dto.*;
import restaurant.query.service.OrderQueryService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RestaurantFacade {
    private final CommandBus commandBus;
    private final OrderQueryService queryService;

    public RestaurantFacade(CommandBus commandBus, OrderQueryService queryService) {
        this.commandBus = commandBus;
        this.queryService = queryService;
    }

    public UUID createOrder(String customerName) {
        UUID orderId = UUID.randomUUID();
        commandBus.send(new CreateOrderCommand(orderId, customerName));
        return orderId;
    }

    public void addDishToOrder(UUID orderId, String dishName, int quantity, double price) {
        commandBus.send(new AddDishCommand(orderId, dishName, quantity, price));
    }

    public void modifyOrderItem(UUID orderId, String dishName, int newQuantity) {
        commandBus.send(new ModifyOrderItemCommand(orderId, dishName, newQuantity));
    }

    public void updateOrderStatus(UUID orderId, String status) {
        commandBus.send(new UpdateOrderStatusCommand(orderId, status));
    }

    public void completeOrder(UUID orderId) {
        commandBus.send(new CompleteOrderCommand(orderId));
    }

    public OrderDTO getOrder(UUID orderId) {
        return queryService.getOrderById(orderId);
    }

    public List<OrderDTO> getAllOrders() {
        return queryService.getAllOrders();
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        return queryService.getOrdersByStatus(status);
    }

    public OrderStatisticsDTO getStatistics(LocalDate date) {
        return queryService.getStatistics(date);
    }

    public List<OrderHistoryDTO> getOrderHistory(UUID orderId) {
        return queryService.getOrderHistory(orderId);
    }

    public OrderReportDTO getDailyReport(LocalDate date) {
        return queryService.generateDailyReport(date);
    }
}
