package view;

import classes.behavior.EnemyTankManager;
import classes.behavior.UserInputHandler;
import classes.behavior.ViewMotionManager;
import classes.gameObjects.Box;
import classes.levels.ConfigurationReader;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import view.infoPanel.InfoPanel;

import java.io.FileNotFoundException;

public class Controller {
    @FXML
    private AnchorPane gameFieldPane;
    @FXML
    private BorderPane globalPane;
    private InfoPanel infoPanel;

    private ConfigurationReader configReader;

    @FXML
    private void initialize() throws FileNotFoundException {
        ViewMotionManager.setParent(gameFieldPane);
        createAndSetupInfoPanel();
        configReader = ConfigurationReader.getInstance();
        configReader.setupLevel(0, infoPanel, gameFieldPane, globalPane);
    }

    private void createAndSetupInfoPanel() {
        infoPanel = new InfoPanel(1000);
        globalPane.setTop(infoPanel);
    }
}
