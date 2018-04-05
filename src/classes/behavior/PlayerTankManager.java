package classes.behavior;

import classes.gameObjects.GameTankInstance;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.RotateBuilder;

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
    private ImageView chassisView, turretView;
    private Group viewGroup;
    private Rotate rotation;

    private GameTankInstance tankInstance = null;
    private static final double DELTA_ANGLE = Math.PI / 16;

    public PlayerTankManager(AnchorPane parent) {
        initDefaultTank();
        setTankImageView(parent);
    }

    /**
     * Configures ImageView to display
     * Location, rotation and etc.
     */
    private void setTankImageView(AnchorPane parent) {
        assert (tankInstance != null) && (parent != null);

        // there i combine turret and chassis textures to
        // get complete tank texture

        chassisView = new ImageView(tankInstance.getGameChassis().getTexture());
        turretView = new ImageView(tankInstance.getGameTurret().getTexture());
        turretView.setBlendMode(BlendMode.SRC_OVER);

        chassisView.setTranslateX(tankInstance.getGameChassis().getPaintCoordinates().x);
        chassisView.setTranslateY(tankInstance.getGameChassis().getPaintCoordinates().y);

        turretView.setTranslateX(tankInstance.getGameTurret().getPaintCoordinates().x);
        turretView.setTranslateY(tankInstance.getGameTurret().getPaintCoordinates().y);

        rotation = new Rotate();
        rotation.setPivotX(25);//Set the Pivot's X to be the same location as the Circle's X. This is only used to help you see the Pivot's point
        rotation.setPivotY(30);
        rotation.setAngle(0);
        turretView.getTransforms().add(rotation);

        viewGroup = new Group(chassisView, turretView);
        parent.getChildren().add(viewGroup);

    }


    /**
     * Fills tank model with default tank. Should be removed later after
     * creating tank chooser
     */
    private void initDefaultTank() {
        ITank tankModel = TankConstructor.createDrumTank();

        tankInstance = new GameTankInstance(tankModel, null,
                new Point(200, 200), 100, 50);
    }

    @Override
    public void handle(Event event) {
        EventType eventType = event.getEventType();

        if (eventType.equals(MouseEvent.MOUSE_CLICKED))
            handleMouseClickEvent((MouseEvent) event);
        else if (eventType.equals(MouseEvent.MOUSE_MOVED))
            handleMouseMotionEvent((MouseEvent) event);
        else if (eventType.equals(KeyEvent.ANY))
            handleKeyboardEvent((KeyEvent) event);
        else throw new IllegalArgumentException("event");
    }

    @Override
    public void handleKeyboardEvent(KeyEvent event) {
        KeyCode code = event.getCode();
        switch (code) {
            case W:
                break;
            case A:
                tankInstance.turnLeft(DELTA_ANGLE);
                break;
            case S:
                break;
            case D:
                tankInstance.turnRight(DELTA_ANGLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void handleMouseClickEvent(javafx.scene.input.MouseEvent event) {
        if (event.isPrimaryButtonDown())
            tankInstance.fire();

    }

    @Override
    public void handleMouseMotionEvent(javafx.scene.input.MouseEvent event) {
        double sceneX = event.getSceneX(), sceneY = event.getSceneY();
        double currX = tankInstance.getGameTurret().getPaintCoordinates().x;
        double currY = tankInstance.getGameTurret().getPaintCoordinates().y;
        if (currY != sceneY) {
            double angle = Math.atan2( currY - sceneY, currX - sceneX);
            rotation.setAngle(Math.toDegrees(angle) + 90);
        }
    }
}
