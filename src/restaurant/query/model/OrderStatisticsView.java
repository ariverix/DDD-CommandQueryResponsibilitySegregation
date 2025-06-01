package restaurant.query.model;

import java.time.LocalDate;

public class OrderStatisticsView {
    private LocalDate date;
    private int totalOrders;
    private double totalRevenue;
    private int completedOrders;
    private int cancelledOrders;

    public OrderStatisticsView(LocalDate date) {
        this.date = date;
        this.totalOrders = 0;
        this.totalRevenue = 0.0;
        this.completedOrders = 0;
        this.cancelledOrders = 0;
    }

    public void incrementTotalOrders() {
        this.totalOrders++;
    }

    public void addRevenue(double amount) {
        this.totalRevenue += amount;
    }

    public void incrementCompletedOrders() {
        this.completedOrders++;
    }

    public void incrementCancelledOrders() {
        this.cancelledOrders++;
    }

    public LocalDate getDate() { return date; }
    public int getTotalOrders() { return totalOrders; }
    public double getTotalRevenue() { return totalRevenue; }
    public int getCompletedOrders() { return completedOrders; }
    public int getCancelledOrders() { return cancelledOrders; }
}