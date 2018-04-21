package classes.behavior;

import classes.gameObjects.GameTank;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.reactfx.util.FxTimer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private Timeline actionTimeline;
    private TankController tankController;
    private static final int TIMER_DELAY = 50;
    private static final int MOVE_DURATION = 20;

    Activity(TankController controller) {
        this.tankController = controller;
        move(generateMoveTickTimes(), KeyCode.W);
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
     * Includes logic for emulating rotations for tanks , controlled by computer
     *
     * @param toAngle new angle to rotate to
     */
    private void rotate(double toAngle) {
        if (actionTimeline != null)
            actionTimeline.stop();

        if (Math.abs(tankController.getDirectionAngle() - toAngle) <= 5) {
            rotate(generateNewDirection(90));
            return;
        }

        boolean left = shouldRotateLeft(tankController.getDirectionAngle(), toAngle);
        KeyCode code = (left) ? KeyCode.A : KeyCode.D;

        actionTimeline = new Timeline(new KeyFrame(Duration.millis(TIMER_DELAY), event -> {
            double tankAngle = tankController.getDirectionAngle();
            boolean canRotateLeft = (left) ? tankController.canRotateLeft : tankController.canRotateRight;
            if (!canRotateLeft) {
                actionTimeline.stop();
                move(generateMoveTickTimes(), KeyCode.W);
            }
            tankController.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                    "", code, false, false, false, false));

            if (Math.abs(tankAngle - toAngle) <= 5) {
                actionTimeline.stop();
                move(generateMoveTickTimes(), KeyCode.W);
            }
        }));
        actionTimeline.setCycleCount(Timeline.INDEFINITE);
        actionTimeline.play();
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

        if (actionTimeline != null)
            actionTimeline.stop();

        actionTimeline = new Timeline(new KeyFrame(Duration.millis(TIMER_DELAY), event -> {
            tankController.handle(new KeyEvent(KeyEvent.KEY_PRESSED, "",
                    "", keyCode, false, false, false, false));
        }));

        actionTimeline.setOnFinished((event -> {
            if (keyCode == KeyCode.S) {
                rotate(generateNewDirection(90));
                return;
            }
            if (tankController.canMoveForward) {
                move(generateMoveTickTimes(), KeyCode.W);
                return;
            }
            move(generateMoveTickTimes(), KeyCode.S);
        }));

        actionTimeline.setCycleCount(n);
        actionTimeline.play();
        System.out.println("Started moving");
    }

    private boolean shouldRotateLeft(double current, double target) {
        return target < current;
    }

    @Override
    public void dispose() {
        if (actionTimeline != null)
            actionTimeline.stop();
    }
}

/**
 * Class responsible for managing enemy tanks of current level
 * Includes timers which are dedicated to checking whether tank is alive
 * or not and triggering fire event to hit player`s tank
 */
public class EnemyTankManager implements MediaDisposer.Disposable, EventHandler<Event> {
    private List<TankController> enemyTanks;
    private List<Timeline> stateUpdateTimers;
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

            stateUpdateTimers.add(new Timeline(new KeyFrame(Duration.millis(TIMER_DELAY), event -> {
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
            })));
        }
        for (int i = 0; i < enemyTanks.size(); i++) {
            stateUpdateTimers.get(i).setCycleCount(Timeline.INDEFINITE);
            stateUpdateTimers.get(i).play();
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
            if (stateUpdateTimers.get(i) != null)
                stateUpdateTimers.get(i).stop();
        }
        activities.forEach(Activity::dispose);
        enemyTanks.forEach(TankController::dispose);
        player.dispose();
    }
}
