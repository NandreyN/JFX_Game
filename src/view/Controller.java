package view;

import classes.behavior.EnemyTankManager;
import classes.behavior.UserInputHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;

public class Controller {
    @FXML
    private AnchorPane mainPane;
    private UserInputHandler inputHandler;
    private EnemyTankManager enemyTankManager;

    @FXML
    private void initialize() {
        inputHandler = new UserInputHandler(mainPane);
        enemyTankManager = new EnemyTankManager(mainPane);
        enemyTankManager.startTrackingPlayersTank(inputHandler.getPlayerTankManager());
    }
}
