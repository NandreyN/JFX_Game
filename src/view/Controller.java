package view;

import classes.behavior.EnemyTankManager;
import classes.behavior.UserInputHandler;
import classes.behavior.ViewMotionManager;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import view.infoPanel.InfoPanel;

public class Controller {
    @FXML
    private AnchorPane gameFieldPane;
    @FXML
    private BorderPane globalPane;
    private InfoPanel infoPanel;

    private UserInputHandler inputHandler;
    private EnemyTankManager enemyTankManager;

    @FXML
    private void initialize() {
        ViewMotionManager.setParent(gameFieldPane);
        inputHandler = new UserInputHandler(gameFieldPane, globalPane);
        enemyTankManager = new EnemyTankManager(gameFieldPane);
        enemyTankManager.startTrackingPlayersTank(inputHandler.getTankController());
        createAndSetupInfoPanel();
        inputHandler.getTankController().setUIInfo(infoPanel);
    }

    private void createAndSetupInfoPanel() {
        infoPanel = new InfoPanel(1000);
        globalPane.setTop(infoPanel);
    }
}
