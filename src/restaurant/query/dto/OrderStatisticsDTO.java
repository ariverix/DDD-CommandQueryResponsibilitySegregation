package restaurant.query.dto;

import java.time.LocalDate;

public class OrderStatisticsDTO {
    private final LocalDate date;
    private final int totalOrders;
    private final double totalRevenue;
    private final int completedOrders;
    private final int cancelledOrders;

    public OrderStatisticsDTO(LocalDate date, int totalOrders, double totalRevenue,
                              int completedOrders, int cancelledOrders) {
        this.date = date;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.completedOrders = completedOrders;
        this.cancelledOrders = cancelledOrders;
    }

    public LocalDate getDate() { return date; }
    public int getTotalOrders() { return totalOrders; }
    public double getTotalRevenue() { return totalRevenue; }
    public int getCompletedOrders() { return completedOrders; }
    public int getCancelledOrders() { return cancelledOrders; }
}