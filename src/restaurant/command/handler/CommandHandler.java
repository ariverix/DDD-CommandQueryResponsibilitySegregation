package restaurant.command.handler;


import restaurant.command.command.Command;

public interface CommandHandler<T extends Command> {
    void handle(T command);
}
