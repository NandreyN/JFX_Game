package view;

import classes.behavior.UserInputHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class Controller {
    @FXML
    private Canvas canvas;
    private UserInputHandler inputHandler;

    @FXML
    private void initialize() {
        inputHandler = new UserInputHandler(canvas);
    }
}
