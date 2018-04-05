package classes.behavior;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public interface ITankManager {
    /**
     * Handler for keyboard game events - relocating tank
     * on the screen and changing model state
     *
     * @param event Keyboard event triggered
     */
    void handleKeyboardEvent(KeyEvent event);


    /**
     * Handler for mouse click game events -
     * fire and reflecting state to the model
     *
     * @param event Mouse click event triggered
     */
    void handleMouseClickEvent(MouseEvent event);

    /**
     * Handler for mouse move game events -
     * changing turret direction and reflects in to
     * the model
     *
     * @param event Mouse move event
     */
    void handleMouseMotionEvent(MouseEvent event);
}
