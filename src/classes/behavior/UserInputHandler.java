package classes.behavior;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Additional layer between controller class and PlayerTankManager.
 * Created for redirecting events particularly to PlayerTankManager
 */

public class UserInputHandler {
    private TankManager playerTankManager;

    public UserInputHandler(AnchorPane pane) {
        if (pane == null)
            throw new NullPointerException("pane");
        playerTankManager = new PlayerTankManager(pane, new Point2D(0, 0), -90,1);

        setupKeyboardListener(pane);
        setupMouseClickListeners(pane);
        setupMouseMotionListeners(pane);
    }

    /**
     * Redirection of KeyEvents to PlayerTankManager
     */
    private void setupKeyboardListener(Node pane) {
        pane.setOnKeyPressed(playerTankManager);
    }

    /**
     * Redirection of MouseClick events to PlayerTankManager
     */
    private void setupMouseClickListeners(Node pane) {
        pane.setOnMouseClicked(playerTankManager);
    }

    /**
     * Redirection of MOuseMoved events to PlayerTankManager
     */
    private void setupMouseMotionListeners(Node pane) {
        pane.setOnMouseMoved(playerTankManager);
    }
}
