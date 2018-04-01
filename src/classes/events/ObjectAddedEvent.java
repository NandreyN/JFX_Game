package classes.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ObjectAddedEvent extends GameEvent {
    public ObjectAddedEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
