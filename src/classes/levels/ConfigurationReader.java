package classes.levels;

import classes.behavior.EnemyTankManager;
import classes.behavior.UserInputHandler;
import classes.gameObjects.GameObject;
import classes.gameObjects.GameTank;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import view.infoPanel.InfoPanel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigurationReader {
    private static String LEVEL_FOLDER = "config\\levels\\";
    private static ConfigurationReader configurationReader;

    private UserInputHandler inputHandler;
    private EnemyTankManager enemyTankManager;

    public static ConfigurationReader getInstance() {
        if (configurationReader == null)
            configurationReader = new ConfigurationReader();
        return configurationReader;
    }

    public ConfigurationReader() {
    }

    private List<GameObject> loadLevel(int level) throws FileNotFoundException {
        Scanner in = new Scanner(new File(LEVEL_FOLDER + level + ".txt"));
        List<GameObject> gameList = new ArrayList<>();

        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.startsWith("@"))
                continue;
            String[] params = line.split("\\s+",0);
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
                        GameTank gameTank = new GameTank(tank, null, 2, new Point2D(posX, posY));

                        gameList.add(gameTank);
                    } else if (gunType.equals("cyclical")) {
                        double coolDown = Double.parseDouble(params[5]),
                                hp = Double.parseDouble(params[6]), speedF = Double.parseDouble(params[7]),
                                speedB = Double.parseDouble(params[8]), damage = Double.parseDouble(params[9]),
                                missileSpeed = Double.parseDouble(params[10]);

                        ITank tank = TankConstructor.createCyclicalTank(coolDown, hp, speedF, speedB, true);
                        GameTank gameTank = new GameTank(tank, null, 2, new Point2D(posX, posY));
                        gameList.add(gameTank);
                    }
                    break;
                }
                case "box":
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
                        GameTank gameTank = new GameTank(tank, null, 2, new Point2D(posX, posY));
                        gameList.add(gameTank);
                    } else if (gunType.equals("cyclical")) {
                        double coolDown = Double.parseDouble(params[5]),
                                hp = Double.parseDouble(params[6]), speedF = Double.parseDouble(params[7]),
                                speedB = Double.parseDouble(params[8]), damage = Double.parseDouble(params[9]),
                                missileSpeed = Double.parseDouble(params[10]);

                        ITank tank = TankConstructor.createCyclicalTank(coolDown, hp, speedF, speedB, false);
                        GameTank gameTank = new GameTank(tank, null, 2, new Point2D(posX, posY));
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

    public void setupLevel(int level, InfoPanel infoPanel, AnchorPane gameFieldPane, BorderPane globalPane) throws FileNotFoundException {
        inputHandler = new UserInputHandler(gameFieldPane, globalPane);
        List<GameObject> gameLevelObjects = loadLevel(level);
        List<GameTank> tanks = new ArrayList<>();
        for (GameObject o : gameLevelObjects)
            if (o instanceof GameTank)
                tanks.add((GameTank) o);

        enemyTankManager = new EnemyTankManager(gameFieldPane, tanks);
        enemyTankManager.startTrackingPlayersTank(inputHandler.getTankController());
        inputHandler.getTankController().setUIInfo(infoPanel);
    }

}
