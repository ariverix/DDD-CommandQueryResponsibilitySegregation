package restaurant.command.model;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PREPARING,
    READY,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus newStatus) {
        switch (this) {
            case CREATED:
                return newStatus == CONFIRMED || newStatus == CANCELLED;
            case CONFIRMED:
                return newStatus == PREPARING || newStatus == CANCELLED;
            case PREPARING:
                return newStatus == READY || newStatus == CANCELLED;
            case READY:
                return newStatus == COMPLETED;
            case COMPLETED:
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }
}