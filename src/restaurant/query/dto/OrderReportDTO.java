package restaurant.query.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderReportDTO {
    private final LocalDate reportDate;
    private final List<PopularDishDTO> popularDishes;
    private final List<CustomerOrderSummaryDTO> topCustomers;
    private final double averageOrderValue;
    private final int totalOrdersToday;

    public OrderReportDTO(LocalDate reportDate, List<PopularDishDTO> popularDishes,
                          List<CustomerOrderSummaryDTO> topCustomers,
                          double averageOrderValue, int totalOrdersToday) {
        this.reportDate = reportDate;
        this.popularDishes = popularDishes;
        this.topCustomers = topCustomers;
        this.averageOrderValue = averageOrderValue;
        this.totalOrdersToday = totalOrdersToday;
    }

    public LocalDate getReportDate() { return reportDate; }
    public List<PopularDishDTO> getPopularDishes() { return popularDishes; }
    public List<CustomerOrderSummaryDTO> getTopCustomers() { return topCustomers; }
    public double getAverageOrderValue() { return averageOrderValue; }
    public int getTotalOrdersToday() { return totalOrdersToday; }

    public static class PopularDishDTO {
        private final String dishName;
        private final int totalOrdered;
        private final double totalRevenue;

        public PopularDishDTO(String dishName, int totalOrdered, double totalRevenue) {
            this.dishName = dishName;
            this.totalOrdered = totalOrdered;
            this.totalRevenue = totalRevenue;
        }

        public String getDishName() { return dishName; }
        public int getTotalOrdered() { return totalOrdered; }
        public double getTotalRevenue() { return totalRevenue; }
    }

    public static class CustomerOrderSummaryDTO {
        private final String customerName;
        private final int totalOrders;
        private final double totalSpent;

        public CustomerOrderSummaryDTO(String customerName, int totalOrders, double totalSpent) {
            this.customerName = customerName;
            this.totalOrders = totalOrders;
            this.totalSpent = totalSpent;
        }

        public String getCustomerName() { return customerName; }
        public int getTotalOrders() { return totalOrders; }
        public double getTotalSpent() { return totalSpent; }
    }
}