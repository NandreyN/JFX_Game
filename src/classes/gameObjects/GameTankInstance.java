package classes.gameObjects;

import classes.tanks.ITank;
import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.awt.*;

public class GameTankInstance extends GameObject {
    private ITank tankDataModel;
    private GameTurret turret;
    private GameChassis chassis;

    public GameTankInstance(@Nullable Image texture, Point2D centre, double dispHeight, double dispWidth) {
        super(texture, centre, dispHeight, dispWidth);
    }

    public GameTankInstance(ITank tankModel,
                            @Nullable Image texture, Point2D centre, double dispHeight, double dispWidth) {
        super(texture, centre, dispHeight, dispWidth);

        chassis = new GameChassis(new Image("file:game_textures/Cut/chassis_1.png"),
                centre, 100, 50);
        turret = new GameTurret(new Image("file:game_textures/Cut/turret_1.png"),
                centre, 100, 50);

        this.tankDataModel = tankModel;
        chassis.setChassis(tankModel.getChassis());
        turret.setTurret(tankModel.getTurret());
    }

    public GameTurret getGameTurret() {
        return turret;
    }

    public GameChassis getGameChassis() {
        return chassis;
    }

    public boolean fire() {
        return tankDataModel.fire();
    }

    public void rotateTurret(double toAngle) {
        tankDataModel.rotateTurret(toAngle);
    }

    public void turnLeft(double absDeltaAngle) {
        tankDataModel.turnLeft(absDeltaAngle);
    }


    public void turnRight(double absDeltaAngle) {
        tankDataModel.turnRight(absDeltaAngle);
    }
}