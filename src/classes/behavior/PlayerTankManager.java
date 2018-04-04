package classes.behavior;

import classes.gameObjects.GameTankInstance;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public class PlayerTankManager extends TankManager {
    private GameTankInstance tankInstance = null;

    public PlayerTankManager() {
        initDefaultTank();
    }

    private void initDefaultTank() {
        ITank tankModel = TankConstructor.createDrumTank();

        tankInstance = new GameTankInstance(tankModel, null,
                new Point(0, 0), 100, 50);
    }

    @Override
    public void handle(Event event) {
        EventType eventType = event.getEventType();

        if (eventType.equals(MouseEvent.MOUSE_CLICKED))
            handleMouseClickEvent((MouseEvent) event);
        else if (eventType.equals(MouseEvent.MOUSE_MOVED))
            handleMouseMotionEvent((MouseEvent) event);
        else if (eventType.equals(KeyEvent.ANY))
            handleKeyboardEvent((KeyEvent) event);
        else throw new IllegalArgumentException("event");
    }

    @Override
    public void handleKeyboardEvent(KeyEvent event) {

    }

    @Override
    public void handleMouseClickEvent(javafx.scene.input.MouseEvent event) {

    }

    @Override
    public void handleMouseMotionEvent(javafx.scene.input.MouseEvent event) {

    }
}
