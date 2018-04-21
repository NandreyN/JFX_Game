package classes.behavior;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.beans.EventHandler;

public class EventGenerator {
    public static synchronized void fireMouseMovedEvent(EventTarget target, double x, double y) {
        Event.fireEvent(target, new javafx.scene.input.MouseEvent(
                javafx.scene.input.MouseEvent.MOUSE_MOVED, x,
                y, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null
        ));
    }

    public static synchronized void fireMouseClickEvent(EventTarget target, double x, double y) {
        Event.fireEvent(target, new javafx.scene.input.MouseEvent(
                MouseEvent.MOUSE_CLICKED, x,
                y, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                true, true, true, true, true, true, null
        ));
    }

    public static synchronized void fireKeyPressedEvent(EventTarget target, KeyCode keyPressed) {
        Event.fireEvent(target, new KeyEvent(KeyEvent.KEY_PRESSED, "",
                "", keyPressed, false, false, false, false));
    }
}
