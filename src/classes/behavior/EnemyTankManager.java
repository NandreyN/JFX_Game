package classes.behavior;

import classes.events.GameEvent;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EnemyTankManager extends TankManager implements AutoCloseable {
    private List<PlayerTankManager> enemyTanks;
    private List<Timer> stateUpdateTimers;

    private static final int ENEMY_COUNT = 1, TIMER_DELAY = 30;

    private ViewMotionManager motionManager;
    private PlayerTankManager player;

    public EnemyTankManager(AnchorPane pane) {
        motionManager = ViewMotionManager.getInstance();

        enemyTanks = new ArrayList<>();
        for (int i = 0; i < ENEMY_COUNT; i++)
            enemyTanks.add(initialize(pane, new Point2D(300, 300), 90));
    }

    private PlayerTankManager initialize(AnchorPane pane, Point2D startPos, double orientAngle) {
        return new PlayerTankManager(pane, startPos, orientAngle, 2);
    }

    @Override
    public void handle(Event event) {

    }

    @Override
    public void handleKeyboardEvent(javafx.scene.input.KeyEvent event) {

    }

    @Override
    public void handleMouseClickEvent(javafx.scene.input.MouseEvent event) {

    }

    @Override
    public void handleMouseMotionEvent(javafx.scene.input.MouseEvent event) {

    }

    public void startTrackingPlayersTank(PlayerTankManager tankManager) {
        stateUpdateTimers = new ArrayList<>();
        this.player = tankManager;
        for (int i = 0; i < ENEMY_COUNT; i++) {
            int finalI = i;
            stateUpdateTimers.add(new Timer(TIMER_DELAY, e -> {
                Event.fireEvent(enemyTanks.get(finalI), new javafx.scene.input.MouseEvent(
                        javafx.scene.input.MouseEvent.MOUSE_MOVED, player.tankInstance.getPaintCoordinates().getX(),
                        player.tankInstance.getPaintCoordinates().getY(), 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                        true, true, true, true, true, true, null
                ));

            }));
        }
        for (int i = 0; i < ENEMY_COUNT; i++)
            stateUpdateTimers.get(i).start();
    }

    @Override
    public void close() {
        if (stateUpdateTimers == null)
            return;
        for (int i = 0; i < ENEMY_COUNT; i++)
            if (stateUpdateTimers.get(i) != null && stateUpdateTimers.get(i).isRunning())
                stateUpdateTimers.get(i).stop();
    }
}
