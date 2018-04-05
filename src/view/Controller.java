package view;

import classes.behavior.UserInputHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;

public class Controller {
    @FXML
    private AnchorPane mainPane;
    private UserInputHandler inputHandler;

    @FXML
    private void initialize() {
        inputHandler = new UserInputHandler(mainPane);
    }
}
