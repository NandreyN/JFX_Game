package classes.behavior;

import javafx.scene.Node;

/**
 * Additional layer between controller class and PlayerTankManager.
 * Created for redirecting events particularly to PlayerTankManager
 */

public class UserInputHandler {
    private Node pane;
    private TankManager playerTankManager;

    public UserInputHandler(Node pane) {
        if (pane == null)
            throw new NullPointerException("pane");
        this.pane = pane;
        playerTankManager = new PlayerTankManager(pane);

        setupKeyboardListener();
        setupMouseClickListeners();
        setupMouseMotionListeners();
    }

    /**
     * Redirection of KeyEvents to PlayerTankManager
     */
    private void setupKeyboardListener() {
        pane.setOnKeyPressed(playerTankManager);
    }

    /**
     * Redirection of MouseClick events to PlayerTankManager
     */
    private void setupMouseClickListeners() {
        pane.setOnMouseClicked(playerTankManager);
    }

    /**
     * Redirection of MOuseMoved events to PlayerTankManager
     */
    private void setupMouseMotionListeners() {
        pane.setOnMouseMoved(playerTankManager);
    }
}
