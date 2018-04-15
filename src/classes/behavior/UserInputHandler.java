package classes.behavior;

import classes.gameObjects.GameTank;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Additional layer between controller class and TankController.
 * Created for redirecting events particularly to TankController
 */

public class UserInputHandler {
    private TankController tankController;

    public UserInputHandler(AnchorPane pane, BorderPane eventPane) {
        if (pane == null)
            throw new NullPointerException("pane");
        ITank tank = TankConstructor.createCyclicalTank(4, 1500, 3, 1, false);
        GameTank gameTank = new GameTank(tank, null, 1, new Point2D(60, 60));
        tankController = new TankController(pane, gameTank, 1);

        setupKeyboardListener(eventPane);
        setupMouseClickListeners(eventPane);
        setupMouseMotionListeners(eventPane);
    }

    /**
     * Redirection of KeyEvents to TankController
     */
    private void setupKeyboardListener(Node pane) {
        pane.setOnKeyPressed(tankController);
        pane.setOnKeyReleased(tankController);
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

    public TankController getTankController() {
        return tankController;
    }
}
