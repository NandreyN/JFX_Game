package classes.behavior;

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
        tankController = new TankController(pane, new Point2D(60, 60), -90, 1);

        setupKeyboardListener(eventPane);
        setupMouseClickListeners(eventPane);
        setupMouseMotionListeners(eventPane);
    }

    /**
     * Redirection of KeyEvents to TankController
     */
    private void setupKeyboardListener(Node pane) {
        pane.setOnKeyPressed(tankController);
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
