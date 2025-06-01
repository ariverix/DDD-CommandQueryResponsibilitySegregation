package restaurant.command.handler;


import restaurant.command.command.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandBus {
    private final Map<Class<?>, CommandHandler> handlers = new HashMap<>();

    public void registerHandler(Class<? extends Command> commandType, CommandHandler handler) {
        handlers.put(commandType, handler);
    }

    @SuppressWarnings("unchecked")
    public void send(Command command) {
        CommandHandler handler = handlers.get(command.getClass());
        if (handler != null) {
            handler.handle(command);
        } else {
            throw new IllegalArgumentException("Для команды не найден обработчик: " + command.getClass());
        }
    }
}