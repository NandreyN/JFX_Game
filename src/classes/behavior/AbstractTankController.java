package classes.behavior;

import javafx.event.Event;
import javafx.event.EventHandler;

/**
 * Layer abstract class , additionally extends EventHandler to allow handling
 * game events
 */
public abstract class AbstractTankController implements ITankManager, EventHandler<Event> {
}
