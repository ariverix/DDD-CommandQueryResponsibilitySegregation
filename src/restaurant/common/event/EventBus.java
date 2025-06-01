package restaurant.common.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventBus {
    private static EventBus instance;
    private final List<Consumer<Event>> handlers = new ArrayList<>();

    private EventBus() {}

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public void publish(Event event) {
        handlers.forEach(handler -> {
            try {
                handler.accept(event);
            } catch (Exception e) {
                System.err.println("Не удалось обработать событие " + event.getClass().getSimpleName() +
                        ": " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void subscribe(Consumer<Event> handler) {
        handlers.add(handler);
    }
}