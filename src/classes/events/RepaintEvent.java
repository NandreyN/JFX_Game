package classes.events;

import javafx.event.Event;
import javafx.event.EventType;

public class RepaintEvent extends GameEvent {
    public RepaintEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
