package classes.behavior;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public interface ITankManager {
    void handleKeyboardEvent(KeyEvent event);

    void handleMouseClickEvent(MouseEvent event);

    void handleMouseMotionEvent(MouseEvent event);
}
