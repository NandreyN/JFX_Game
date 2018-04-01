package classes.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ObjectDeletedEvent extends GameEvent {
    public ObjectDeletedEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
