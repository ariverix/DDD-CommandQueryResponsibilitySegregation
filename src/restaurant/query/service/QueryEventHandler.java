package restaurant.query.service;

import restaurant.common.event.*;
import restaurant.query.model.OrderHistoryView;
import restaurant.query.model.OrderStatisticsView;
import restaurant.query.model.OrderView;
import restaurant.query.repository.OrderHistoryRepository;
import restaurant.query.repository.OrderStatisticsRepository;
import restaurant.query.repository.OrderViewRepository;

import java.time.LocalDate;
import java.util.function.Consumer;

public class QueryEventHandler implements Consumer<Event> {
    private final OrderViewRepository orderViewRepository;
    private final OrderStatisticsRepository statisticsRepository;
    private final OrderHistoryRepository historyRepository;

    public QueryEventHandler(OrderViewRepository orderViewRepository,
                             OrderStatisticsRepository statisticsRepository,
                             OrderHistoryRepository historyRepository) {
        this.orderViewRepository = orderViewRepository;
        this.statisticsRepository = statisticsRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public void accept(Event event) {
        if (event instanceof OrderCreatedEvent) {
            handleOrderCreated((OrderCreatedEvent) event);
        } else if (event instanceof DishAddedEvent) {
            handleDishAdded((DishAddedEvent) event);
        } else if (event instanceof OrderItemModifiedEvent) {
            handleOrderItemModified((OrderItemModifiedEvent) event);
        } else if (event instanceof OrderStatusChangedEvent) {
            handleOrderStatusChanged((OrderStatusChangedEvent) event);
        } else if (event instanceof OrderCompletedEvent) {
            handleOrderCompleted((OrderCompletedEvent) event);
        }
    }

    private void handleOrderCreated(OrderCreatedEvent event) {
        OrderView order = new OrderView(event.getOrderId(), event.getCustomerName());
        orderViewRepository.save(order);

        OrderHistoryView history = new OrderHistoryView(
                event.getOrderId(), event.getCustomerName(),
                "ORDER_CREATED", "Заказ создан"
        );
        historyRepository.save(history);

        LocalDate today = event.getOccurredAt().toLocalDate();
        OrderStatisticsView stats = statisticsRepository.findByDate(today);
        if (stats == null) {
            stats = new OrderStatisticsView(today);
        }
        stats.incrementTotalOrders();
        statisticsRepository.save(stats);
    }

    private void handleDishAdded(DishAddedEvent event) {
        OrderView order = orderViewRepository.findById(event.getOrderId());
        if (order != null) {
            order.addItem(event.getDishName(), event.getQuantity(), event.getPrice());
            orderViewRepository.save(order);

            OrderHistoryView history = new OrderHistoryView(
                    event.getOrderId(), order.getCustomerName(),
                    "DISH_ADDED", String.format("Добавлено блюдо: %s x%d",
                    event.getDishName(), event.getQuantity())
            );
            historyRepository.save(history);
        }
    }

    private void handleOrderItemModified(OrderItemModifiedEvent event) {
        OrderView order = orderViewRepository.findById(event.getOrderId());
        if (order != null) {
            try {
                order.modifyItem(event.getDishName(), event.getNewQuantity());
                orderViewRepository.save(order);

                String action = event.getNewQuantity() == 0 ? "DISH_REMOVED" : "DISH_MODIFIED";
                String details = event.getNewQuantity() == 0 ?
                        String.format("Удалено блюдо: %s", event.getDishName()) :
                        String.format("Изменено количество %s: %d -> %d",
                                event.getDishName(), event.getOldQuantity(), event.getNewQuantity());

                OrderHistoryView history = new OrderHistoryView(
                        event.getOrderId(), order.getCustomerName(), action, details
                );
                historyRepository.save(history);
            } catch (Exception e) {
                System.err.println("Ошибка при изменении позиции заказа: " + e.getMessage());
            }
        }
    }

    private void handleOrderStatusChanged(OrderStatusChangedEvent event) {
        OrderView order = orderViewRepository.findById(event.getOrderId());
        if (order != null) {
            order.updateStatus(event.getNewStatus());
            orderViewRepository.save(order);

            OrderHistoryView history = new OrderHistoryView(
                    event.getOrderId(), order.getCustomerName(),
                    "STATUS_CHANGED", String.format("Статус изменен: %s -> %s",
                    event.getOldStatus(), event.getNewStatus())
            );
            historyRepository.save(history);

            if ("CANCELLED".equals(event.getNewStatus())) {
                LocalDate today = LocalDate.now();
                OrderStatisticsView stats = statisticsRepository.findByDate(today);
                if (stats == null) {
                    stats = new OrderStatisticsView(today);
                }
                stats.incrementCancelledOrders();
                statisticsRepository.save(stats);
            }
        }
    }

    private void handleOrderCompleted(OrderCompletedEvent event) {
        OrderView order = orderViewRepository.findById(event.getOrderId());
        if (order != null) {
            OrderHistoryView history = new OrderHistoryView(
                    event.getOrderId(), order.getCustomerName(),
                    "ORDER_COMPLETED", String.format("Заказ завершен. Сумма: %.2f", event.getTotalAmount())
            );
            historyRepository.save(history);
        }

        LocalDate today = event.getOccurredAt().toLocalDate();
        OrderStatisticsView stats = statisticsRepository.findByDate(today);
        if (stats == null) {
            stats = new OrderStatisticsView(today);
        }
        stats.incrementCompletedOrders();
        stats.addRevenue(event.getTotalAmount());
        statisticsRepository.save(stats);
    }
}