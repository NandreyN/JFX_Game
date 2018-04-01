package classes.events;

import javafx.event.Event;
import javafx.event.EventType;

public class GameEvent extends Event {
    public GameEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
