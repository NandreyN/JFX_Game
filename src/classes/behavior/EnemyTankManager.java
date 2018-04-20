package classes.behavior;

import classes.gameObjects.GameTank;
import com.sun.media.jfxmediaimpl.MediaDisposer;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Describes kind of Action performed by GameTank
 * Controlled by EnemyTankManager
 * Includes two timers: action,inactivity
 * action timer should be killed when new action is triggered
 */
class Activity implements MediaDisposer.Disposable {
    private Timer inactivityTimer, actionTimer;
    private TankController tankController;
    private static final int TIMER_DELAY = 50;
    private static final int MOVE_DURATION = 20;

    Activity(TankController controller) {
        this.tankController = controller;
        startTrackingInactivity();
    }

    /**
     * Generates new direction in range [a-delta, a+delta]
     *
     * @param delta delta angle from current direction
     * @return new direction angle
     */
    private double generateNewDirection(double delta) {
        Random random = new Random();
        return tankController.getDirectionAngle() + random.nextDouble() * (random.nextBoolean() ? -1 : 1) * delta;
    }

    /**
     * Generates number of triggering move events per one
     * move action
     *
     * @return number of events to be triggered
     */
    private int generateMoveTickTimes() {
        return (int) (Math.random() * MOVE_DURATION);
    }

    /**
     * Whenever tank becomes inactive ,
     * method generates some kind of Activity for it
     */
    private void startTrackingInactivity() {
        inactivityTimer = new Timer(TIMER_DELAY, (e) -> {
            if (actionTimer == null || !actionTimer.isRunning())
                move(generateMoveTickTimes(), KeyCode.W);
        });
        inactivityTimer.start();
    }

    /**
     * Includes logic for emulating rotations for tanks , controlled by computer
     *
     * @param toAngle new angle to rotate to
     */
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
            boolean canRotateLeft = (left) ? tankController.canRotateLeft : tankController.canRotateRight;
            if (!canRotateLeft)
                move(generateMoveTickTimes(), KeyCode.W);
            tankController.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                    "", code, false, false, false, false));

            if (Math.abs(tankAngle - toAngle) <= 5) {
                move(generateMoveTickTimes(), KeyCode.W);
            }

        });
        actionTimer.start();
        System.out.println("Rotation to angle " + toAngle + " started");
    }

    /**
     * Emulates changing location action. Tank goes forward or back
     * depending on situation
     *
     * @param n       number of moves
     * @param keyCode move direction(W-forward,S-backwards)
     */
    private void move(int n, KeyCode keyCode) {
        if (keyCode != KeyCode.W && keyCode != KeyCode.S)
            return;

        if (actionTimer != null)
            actionTimer.stop();

        actionTimer = new Timer(TIMER_DELAY, new ActionListener() {
            private int counter;

            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                if (counter >= n) {
                    if (keyCode == KeyCode.S) {
                        rotate(generateNewDirection(90));
                        return;
                    }
                    if (tankController.canMoveForward) {
                        move(generateMoveTickTimes(), KeyCode.W);
                        return;
                    }
                    move(generateMoveTickTimes(), KeyCode.S);
                    return;
                }
                tankController.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                        "", keyCode, false, false, false, false));
            }
        });
        actionTimer.start();
        System.out.println("Started moving");
    }

    private boolean shouldRotateLeft(double current, double target) {
        return target < current;
    }

    @Override
    public void dispose() {
        inactivityTimer.stop();
        actionTimer.stop();
    }
}

/**
 * Class responsible for managing enemy tanks of current level
 * Includes timers which are dedicated to checking whether tank is alive
 * or not and triggering fire event to hit player`s tank
 */
public class EnemyTankManager implements MediaDisposer.Disposable, EventHandler<Event> {
    private List<TankController> enemyTanks;
    private List<Timer> stateUpdateTimers;
    private List<Boolean> areAlive;
    private IntegerProperty tanksAliveCount;

    private static final int TIMER_DELAY = 50;

    private List<Activity> activities;

    private TankController player;

    public EnemyTankManager(AnchorPane pane, List<GameTank> tanks) {
        enemyTanks = new ArrayList<>();
        areAlive = new ArrayList<>();
        activities = new ArrayList<>();

        for (GameTank t : tanks) {
            enemyTanks.add(new TankController(pane, t));
            areAlive.add(true);
        }

        for (TankController enemyTank : enemyTanks) activities.add(new Activity(enemyTank));

        tanksAliveCount = new SimpleIntegerProperty(tanks.size());
        ResourceDisposer.getInstance().add(this);
    }

    @Override
    public void handle(Event event) {
    }

    /**
     * Initializes timers for checking enemy tanks` states
     *
     * @param tankManager Controller of player`s tank
     */
    public void startTrackingPlayersTank(TankController tankManager) {
        stateUpdateTimers = new ArrayList<>();
        this.player = tankManager;
        for (int i = 0; i < enemyTanks.size(); i++) {
            int finalI = i;
            enemyTanks.get(i).isAliveProperty().addListener((observable, oldValue, newValue) -> {
                if (!enemyTanks.get(finalI).tankModel.getTank().isAlive() || !areAlive.get(finalI)) {
                    areAlive.set(finalI, false);
                    tanksAliveCount.set(tanksAliveCount.get() - 1);
                    activities.get(finalI).dispose();
                }
            });
            stateUpdateTimers.add(new Timer(TIMER_DELAY, e -> {
                if (!areAlive.get(finalI))
                    stateUpdateTimers.get(finalI).stop();

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

    /**
     * Tanks count which are alive at the moment.
     * Allows to detect level finish
     *
     * @return number of alive tanks
     */
    public IntegerProperty tanksAliveCountProperty() {
        return tanksAliveCount;
    }

    @Override
    public void dispose() {
        if (stateUpdateTimers == null)
            return;
        for (int i = 0; i < enemyTanks.size(); i++) {
            if (stateUpdateTimers.get(i) != null && stateUpdateTimers.get(i).isRunning())
                stateUpdateTimers.get(i).stop();
        }
        activities.forEach(Activity::dispose);
        enemyTanks.forEach(TankController::dispose);
        player.dispose();
    }
}
