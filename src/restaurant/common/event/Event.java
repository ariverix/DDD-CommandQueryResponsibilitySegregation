package restaurant.common.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Event {
    private final UUID eventId;
    private final LocalDateTime occurredAt;

    public Event() {
        this.eventId = UUID.randomUUID();
        this.occurredAt = LocalDateTime.now();
    }

    public UUID getEventId() { return eventId; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
}
