package classes.behavior;

import classes.gameObjects.GameTank;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class EnemyTankManager implements AutoCloseable, EventHandler<Event> {
    private List<TankController> enemyTanks;
    private List<Timer> stateUpdateTimers;
    private List<Boolean> areAlive;
    private IntegerProperty tanksAliveCount;

    private static final int TIMER_DELAY = 30;

    private ViewMotionManager motionManager;
    private TankController player, currentActive;

    public EnemyTankManager(AnchorPane pane, List<GameTank> tanks) {
        motionManager = ViewMotionManager.getInstance();

        enemyTanks = new ArrayList<>();
        areAlive = new ArrayList<>();
        for (GameTank t : tanks) {
            enemyTanks.add(new TankController(pane, t, 90));
            areAlive.add(true);
        }
        currentActive = enemyTanks.get(0);
        tanksAliveCount = new SimpleIntegerProperty(tanks.size());
    }

    @Override
    public void handle(Event event) {
        currentActive.handle(event);
    }

    public void startTrackingPlayersTank(TankController tankManager) {
        stateUpdateTimers = new ArrayList<>();
        this.player = tankManager;
        for (int i = 0; i < enemyTanks.size(); i++) {
            int finalI = i;
            stateUpdateTimers.add(new Timer(TIMER_DELAY, e -> {
                if (!enemyTanks.get(finalI).tankModel.getTank().isAlive() || !areAlive.get(finalI)) {
                    areAlive.set(finalI, false);
                    Platform.runLater(() -> ((Timer) (e.getSource())).stop());
                    tanksAliveCount.set(tanksAliveCount.get() - 1);
                    return;
                }
                Event.fireEvent(enemyTanks.get(finalI), new javafx.scene.input.MouseEvent(
                        javafx.scene.input.MouseEvent.MOUSE_MOVED, player.tankModel.getLeftUpper().getX(),
                        player.tankModel.getLeftUpper().getY(), 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                        true, true, true, true, true, true, null
                ));

            }));
        }
        for (int i = 0; i < enemyTanks.size(); i++)
            stateUpdateTimers.get(i).start();
    }

    @Override
    public void close() {
        if (stateUpdateTimers == null)
            return;
        for (int i = 0; i < enemyTanks.size(); i++)
            if (stateUpdateTimers.get(i) != null && stateUpdateTimers.get(i).isRunning())
                stateUpdateTimers.get(i).stop();
    }

    public int getTanksAliveCount() {
        return tanksAliveCount.get();
    }

    public IntegerProperty tanksAliveCountProperty() {
        return tanksAliveCount;
    }
}
