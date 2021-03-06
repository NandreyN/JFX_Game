package classes.behavior;

import classes.gameObjects.GameConstants;
import classes.gameObjects.GameTank;
import classes.sound.SoundPlayer;
import classes.tanks.parts.SAUTurret;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import view.infoPanel.ITankStateUI;

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
    private Timer aliveChecker;

    private BooleanProperty isAliveProperty;
    boolean canMoveForward = true, canRotateLeft = true, canRotateRight = true, isDisposed = false;

    public TankController(AnchorPane parent, GameTank tank) {
        this.uIParent = parent;
        this.tankModel = tank;
        motionManager = ViewMotionManager.getInstance();
        soundPlayer = SoundPlayer.getInstance();
        isAliveProperty = new SimpleBooleanProperty(true);

        setTankImageView();
        setAliveChecker();
        trackBorder();

        ResourceDisposer.getInstance().add(this);
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    /**
     * Displays red rectangle border for current tank. For debug purposes
     */
    private void trackBorder() {
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
    public void setUIInfo(ITankStateUI panel) {
        tankModel.setTankStateUI(panel);
        panel.cooldown(tankModel.getTank().getGun().getNextCooldown());
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
        if (isDisposed)
            return;

        EventType eventType = event.getEventType();
        if (eventType.equals(MouseEvent.MOUSE_CLICKED)) {
            handleMouseClickEvent((MouseEvent) event);
        } else if (eventType.equals(MouseEvent.MOUSE_MOVED)) {
            handleMouseMotionEvent((MouseEvent) event);
        } else if (eventType.equals(KeyEvent.KEY_PRESSED)) {
            handleKeyboardEvent((KeyEvent) event);
        } else if (eventType.equals(KeyEvent.KEY_RELEASED)) {
            stopMoveSound();
        } else throw new IllegalArgumentException("event");
    }

    /**
     * Stops playing move sound when tank stops
     */
    private void stopMoveSound() {
        soundPlayer.stopMoveSound();
    }

    @Override
    public void handleKeyboardEvent(KeyEvent event) {
        KeyCode code = event.getCode();
        trackBorder();
        switch (code) {
            case W:
                motionManager.forwardMove(this);
                //isDirectionForwardPressed = true;
                break;
            case A:
                motionManager.turnLeft(this);
                //isRotationLeftPressed = true;
                break;
            case S:
                motionManager.backwardsMove(this);
                //isDirectionBackPressed = true;
                break;
            case D:
                motionManager.turnRight(this);
                //isRotationRightPressed = true;
                break;
            default:
                return;
        }
        soundPlayer.play(SoundPlayer.SoundTypes.MOVE);
    }

    @Override
    public void handleMouseClickEvent(javafx.scene.input.MouseEvent event) {
        motionManager.fire(this);
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
            double angle = Math.atan2(currY - sceneY, currX - sceneX);
            angle = motionManager.rotateTurret(this, angle);
            tankModel.getGameTurret().setDirectionAngle(angle);
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
        isDisposed = true;
        aliveChecker.stop();

        if (this.uIParent != null)
            Platform.runLater(() -> {
                if (!chassisView.getTransforms().isEmpty()) {
                    chassisView.getTransforms().clear();
                }
                if (!turretView.getTransforms().isEmpty()) {
                    chassisView.getTransforms().clear();
                }

                this.uIParent.getChildren().removeAll(chassisView, turretView);
            });
        this.motionManager.unRegister(tankModel);
        soundPlayer.stopMoveSound();
        if (border != null)
            Platform.runLater(() -> uIParent.getChildren().remove(border));
        tankModel.dispose();
    }

    /**
     * Checks tank HP using timer.
     * If it is negative, dispose tank
     */

    private void setAliveChecker() {
        final int CHECK_DELAY = 10;
        aliveChecker = new Timer(CHECK_DELAY, (e) -> {
            if (!tankModel.getTank().isAlive()) {
                isAliveProperty.set(false);
                ((Timer) e.getSource()).stop();
                Platform.runLater(this::dispose);
            }
        });
        aliveChecker.start();
    }

    public double getDirectionAngle() {
        return chassisRotation.getAngle();
    }

    public BooleanProperty isAliveProperty() {
        return isAliveProperty;
    }
}
