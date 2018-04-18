package classes.behavior;

import classes.gameObjects.GameConstants;
import classes.gameObjects.GameTank;
import classes.sound.SoundPlayer;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import classes.tanks.parts.SAUTurret;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.*;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import view.infoPanel.InfoPanel;

import javax.swing.*;

/**
 * Class in created for handling events dedicated to player`s tank
 * It is responsible for handling input events (mouse , keyboard)
 * and changing model and view state , ex:
 * Rotating turret inside model and reflecting model state to the view.
 * <p>
 * View component should be pushed to another ViewHandler class later.
 */

public class TankController extends AbstractTankController implements EventTarget, MediaDisposer.Disposable {
    // View of controller`s content
    ImageView chassisView, turretView;
    // Describes view rotation relatively to some point
    Rotate turretRotation, chassisRotation;
    private ViewMotionManager motionManager;
    MouseEvent previousMouseEvent;

    GameTank tankModel;
    static final double DELTA_ANGLE = Math.PI / 146;

    private AnchorPane uIParent;
    private Shape border;
    private SoundPlayer soundPlayer;

    boolean canMoveForward = true, canRotateLeft = true, canRotateRight = true;

    public TankController(AnchorPane parent, GameTank tank) {
        this.uIParent = parent;
        this.tankModel = tank;
        motionManager = ViewMotionManager.getInstance();
        soundPlayer = SoundPlayer.getInstance();
        setTankImageView();
        setAliveChecker();
        trackBorder();
    }

    /**
     * Displays red rectangle border for current tank. For debug purposes
     */
    public void trackBorder() {
        if (border != null && uIParent.getChildren().contains(border)) {
            Shape old = border;
            Platform.runLater(() -> {
                uIParent.getChildren().remove(old);
                border = ViewMotionManager.getGameObjectShape(tankModel);
                border.setFill(null);
                border.setStroke(Paint.valueOf("red"));
                border.setStrokeWidth(2);
                Platform.runLater(() -> uIParent.getChildren().add(border));
            });
        }
    }

    /**
     * Apply UI panel for controller. UI panel displays cooldown and HP remaining
     *
     * @param panel Game Info UI panel
     */
    public void setUIInfo(InfoPanel panel) {
        tankModel.setTankStateUI(panel);
        Platform.runLater(() -> panel.cooldown(tankModel.getTank().getGun().getNextCooldown()));
    }

    /**
     * Configures ImageView to display
     * Location, turretRotation and etc.
     */
    private void setTankImageView() {
        assert (tankModel != null) && (this.uIParent != null);

        // there i combine turret and chassis textures to
        // get complete tank texture

        chassisView = new ImageView(tankModel.getGameChassis().getTexture());
        turretView = new ImageView(tankModel.getGameTurret().getTexture());
        turretView.setBlendMode(BlendMode.SRC_OVER);

        chassisView.setTranslateX(tankModel.getGameChassis().getLeftUpper().getX());
        chassisView.setTranslateY(tankModel.getGameChassis().getLeftUpper().getY());

        turretView.setTranslateX(tankModel.getGameTurret().getLeftUpper().getX());
        turretView.setTranslateY(tankModel.getGameTurret().getLeftUpper().getY());

        turretRotation = new Rotate();
        turretRotation.setPivotX(GameConstants.turretConnectionPoint.getX());
        turretRotation.setPivotY(GameConstants.turretConnectionPoint.getY());
        turretRotation.setAngle(tankModel.getGameTurret().getDirectionAngle());
        turretView.getTransforms().add(turretRotation);

        chassisRotation = new Rotate();
        chassisRotation.setPivotX(GameConstants.turretOnChassis.getX());
        chassisRotation.setPivotY(GameConstants.turretOnChassis.getY());
        chassisRotation.setAngle(tankModel.getGameChassis().getDirectionAngle());
        chassisView.getTransforms().add(chassisRotation);

        //viewGroup = new Group(chassisView, turretView);
        Platform.runLater(() -> this.uIParent.getChildren().addAll(chassisView, turretView));
    }

    @Override
    public void handle(Event event) {
        EventType eventType = event.getEventType();
        if (eventType.equals(MouseEvent.MOUSE_CLICKED))
            Platform.runLater(() -> handleMouseClickEvent((MouseEvent) event));
        else if (eventType.equals(MouseEvent.MOUSE_MOVED))
            Platform.runLater(() -> handleMouseMotionEvent((MouseEvent) event));
        else if (eventType.equals(KeyEvent.KEY_PRESSED))
            Platform.runLater(() -> handleKeyboardEvent((KeyEvent) event));
        else if (eventType.equals(KeyEvent.KEY_RELEASED))
            stopMoveSound((KeyEvent) event);
        else throw new IllegalArgumentException("event");
    }

    /**
     * Stops playing move sound when tank stops
     *
     * @param event KeyReleased event
     */
    private void stopMoveSound(KeyEvent event) {
        soundPlayer.stopMoveSound();
    }

    @Override
    public void handleKeyboardEvent(KeyEvent event) {
        KeyCode code = event.getCode();
        trackBorder();
        Platform.runLater(() -> {
            switch (code) {
                case W:
                    motionManager.forwardMove(this);
                    break;
                case A:
                    motionManager.turnLeft(this);
                    break;
                case S:
                    motionManager.backwardsMove(this);
                    break;
                case D:
                    motionManager.turnRight(this);
                    break;
                default:
                    return;
            }
            soundPlayer.play(SoundPlayer.SoundTypes.MOVE);
        });
    }

    @Override
    public void handleMouseClickEvent(javafx.scene.input.MouseEvent event) {
        Platform.runLater(() -> {
            motionManager.fire(this);
        });
    }

    @Override
    public void handleMouseMotionEvent(javafx.scene.input.MouseEvent event) {
        previousMouseEvent = event;
        if (tankModel.getGameTurret().getTurret() instanceof SAUTurret)
            return;

        double sceneX = event.getSceneX(), sceneY = event.getSceneY();
        double currX = turretView.getTranslateX();
        double currY = turretView.getTranslateY();

        if (currY != sceneY) {
            Platform.runLater(() -> {
                double angle = Math.atan2(currY - sceneY, currX - sceneX);
                angle = motionManager.rotateTurret(this, angle);
                tankModel.getGameTurret().setDirectionAngle(angle);
            });
        }
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.append((event, tail12) -> {
            this.handle(event);
            return event;
        });
    }

    /**
     * Free all resources
     */
    @Override
    public void dispose() {
        if (this.uIParent == null)
            return;
        this.uIParent.getChildren().removeAll(chassisView, turretView);
        this.motionManager.unRegister(tankModel);
        soundPlayer.play(SoundPlayer.SoundTypes.EXPLOSION);
        if (border != null)
            Platform.runLater(() -> {
                uIParent.getChildren().remove(border);
            });
        soundPlayer.stopMoveSound();
    }

    /**
     * Checks tank HP using timer.
     * If it is negative, dispose tank
     */
    private void setAliveChecker() {
        final int CHECK_DELAY = 10;
        new Timer(CHECK_DELAY, (e) -> {
            if (!tankModel.getTank().isAlive()) {
                Platform.runLater(this::dispose);
                ((Timer) e.getSource()).stop();
            }
        }).start();
    }

    public double getDirectionAngle() {
        return chassisRotation.getAngle();
    }

}
