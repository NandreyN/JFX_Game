package classes.behavior;

import classes.gameObjects.GameTank;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.security.Key;

/**
 * Additional layer between controller class and TankController.
 * Created for redirecting events particularly to TankController
 */

public class UserInputHandler {
    private TankController tankController;

    private boolean isDirectionForwardPressed = false, isDirectionBackPressed = false,
            isRotationLeftPressed = false, isRotationRightPressed = false;

    private Timeline timeline;
    private final static int UPDATE_INTERVAL = 50;
    public UserInputHandler(AnchorPane pane, BorderPane eventPane) {
        if (pane == null)
            throw new NullPointerException("pane");
        ITank tank = TankConstructor.createCyclicalTank(4, 1500, 3, 1, false, 5, 1000);
        GameTank gameTank = new GameTank(tank, null, 1, new Point2D(60, 60), 0);
        tankController = new TankController(pane, gameTank);

        setupKeyboardListener(eventPane);
        setupMouseClickListeners(eventPane);
        setupMouseMotionListeners(eventPane);

        timeline = new Timeline(new KeyFrame(Duration.millis(UPDATE_INTERVAL), event -> {
            if (isDirectionForwardPressed) {
                EventGenerator.fireKeyPressedEvent(tankController,KeyCode.W);
            }
            if (isDirectionBackPressed) {
                EventGenerator.fireKeyPressedEvent(tankController,KeyCode.S);
            }
            if (isRotationRightPressed) {
                EventGenerator.fireKeyPressedEvent(tankController,KeyCode.D);
            }
            if (isRotationLeftPressed) {
               EventGenerator.fireKeyPressedEvent(tankController,KeyCode.A);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Redirection of KeyEvents to TankController
     */
    private void setupKeyboardListener(Node pane) {
        pane.setOnKeyPressed((event -> {
            KeyCode c = event.getCode();
            if (c == KeyCode.W) {
                isDirectionForwardPressed = true;
            }
            if (c == KeyCode.A) {
                isRotationLeftPressed = true;
            }
            if (c == KeyCode.S) {
                isDirectionBackPressed = true;
            }
            if (c == KeyCode.D) {
                isRotationRightPressed = true;
            }
        }));

        pane.setOnKeyReleased((event -> {
            KeyCode c = event.getCode();
            if (c == KeyCode.W) {
                isDirectionForwardPressed = false;
            }
            if (c == KeyCode.A) {
                isRotationLeftPressed = false;
            }
            if (c == KeyCode.S) {
                isDirectionBackPressed = false;
            }
            if (c == KeyCode.D) {
                isRotationRightPressed = false;
            }
            tankController.handle(event);
        }));
    }

    /**
     * Redirection of MouseClick events to TankController
     */
    private void setupMouseClickListeners(Node pane) {
        pane.setOnMouseClicked(tankController);
    }

    /**
     * Redirection of MouseMoved events to TankController
     */
    private void setupMouseMotionListeners(Node pane) {
        pane.setOnMouseMoved(tankController);
    }

    /**
     * Simple getter
     *
     * @return controller of tank managed by user
     */
    public TankController getTankController() {
        return tankController;
    }
}
