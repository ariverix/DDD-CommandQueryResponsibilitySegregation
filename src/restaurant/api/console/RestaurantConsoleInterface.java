package restaurant.api.console;

import restaurant.api.facade.RestaurantFacade;
import restaurant.query.dto.*;
import java.time.LocalDate;
import java.util.*;

public class RestaurantConsoleInterface {
    private final RestaurantFacade facade;
    private final Scanner scanner;

    public RestaurantConsoleInterface(RestaurantFacade facade) {
        this.facade = facade;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== СИСТЕМА УПРАВЛЕНИЯ ЗАКАЗАМИ ===");

        while (true) {
            showMenu();
            int choice = getIntInput("Выберите действие: ");

            try {
                switch (choice) {
                    case 1: createOrder(); break;
                    case 2: addDishToOrder(); break;
                    case 3: modifyOrderItem(); break;
                    case 4: updateOrderStatus(); break;
                    case 5: completeOrder(); break;
                    case 6: viewOrder(); break;
                    case 7: viewAllOrders(); break;
                    case 8: viewStatistics(); break;
                    case 9: viewDailyReport(); break;
                    case 0:
                        System.out.println("До свидания!");
                        return;
                    default:
                        System.out.println("Неверный выбор!");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            System.out.print("\nНажмите Enter...");
            scanner.nextLine();
        }
    }

    private void showMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Создать заказ");
        System.out.println("2. Добавить блюдо");
        System.out.println("3. Изменить количество");
        System.out.println("4. Обновить статус");
        System.out.println("5. Завершить заказ");
        System.out.println("6. Просмотр заказа");
        System.out.println("7. Все заказы");
        System.out.println("8. Статистика");
        System.out.println("9. Ежедневный отчет");
        System.out.println("0. Выход");
    }

    private void createOrder() {
        String customerName = getStringInput("Имя клиента: ");
        if (customerName.trim().isEmpty()) {
            System.out.println("Имя не может быть пустым!");
            return;
        }

        UUID orderId = facade.createOrder(customerName.trim());
        System.out.println("Заказ создан! ID: " + orderId);
    }

    private void addDishToOrder() {
        UUID orderId = getOrderId();
        if (orderId == null) return;

        String dishName = getStringInput("Название блюда: ");
        int quantity = getIntInput("Количество: ");
        double price = getDoubleInput("Цена: ");

        if (dishName.trim().isEmpty() || quantity <= 0 || price <= 0) {
            System.out.println("Некорректные данные!");
            return;
        }

        facade.addDishToOrder(orderId, dishName.trim(), quantity, price);
        System.out.println("Блюдо добавлено!");
    }

    private void modifyOrderItem() {
        UUID orderId = getOrderId();
        if (orderId == null) return;

        OrderDTO order = facade.getOrder(orderId);
        if (order == null) {
            System.out.println("Заказ не найден!");
            return;
        }

        if (order.getItems().isEmpty()) {
            System.out.println("В заказе нет блюд!");
            return;
        }

        System.out.println("\nТекущий состав заказа:");
        int index = 1;
        for (OrderDTO.OrderItemDTO item : order.getItems()) {
            System.out.printf("%d. %s x%d\n", index++, item.getDishName(), item.getQuantity());
        }

        String dishName = getStringInput("Название блюда для изменения: ");
        int newQuantity = getIntInput("Новое количество (0 - удалить): ");

        if (dishName.trim().isEmpty() || newQuantity < 0) {
            System.out.println("Некорректные данные!");
            return;
        }

        boolean dishExists = order.getItems().stream()
                .anyMatch(item -> item.getDishName().equalsIgnoreCase(dishName.trim()));

        if (!dishExists) {
            System.out.println("Блюдо '" + dishName.trim() + "' не найдено в заказе!");
            System.out.println("Доступные блюда:");
            for (OrderDTO.OrderItemDTO item : order.getItems()) {
                System.out.println("- " + item.getDishName());
            }
            return;
        }

        facade.modifyOrderItem(orderId, dishName.trim(), newQuantity);
        System.out.println(newQuantity == 0 ? "Блюдо удалено!" : "Количество изменено!");
    }

    private void updateOrderStatus() {
        UUID orderId = getOrderId();
        if (orderId == null) return;

        OrderDTO order = facade.getOrder(orderId);
        if (order != null) {
            System.out.println("Текущий статус: " + order.getStatus());
        }

        System.out.println("Доступные статусы:");
        System.out.println("CREATED - Создан");
        System.out.println("CONFIRMED - Подтвержден");
        System.out.println("PREPARING - Готовится");
        System.out.println("READY - Готов");
        System.out.println("COMPLETED - Завершен");
        System.out.println("CANCELLED - Отменен");

        String status = getStringInput("Новый статус: ").toUpperCase();

        String[] validStatuses = {"CREATED", "CONFIRMED", "PREPARING", "READY", "COMPLETED", "CANCELLED"};
        if (!Arrays.asList(validStatuses).contains(status)) {
            System.out.println("Неверный статус!");
            return;
        }

        facade.updateOrderStatus(orderId, status);
        System.out.println("Статус обновлен!");
    }

    private void completeOrder() {
        UUID orderId = getOrderId();
        if (orderId == null) return;

        OrderDTO order = facade.getOrder(orderId);
        if (order != null) {
            System.out.println("Текущий статус заказа: " + order.getStatus());
            if (!"READY".equals(order.getStatus())) {
                System.out.println("Внимание: заказ должен быть в статусе READY для завершения!");
                String confirm = getStringInput("Продолжить? (y/n): ");
                if (!"y".equalsIgnoreCase(confirm) && !"yes".equalsIgnoreCase(confirm)) {
                    return;
                }
            }
        }

        facade.completeOrder(orderId);
        System.out.println("Заказ завершен!");
    }

    private void viewOrder() {
        UUID orderId = getOrderId();
        if (orderId == null) return;

        OrderDTO order = facade.getOrder(orderId);
        if (order != null) {
            printOrder(order);
        } else {
            System.out.println("Заказ не найден");
        }
    }

    private void viewAllOrders() {
        List<OrderDTO> orders = facade.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Заказов нет");
        } else {
            System.out.println("\n=== ВСЕ ЗАКАЗЫ ===");
            System.out.println("Всего заказов: " + orders.size());
            System.out.println("ID (короткий) | Клиент | Статус | Сумма");
            System.out.println("--------------------------------------------------");
            orders.forEach(this::printOrderSummary);
        }
    }

    private void viewStatistics() {
        LocalDate date = getDateInput("Дата (ГГГГ-ММ-ДД, Enter - сегодня): ");
        if (date == null) {
            date = LocalDate.now();
        }

        OrderStatisticsDTO stats = facade.getStatistics(date);
        System.out.println("\n=== СТАТИСТИКА ===");
        System.out.println("Дата: " + date);
        System.out.println("Всего заказов: " + stats.getTotalOrders());
        System.out.println("Завершенных: " + stats.getCompletedOrders());
        System.out.println("Отмененных: " + stats.getCancelledOrders());
        System.out.println("Выручка: " + String.format("%.2f", stats.getTotalRevenue()));

        if (stats.getTotalOrders() > 0) {
            double completionRate = (double) stats.getCompletedOrders() / stats.getTotalOrders() * 100;
            System.out.println("Процент завершенных: " + String.format("%.1f%%", completionRate));
        }
    }

    private void viewDailyReport() {
        LocalDate date = getDateInput("Дата (ГГГГ-ММ-ДД, Enter - сегодня): ");
        if (date == null) {
            date = LocalDate.now();
        }

        OrderReportDTO report = facade.getDailyReport(date);
        System.out.println("\n=== ЕЖЕДНЕВНЫЙ ОТЧЕТ ===");
        System.out.println("Дата: " + report.getReportDate());
        System.out.println("Всего заказов: " + report.getTotalOrdersToday());
        System.out.println("Средняя стоимость заказа: " + String.format("%.2f", report.getAverageOrderValue()));

        System.out.println("\nПопулярные блюда:");
        if (report.getPopularDishes().isEmpty()) {
            System.out.println("Нет данных о блюдах");
        } else {
            for (OrderReportDTO.PopularDishDTO dish : report.getPopularDishes()) {
                System.out.printf("- %s: %d порций, выручка %.2f\n",
                        dish.getDishName(), dish.getTotalOrdered(), dish.getTotalRevenue());
            }
        }

        System.out.println("\nТоп клиенты:");
        if (report.getTopCustomers().isEmpty()) {
            System.out.println("Нет данных о клиентах");
        } else {
            for (OrderReportDTO.CustomerOrderSummaryDTO customer : report.getTopCustomers()) {
                System.out.printf("- %s: %d заказов, потрачено %.2f\n",
                        customer.getCustomerName(), customer.getTotalOrders(), customer.getTotalSpent());
            }
        }
    }

    private void printOrder(OrderDTO order) {
        System.out.println("\n=== ЗАКАЗ ===");
        System.out.println("ID: " + order.getOrderId());
        System.out.println("Клиент: " + order.getCustomerName());
        System.out.println("Статус: " + order.getStatus());
        System.out.println("Сумма: " + String.format("%.2f", order.getTotalAmount()));

        if (order.getItems().isEmpty()) {
            System.out.println("Состав: заказ пустой");
        } else {
            System.out.println("Состав:");
            for (OrderDTO.OrderItemDTO item : order.getItems()) {
                System.out.printf("  %s x%d = %.2f (цена за единицу: %.2f)\n",
                        item.getDishName(), item.getQuantity(), item.getTotalPrice(),
                        item.getQuantity() > 0 ? item.getTotalPrice() / item.getQuantity() : 0);
            }
        }
    }

    private void printOrderSummary(OrderDTO order) {
        System.out.printf("%s | %-15s | %-10s | %8.2f\n",
                order.getOrderId().toString().substring(0, 8),
                order.getCustomerName().length() > 15 ?
                        order.getCustomerName().substring(0, 12) + "..." : order.getCustomerName(),
                order.getStatus(),
                order.getTotalAmount());
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Введите число!");
                    continue;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число!");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Введите число!");
                    continue;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число!");
            }
        }
    }

    private UUID getOrderId() {
        try {
            String orderIdStr = getStringInput("ID заказа: ").trim();
            if (orderIdStr.isEmpty()) {
                System.out.println("ID не может быть пустым!");
                return null;
            }
            return UUID.fromString(orderIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Некорректный формат ID! Используйте полный UUID.");
            return null;
        }
    }

    private LocalDate getDateInput(String prompt) {
        try {
            String dateStr = getStringInput(prompt).trim();
            if (dateStr.isEmpty()) {
                return null;
            }
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            System.out.println("Некорректная дата! Используйте формат ГГГГ-ММ-ДД");
            return null;
        }
    }
}