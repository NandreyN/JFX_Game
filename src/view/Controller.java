package view;

import classes.behavior.EnemyTankManager;
import classes.behavior.UserInputHandler;
import classes.behavior.ViewMotionManager;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class Controller {
    @FXML
    private AnchorPane mainPane;
    private UserInputHandler inputHandler;
    private EnemyTankManager enemyTankManager;

    @FXML
    private void initialize() {
        ViewMotionManager.setParent(mainPane);
        inputHandler = new UserInputHandler(mainPane);
        enemyTankManager = new EnemyTankManager(mainPane);
        enemyTankManager.startTrackingPlayersTank(inputHandler.getTankController());
    }
}
