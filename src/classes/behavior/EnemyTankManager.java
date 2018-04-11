package classes.behavior;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class EnemyTankManager implements AutoCloseable, EventHandler<Event> {
    private List<TankController> enemyTanks;
    private List<Timer> stateUpdateTimers;

    private static final int ENEMY_COUNT = 1, TIMER_DELAY = 30;

    private ViewMotionManager motionManager;
    private TankController player, currentActive;

    public EnemyTankManager(AnchorPane pane) {
        motionManager = ViewMotionManager.getInstance();

        enemyTanks = new ArrayList<>();
        for (int i = 0; i < ENEMY_COUNT; i++)
            enemyTanks.add(initialize(pane, new Point2D(300, 300), 90));

        currentActive = enemyTanks.get(0);
    }

    private TankController initialize(AnchorPane pane, Point2D startPos, double orientAngle) {
        return new TankController(pane, startPos, orientAngle, 2);
    }

    @Override
    public void handle(Event event) {
        currentActive.handle(event);
    }

    public void startTrackingPlayersTank(TankController tankManager) {
        stateUpdateTimers = new ArrayList<>();
        this.player = tankManager;
        for (int i = 0; i < ENEMY_COUNT; i++) {
            int finalI = i;
            stateUpdateTimers.add(new Timer(TIMER_DELAY, e -> {
                Event.fireEvent(enemyTanks.get(finalI), new javafx.scene.input.MouseEvent(
                        javafx.scene.input.MouseEvent.MOUSE_MOVED, player.tankInstance.getLeftUpper().getX(),
                        player.tankInstance.getLeftUpper().getY(), 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
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
