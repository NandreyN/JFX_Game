package classes.behavior;

import classes.gameObjects.GameChassis;
import classes.gameObjects.GameObject;
import classes.gameObjects.GameTankInstance;
import classes.gameObjects.GameTurret;
import classes.tanks.ITank;
import classes.tanks.TankConstructor;
import javafx.scene.image.Image;

import java.awt.*;

public class PlayerTankManager extends TankManager {
    private GameTankInstance tankInstance = null;

    public PlayerTankManager() {
        initDefaultTank();
    }

    private void initDefaultTank() {
        ITank tankModel = TankConstructor.createDrumTank();

        tankInstance = new GameTankInstance(tankModel, null,
                new Point(0, 0), 100, 50);
    }

    @Override
    public void registerEventListeners() {

    }
}
