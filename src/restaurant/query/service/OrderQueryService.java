package restaurant.query.service;

import restaurant.query.dto.*;
import restaurant.query.model.OrderView;
import restaurant.query.repository.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderQueryService {
    private final OrderViewRepository orderViewRepository;
    private final OrderStatisticsRepository statisticsRepository;
    private final OrderHistoryRepository historyRepository;

    public OrderQueryService(OrderViewRepository orderViewRepository,
                             OrderStatisticsRepository statisticsRepository,
                             OrderHistoryRepository historyRepository) {
        this.orderViewRepository = orderViewRepository;
        this.statisticsRepository = statisticsRepository;
        this.historyRepository = historyRepository;
    }

    public OrderDTO getOrderById(UUID orderId) {
        OrderView order = orderViewRepository.findById(orderId);
        if (order == null) return null;
        return convertToDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderViewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        return orderViewRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderStatisticsDTO getStatistics(LocalDate date) {
        var stats = statisticsRepository.findByDate(date);
        return new OrderStatisticsDTO(
                stats.getDate(),
                stats.getTotalOrders(),
                stats.getTotalRevenue(),
                stats.getCompletedOrders(),
                stats.getCancelledOrders()
        );
    }

    public List<OrderHistoryDTO> getOrderHistory(UUID orderId) {
        return historyRepository.findByOrderId(orderId).stream()
                .map(history -> new OrderHistoryDTO(
                        history.getOrderId(),
                        history.getCustomerName(),
                        history.getAction(),
                        history.getDetails(),
                        history.getTimestamp()
                ))
                .collect(Collectors.toList());
    }

    public OrderReportDTO generateDailyReport(LocalDate date) {
        List<OrderView> dailyOrders = orderViewRepository.findAll().stream()
                .filter(order -> order.getCreatedAt().toLocalDate().equals(date))
                .collect(Collectors.toList());

        Map<String, Integer> dishCounts = new HashMap<>();
        Map<String, Double> dishRevenue = new HashMap<>();

        for (OrderView order : dailyOrders) {
            for (OrderView.OrderItemView item : order.getItems()) {
                dishCounts.merge(item.getDishName(), item.getQuantity(), Integer::sum);
                dishRevenue.merge(item.getDishName(), item.getTotalPrice(), Double::sum);
            }
        }

        List<OrderReportDTO.PopularDishDTO> popularDishes = dishCounts.entrySet().stream()
                .map(entry -> new OrderReportDTO.PopularDishDTO(
                        entry.getKey(),
                        entry.getValue(),
                        dishRevenue.get(entry.getKey())
                ))
                .sorted((a, b) -> Integer.compare(b.getTotalOrdered(), a.getTotalOrdered()))
                .limit(5)
                .collect(Collectors.toList());


        Map<String, Integer> customerOrderCounts = new HashMap<>();
        Map<String, Double> customerSpending = new HashMap<>();

        for (OrderView order : dailyOrders) {
            customerOrderCounts.merge(order.getCustomerName(), 1, Integer::sum);
            customerSpending.merge(order.getCustomerName(), order.getTotalAmount(), Double::sum);
        }

        List<OrderReportDTO.CustomerOrderSummaryDTO> topCustomers = customerOrderCounts.entrySet().stream()
                .map(entry -> new OrderReportDTO.CustomerOrderSummaryDTO(
                        entry.getKey(),
                        entry.getValue(),
                        customerSpending.get(entry.getKey())
                ))
                .sorted((a, b) -> Double.compare(b.getTotalSpent(), a.getTotalSpent()))
                .limit(5)
                .collect(Collectors.toList());

        double averageOrderValue = dailyOrders.stream()
                .mapToDouble(OrderView::getTotalAmount)
                .average().orElse(0.0);

        return new OrderReportDTO(date, popularDishes, topCustomers,
                averageOrderValue, dailyOrders.size());
    }

    private OrderDTO convertToDTO(OrderView order) {
        List<OrderDTO.OrderItemDTO> items = order.getItems().stream()
                .map(item -> new OrderDTO.OrderItemDTO(
                        item.getDishName(), item.getQuantity(),
                        item.getPrice(), item.getTotalPrice()
                ))
                .collect(Collectors.toList());

        return new OrderDTO(order.getOrderId(), order.getCustomerName(),
                items, order.getStatus(), order.getTotalAmount(), order.getCreatedAt());
    }
}