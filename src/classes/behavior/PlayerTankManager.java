package classes.behavior;

import classes.gameObjects.GameTankInstance;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import classes.tanks.parts.SAUTurret;
import javafx.animation.PathTransition;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.awt.*;

/**
 * Class in created for handling events dedicated to player`s tank
 * It is responsible for handling input events (mouse , keyboard)
 * and changing model and view state , ex:
 * Rotating turret inside model and reflecting model state to the view.
 * <p>
 * View component should be pushed to another ViewHandler class later.
 */

public class PlayerTankManager extends TankManager {
    ImageView chassisView, turretView;
    Rotate turretRotation, chassisRotation;
    private ViewMotionManager motionManager;

    GameTankInstance tankInstance = null;
    static final double DELTA_ANGLE = Math.PI / 146;

    public PlayerTankManager(AnchorPane parent) {
        initDefaultTank();
        motionManager = ViewMotionManager.getInstance();

        setTankImageView(parent);
    }

    /**
     * Configures ImageView to display
     * Location, turretRotation and etc.
     */
    private void setTankImageView(AnchorPane parent) {
        assert (tankInstance != null) && (parent != null);

        // there i combine turret and chassis textures to
        // get complete tank texture

        chassisView = new ImageView(tankInstance.getGameChassis().getTexture());
        turretView = new ImageView(tankInstance.getGameTurret().getTexture());
        turretView.setBlendMode(BlendMode.SRC_OVER);

        chassisView.setTranslateX(tankInstance.getGameChassis().getPaintCoordinates().getX());
        chassisView.setTranslateY(tankInstance.getGameChassis().getPaintCoordinates().getY());

        turretView.setTranslateX(tankInstance.getGameTurret().getPaintCoordinates().getX());
        turretView.setTranslateY(tankInstance.getGameTurret().getPaintCoordinates().getY());

        turretRotation = new Rotate();
        turretRotation.setPivotX(25);
        turretRotation.setPivotY(30);
        turretRotation.setAngle(0);
        turretView.getTransforms().add(turretRotation);

        chassisRotation = new Rotate();
        chassisRotation.setPivotX(33);
        chassisRotation.setPivotY(65);
        chassisRotation.setAngle(0);
        chassisView.getTransforms().add(chassisRotation);

        //viewGroup = new Group(chassisView, turretView);
        parent.getChildren().addAll(chassisView, turretView);
    }


    /**
     * Fills tank model with default tank. Should be removed later after
     * creating tank chooser
     */
    private void initDefaultTank() {
        ITank tankModel = TankConstructor.createDrumTank();

        tankInstance = new GameTankInstance(tankModel, null,
                new Point2D(200, 200), 100, 50);
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
                motionManager.turnLeft(this);
                break;
            case S:
                motionManager.backwardsMove(this);
                break;
            case D:
                motionManager.turnRight(this);
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
        if (tankInstance.getGameTurret().getTurret() instanceof SAUTurret)
            return;

        double sceneX = event.getSceneX(), sceneY = event.getSceneY();
        double currX = tankInstance.getGameTurret().getPaintCoordinates().getX();
        double currY = tankInstance.getGameTurret().getPaintCoordinates().getY();

        if (currY != sceneY) {
            double angle = Math.atan2(currY - sceneY, currX - sceneX);
            motionManager.rotateTurret(this, angle);
        }
    }
}
