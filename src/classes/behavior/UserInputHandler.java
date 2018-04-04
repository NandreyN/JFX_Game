package classes.behavior;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

public class UserInputHandler {
    private List<ITankManager> tankManagers;

    public UserInputHandler(Canvas canvas) {
        tankManagers = new ArrayList<>();
        setupKeyboardListener();
        setupMouseClickListeners();
        setupMouseMotionListeners();
    }

    private void setupKeyboardListener() {
    }

    private void setupMouseClickListeners() {
    }

    private void setupMouseMotionListeners() {
    }
}
