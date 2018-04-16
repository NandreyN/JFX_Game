package classes.behavior;

import classes.gameObjects.GameTank;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class EnemyTankManager implements AutoCloseable, EventHandler<Event> {
    private List<TankController> enemyTanks;
    private List<Timer> stateUpdateTimers;
    private List<Boolean> areAlive;
    private IntegerProperty tanksAliveCount;
    private List<Timer> moveTimers;

    private static final int TIMER_DELAY = 50;

    private ViewMotionManager motionManager;
    private TankController player, currentActive;

    public EnemyTankManager(AnchorPane pane, List<GameTank> tanks) {
        motionManager = ViewMotionManager.getInstance();
        enemyTanks = new ArrayList<>();
        areAlive = new ArrayList<>();
        moveTimers = new ArrayList<>();
        for (GameTank t : tanks) {
            enemyTanks.add(new TankController(pane, t, 90));
            areAlive.add(true);
            moveTimers.add(new Timer(TIMER_DELAY, (e) -> {
            }));
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
                    Platform.runLater(() -> stateUpdateTimers.get(finalI).stop());
                    moveTimers.get(finalI).stop();
                    tanksAliveCount.set(tanksAliveCount.get() - 1);
                    return;
                }
                Event.fireEvent(enemyTanks.get(finalI), new javafx.scene.input.MouseEvent(
                        javafx.scene.input.MouseEvent.MOUSE_MOVED, player.tankModel.getLeftUpper().getX(),
                        player.tankModel.getLeftUpper().getY(), 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                        true, true, true, true, true, true, null
                ));
                if (enemyTanks.get(finalI).tankModel.getTank().getGun().isReady())
                    Event.fireEvent(enemyTanks.get(finalI), new javafx.scene.input.MouseEvent(
                            MouseEvent.MOUSE_CLICKED, player.tankModel.getLeftUpper().getX(),
                            player.tankModel.getLeftUpper().getY(), 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                            true, true, true, true, true, true, null
                    ));
            }));
        }
        for (int i = 0; i < enemyTanks.size(); i++) {
            stateUpdateTimers.get(i).start();
            move(i, (int) (Math.random() * 100));
        }
    }

    private void rotate(int i, double toAngle) {
        moveTimers.get(i).stop();

        TankController controller = enemyTanks.get(i);

        if (Math.abs(controller.getDirectionAngle() - toAngle) <= 5) {
            move(i, (int) (Math.random() * 100));
            return;
        }

        boolean left = controller.canRotateLeft;
        KeyCode code = (left) ? KeyCode.A : KeyCode.D;

        moveTimers.set(i, new Timer(TIMER_DELAY, (e) -> {
            double tankAngle = controller.getDirectionAngle();
            controller.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                    "", code, false, false, false, false));

            if (Math.abs(tankAngle - toAngle) <= 5) {
                moveTimers.get(i).stop();
                move(i, (int) (Math.random() * 100));
            }

        }));

        moveTimers.get(i).start();
    }

    private void move(int i, int n) {
        moveTimers.get(i).stop();

        TankController controller = enemyTanks.get(i);
        moveTimers.set(i, new Timer(TIMER_DELAY, new ActionListener() {
            private int counter;

            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                if (counter == n) {
                    moveTimers.get(i).stop();
                    if (controller.canMoveForward)
                        move(i, (int) (Math.random() * 100));
                    else
                        rotate(i, Math.random() * 40);
                }
                KeyCode code = (controller.canMoveForward) ? KeyCode.W : KeyCode.S;
                if (code == KeyCode.S) {
                    for (int i = 0; i < 5; i++)
                        controller.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                                "", code, false, false, false, false));
                    moveTimers.get(i).stop();
                    rotate(i, Math.random() * 40);
                } else
                    controller.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                            "", code, false, false, false, false));
            }
        }));
        moveTimers.get(i).start();
    }

    @Override
    public void close() {
        if (stateUpdateTimers == null)
            return;
        for (int i = 0; i < enemyTanks.size(); i++) {
            if (stateUpdateTimers.get(i) != null && stateUpdateTimers.get(i).isRunning())
                stateUpdateTimers.get(i).stop();
            if (moveTimers.get(i).isRunning())
                moveTimers.get(i).stop();
        }
    }

    public int getTanksAliveCount() {
        return tanksAliveCount.get();
    }

    public IntegerProperty tanksAliveCountProperty() {
        return tanksAliveCount;
    }

    private boolean shouldRotateLeft(double current, double target) {
        if (current - target < 90) {
            return true;
        } else {
            return false;
        }
    }
}
