package classes.behavior;

import classes.gameObjects.GameTankInstance;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public class PlayerTankManager extends TankManager {
    private GameTankInstance tankInstance = null;
    private static final double DELTA_ANGLE = Math.PI / 16;

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
        KeyCode code = event.getCode();
        switch (code) {
            case W:
                break;
            case A:
                tankInstance.turnLeft(DELTA_ANGLE);
                break;
            case S:
                break;
            case D:
                tankInstance.turnRight(DELTA_ANGLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void handleMouseClickEvent(javafx.scene.input.MouseEvent event) {
        if (event.isPrimaryButtonDown())
            tankInstance.fire();

    }

    @Override
    public void handleMouseMotionEvent(javafx.scene.input.MouseEvent event) {
        double sceneX = event.getSceneX(), sceneY = event.getSceneY();
        // rotate tanks turret
    }
}
