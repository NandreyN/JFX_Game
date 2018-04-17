package classes.behavior;

import classes.gameObjects.GameTank;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Activity implements Closeable {
    private Timer inactivityTimer, actionTimer;
    private TankController tankController;
    private static final int TIMER_DELAY = 50;

    Activity(TankController controller) {
        this.tankController = controller;
        startTrackingInactivity();
    }

    private double generateNewDirection(double delta) {
        Random random = new Random();
        return tankController.getDirectionAngle() + random.nextDouble() * (random.nextBoolean() ? -1 : 1) * delta;
    }

    private void startTrackingInactivity() {
        inactivityTimer = new Timer(TIMER_DELAY, (e) -> {
            if (actionTimer == null || !actionTimer.isRunning())
                move((int) (Math.random() * 100));
        });
        inactivityTimer.start();
    }

    private void rotate(double toAngle) {
        if (actionTimer != null)
            actionTimer.stop();

        if (Math.abs(tankController.getDirectionAngle() - toAngle) <= 5) {
            rotate(generateNewDirection(90));
            return;
        }

        boolean left = shouldRotateLeft(tankController.getDirectionAngle(), toAngle);
        KeyCode code = (left) ? KeyCode.A : KeyCode.D;

        actionTimer = new Timer(TIMER_DELAY, (e) -> {
            double tankAngle = tankController.getDirectionAngle();
            boolean canRotate = (left) ? tankController.canRotateLeft : tankController.canRotateRight;
            if (!canRotate)
                move((int) (Math.random() * 100));
            tankController.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                    "", code, false, false, false, false));

            if (Math.abs(tankAngle - toAngle) <= 5) {
                move((int) (Math.random() * 100));
            }

        });
        actionTimer.start();
        System.out.println("Rotation to angle " + toAngle + " started");
    }

    private void move(int n) {
        if (actionTimer != null)
            actionTimer.stop();

        actionTimer = new Timer(TIMER_DELAY, new ActionListener() {
            private int counter;

            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                if (counter == n) {
                    if (tankController.canMoveForward)
                        move((int) (Math.random() * 100));
                    else {
                        System.out.println("Can`t move forward , rotating");
                        rotate(generateNewDirection(90));
                    }
                }
                KeyCode code = (tankController.canMoveForward) ? KeyCode.W : KeyCode.S;
                if (code == KeyCode.S) {
                    for (int i = 0; i < 5; i++)
                        tankController.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                                "", code, false, false, false, false));
                    rotate(generateNewDirection(90));
                } else
                    tankController.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                            "", code, false, false, false, false));
            }
        });
        actionTimer.start();
        System.out.println("Started moving");
    }

    private boolean shouldRotateLeft(double current, double target) {
        return target < current;
    }

    @Override
    public void close() {
        inactivityTimer.stop();
        actionTimer.stop();
    }
}

public class EnemyTankManager implements AutoCloseable, EventHandler<Event> {
    private List<TankController> enemyTanks;
    private List<Timer> stateUpdateTimers;
    private List<Boolean> areAlive;
    private IntegerProperty tanksAliveCount;

    private static final int TIMER_DELAY = 50;

    private List<Activity> activities;

    private TankController player, currentActive;

    public EnemyTankManager(AnchorPane pane, List<GameTank> tanks) {
        enemyTanks = new ArrayList<>();
        areAlive = new ArrayList<>();
        activities = new ArrayList<>();

        for (GameTank t : tanks) {
            enemyTanks.add(new TankController(pane, t, 90));
            areAlive.add(true);
        }

        for (TankController enemyTank : enemyTanks) activities.add(new Activity(enemyTank));

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
                    tanksAliveCount.set(tanksAliveCount.get() - 1);
                    activities.get(finalI).close();
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
        }
    }


    @Override
    public void close() {
        if (stateUpdateTimers == null)
            return;
        for (int i = 0; i < enemyTanks.size(); i++) {
            if (stateUpdateTimers.get(i) != null && stateUpdateTimers.get(i).isRunning())
                stateUpdateTimers.get(i).stop();
        }
    }

    public int getTanksAliveCount() {
        return tanksAliveCount.get();
    }

    public IntegerProperty tanksAliveCountProperty() {
        return tanksAliveCount;
    }
}
