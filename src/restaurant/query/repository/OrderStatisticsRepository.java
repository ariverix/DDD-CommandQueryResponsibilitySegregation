package restaurant.query.repository;

import restaurant.query.model.OrderStatisticsView;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class OrderStatisticsRepository {
    private final Map<LocalDate, OrderStatisticsView> statistics = new HashMap<>();

    public void save(OrderStatisticsView stats) {
        statistics.put(stats.getDate(), stats);
    }

    public OrderStatisticsView findByDate(LocalDate date) {
        return statistics.computeIfAbsent(date, OrderStatisticsView::new);
    }
}