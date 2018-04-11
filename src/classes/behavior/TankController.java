package classes.behavior;

import classes.gameObjects.GameConstants;
import classes.gameObjects.GameTank;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import classes.tanks.parts.SAUTurret;
import javafx.event.*;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;

/**
 * Class in created for handling events dedicated to player`s tank
 * It is responsible for handling input events (mouse , keyboard)
 * and changing model and view state , ex:
 * Rotating turret inside model and reflecting model state to the view.
 * <p>
 * View component should be pushed to another ViewHandler class later.
 */

public class TankController extends AbstractTankController implements EventTarget {
    ImageView chassisView, turretView;
    Rotate turretRotation, chassisRotation;
    private ViewMotionManager motionManager;
    MouseEvent previousMouseEvent;

    GameTank tankInstance = null;
    static final double DELTA_ANGLE = Math.PI / 146;

    public TankController(AnchorPane parent, Point2D initialPosition, double orientationAngle, int textureId) {
        initDefaultTank(initialPosition, textureId, orientationAngle);
        motionManager = ViewMotionManager.getInstance();
        setTankImageView(parent, orientationAngle);
    }

    /**
     * Configures ImageView to display
     * Location, turretRotation and etc.
     */
    private void setTankImageView(AnchorPane parent, double initAngle) {
        assert (tankInstance != null) && (parent != null);

        // there i combine turret and chassis textures to
        // get complete tank texture

        chassisView = new ImageView(tankInstance.getGameChassis().getTexture());
        turretView = new ImageView(tankInstance.getGameTurret().getTexture());
        turretView.setBlendMode(BlendMode.SRC_OVER);

        chassisView.setTranslateX(tankInstance.getGameChassis().getLeftUpper().getX());
        chassisView.setTranslateY(tankInstance.getGameChassis().getLeftUpper().getY());

        turretView.setTranslateX(tankInstance.getGameTurret().getLeftUpper().getX());
        turretView.setTranslateY(tankInstance.getGameTurret().getLeftUpper().getY());

        turretRotation = new Rotate();
        turretRotation.setPivotX(GameConstants.turretConnectionPoint.getX());
        turretRotation.setPivotY(GameConstants.turretConnectionPoint.getY());
        turretRotation.setAngle(initAngle);
        turretView.getTransforms().add(turretRotation);

        chassisRotation = new Rotate();
        chassisRotation.setPivotX(GameConstants.turretOnChassis.getX());
        chassisRotation.setPivotY(GameConstants.turretOnChassis.getY());
        chassisRotation.setAngle(initAngle);
        chassisView.getTransforms().add(chassisRotation);

        //viewGroup = new Group(chassisView, turretView);
        parent.getChildren().addAll(chassisView, turretView);
    }

    /**
     * Fills tank model with default tank. Should be removed later after
     * creating tank chooser
     */
    private void initDefaultTank(Point2D initPosition, int textureId, double defaultAngle) {
        ITank tankModel = TankConstructor.createDrumTank();

        tankInstance = new GameTank(tankModel, null, textureId,
                initPosition);
        tankInstance.getGameTurret().setDirectionAngle(defaultAngle);
        tankInstance.getGameChassis().setDirectionAngle(defaultAngle);
        tankInstance.setDirectionAngle(defaultAngle);
    }

    @Override
    public void handle(Event event) {
        EventType eventType = event.getEventType();

        if (eventType.equals(MouseEvent.MOUSE_CLICKED))
            handleMouseClickEvent((MouseEvent) event);
        else if (eventType.equals(MouseEvent.MOUSE_MOVED))
            handleMouseMotionEvent((MouseEvent) event);
        else if (eventType.equals(KeyEvent.KEY_PRESSED))
            handleKeyboardEvent((KeyEvent) event);
        else throw new IllegalArgumentException("event");
    }


    @Override
    public void handleKeyboardEvent(KeyEvent event) {
        KeyCode code = event.getCode();
        switch (code) {
            case W:
                motionManager.forwardMove(this);
                break;
            case A:
                double a = motionManager.turnLeft(this);
                tankInstance.setDirectionAngle(a);
                tankInstance.getGameChassis().setDirectionAngle(a);
                break;
            case S:
                motionManager.backwardsMove(this);
                break;
            case D:
                double a2 = motionManager.turnRight(this);
                tankInstance.setDirectionAngle(a2);
                tankInstance.getGameChassis().setDirectionAngle(a2);
                break;
            default:
                break;
        }
    }

    @Override
    public void handleMouseClickEvent(javafx.scene.input.MouseEvent event) {
        motionManager.fire(this);
    }

    @Override
    public void handleMouseMotionEvent(javafx.scene.input.MouseEvent event) {
        previousMouseEvent = event;
        if (tankInstance.getGameTurret().getTurret() instanceof SAUTurret)
            return;

        double sceneX = event.getSceneX(), sceneY = event.getSceneY();
        double currX = tankInstance.getGameTurret().getLeftUpper().getX();
        double currY = tankInstance.getGameTurret().getLeftUpper().getY();

        if (currY != sceneY) {
            double angle = Math.atan2(currY - sceneY, currX - sceneX);
            angle = motionManager.rotateTurret(this, angle);
            tankInstance.getGameTurret().setDirectionAngle(angle);
        }
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        return tail.append((event, tail12) -> {
            this.handle(event);
            return event;
        });
    }
}
