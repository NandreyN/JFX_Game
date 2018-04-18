package classes.levels;

import classes.behavior.EnemyTankManager;
import classes.behavior.ResourceDisposer;
import classes.behavior.UserInputHandler;
import classes.behavior.ViewMotionManager;
import classes.gameObjects.Box;
import classes.gameObjects.GameObject;
import classes.gameObjects.GameTank;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.infoPanel.InfoPanel;
import view.scenes.SceneLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Class for parsing level configuration files
 * and setting up level game scene
 */
public class ConfigurationReader {
    private static int MAX_LEVEL = 2;
    private static String LEVEL_FOLDER = "config\\levels\\";
    private static ConfigurationReader configurationReader;

    private SceneLoader sceneLoader;

    private UserInputHandler inputHandler;
    private EnemyTankManager enemyTankManager;

    public static ConfigurationReader getInstance() {
        if (configurationReader == null)
            configurationReader = new ConfigurationReader();
        return configurationReader;
    }

    public ConfigurationReader() {
        sceneLoader = SceneLoader.getInstance();
    }

    /**
     * Loads level with provided number
     *
     * @param level level id
     * @return List of GameObjects to be added to the game scene except og Player`s tank
     * @throws FileNotFoundException
     */
    private List<GameObject> loadLevel(int level) throws FileNotFoundException {
        Scanner in = new Scanner(new File(LEVEL_FOLDER + level + ".txt"));
        List<GameObject> gameList = new ArrayList<>();

        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.startsWith("@"))
                continue;
            String[] params = line.split("\\s+", 0);
            switch (params[0].toLowerCase()) {
                case "sau": {
                    String gunType = params[1];
                    double posX = Double.parseDouble(params[2]), posY = Double.parseDouble(params[3]), angle = Double.parseDouble(params[4]);
                    if (gunType.equals("drum")) {
                        double internal = Double.parseDouble(params[5]), external = Double.parseDouble(params[6]),
                                hp = Double.parseDouble(params[8]), speedF = Double.parseDouble(params[9]),
                                speedB = Double.parseDouble(params[10]), damage = Double.parseDouble(params[11]),
                                missileSpeed = Double.parseDouble(params[12]);
                        int capacity = Integer.parseInt(params[7]);

                        ITank tank = TankConstructor.createDrumTank(internal, external, capacity, hp, speedF, speedB, true);
                        GameTank gameTank = new GameTank(tank, null, 2, new Point2D(posX, posY), angle);

                        gameList.add(gameTank);
                    } else if (gunType.equals("cyclical")) {
                        double coolDown = Double.parseDouble(params[5]),
                                hp = Double.parseDouble(params[6]), speedF = Double.parseDouble(params[7]),
                                speedB = Double.parseDouble(params[8]), damage = Double.parseDouble(params[9]),
                                missileSpeed = Double.parseDouble(params[10]);

                        ITank tank = TankConstructor.createCyclicalTank(coolDown, hp, speedF, speedB, true);
                        GameTank gameTank = new GameTank(tank, null, 2, new Point2D(posX, posY), angle);
                        gameList.add(gameTank);
                    }
                    break;
                }
                case "box":
                    double x = Double.parseDouble(params[1]), y = Double.parseDouble(params[2]),
                            width = Double.parseDouble(params[3]), height = Double.parseDouble(params[4]);
                    gameList.add(new Box(TextureLoader.getBoxTexture(), new Point2D(x, y), width, height, 0));
                    break;
                case "tank":
                    String gunType = params[1];
                    double posX = Double.parseDouble(params[2]), posY = Double.parseDouble(params[3]), angle = Double.parseDouble(params[4]);
                    if (gunType.equals("drum")) {
                        double internal = Double.parseDouble(params[5]), external = Double.parseDouble(params[6]),
                                hp = Double.parseDouble(params[8]), speedF = Double.parseDouble(params[9]),
                                speedB = Double.parseDouble(params[10]), damage = Double.parseDouble(params[11]),
                                missileSpeed = Double.parseDouble(params[12]);
                        int capacity = Integer.parseInt(params[7]);

                        ITank tank = TankConstructor.createDrumTank(internal, external, capacity, hp, speedF, speedB, false);
                        GameTank gameTank = new GameTank(tank, null, 2, new Point2D(posX, posY), angle);
                        gameList.add(gameTank);
                    } else if (gunType.equals("cyclical")) {
                        double coolDown = Double.parseDouble(params[5]),
                                hp = Double.parseDouble(params[6]), speedF = Double.parseDouble(params[7]),
                                speedB = Double.parseDouble(params[8]), damage = Double.parseDouble(params[9]),
                                missileSpeed = Double.parseDouble(params[10]);

                        ITank tank = TankConstructor.createCyclicalTank(coolDown, hp, speedF, speedB, false);
                        GameTank gameTank = new GameTank(tank, null, 2, new Point2D(posX, posY), angle);
                        gameList.add(gameTank);
                    }
                    break;

                case "@":
                    break;
                default:
                    throw new IllegalArgumentException("params[0]");
            }
        }
        return gameList;
    }

    /**
     * Shown on level completed
     *
     * @return Load next level or close the game
     */
    private boolean askToContinueDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Level completed");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void setupLevel(int level, InfoPanel infoPanel, AnchorPane gameFieldPane, BorderPane globalPane) throws FileNotFoundException {
        ViewMotionManager.getInstance().reset();

        inputHandler = new UserInputHandler(gameFieldPane, globalPane);
        List<GameObject> gameLevelObjects = loadLevel(level);
        List<GameTank> tanks = new ArrayList<>();
        List<Box> boxes = new ArrayList<>();

        for (GameObject o : gameLevelObjects)
            if (o instanceof GameTank)
                tanks.add((GameTank) o);
            else if (o instanceof Box)
                boxes.add((Box) o);

        enemyTankManager = new EnemyTankManager(gameFieldPane, tanks);
        enemyTankManager.startTrackingPlayersTank(inputHandler.getTankController());
        infoPanel.reset();
        inputHandler.getTankController().setUIInfo(infoPanel);
        ViewMotionManager.setupBoxes(boxes);

        inputHandler.getTankController().isAliveProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue)
                    return;
                Platform.runLater(() -> {
                    Stage s = createAndShowStage(sceneLoader.getGameKilledScene(globalPane.getWidth(), globalPane.getHeight()));
                    boolean shouldContinue = askToContinueDialog();
                    if (!shouldContinue) {
                        s.close();
                        ResourceDisposer.getInstance().disposeAll();
                        Platform.exit();
                        return;
                    }
                    s.close();
                    try {
                        ResourceDisposer.getInstance().disposeAll();
                        setupLevel(0, infoPanel, gameFieldPane, globalPane);
                    } catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                });
            }
        });

        enemyTankManager.tanksAliveCountProperty().addListener((observable, oldValue, newValue) -> {
            if ((int) newValue > 0)
                return;
            System.out.println("Level completed");
            if (level + 1 <= MAX_LEVEL) {
                Platform.runLater(() -> {
                    Stage s = createAndShowStage(sceneLoader.getGameLevelCompletedScene(globalPane.getWidth(), globalPane.getHeight()));

                    boolean shouldContinue = askToContinueDialog();
                    if (!shouldContinue) {
                        ResourceDisposer.getInstance().disposeAll();
                        s.close();
                        Platform.exit();
                        return;
                    }
                    gameFieldPane.getChildren().clear();
                    try {
                        s.close();
                        ResourceDisposer.getInstance().disposeAll();
                        setupLevel(level + 1, infoPanel, gameFieldPane, globalPane);
                    } catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                });
            }
        });
    }

    private Stage createAndShowStage(Scene scene) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
        return stage;
    }
}