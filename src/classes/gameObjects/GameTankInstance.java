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

    public GameTankInstance(@Nullable Image texture, Point2D centre) {
        super(texture, centre);
    }

    public GameTankInstance(ITank tankModel,
                            @Nullable Image texture, int textureId, Point2D centre) {
        super(texture, centre);

        chassis = new GameChassis(new Image("file:game_textures/Cut/chassis_" + textureId + ".png"),
                centre);
        turret = new GameTurret(new Image("file:game_textures/Cut/turret_" + textureId + ".png"),
                centre);

        this.tankDataModel = tankModel;
        chassis.setChassis(tankModel.getChassis());
        turret.setTurret(tankModel.getTurret());

        displayedHeight = chassis.getDisplayedHeight();
        displayedWidth = chassis.getDisplayedWidth();
    }

    public GameTurret getGameTurret() {
        return turret;
    }

    public GameChassis getGameChassis() {
        return chassis;
    }

    public Missile fire() {
        boolean success = tankDataModel.fire();
        if (!success)
            return null;
        return new Missile(new Image("file:game_textures/Cut/missile.png"), turret.getPaintCoordinates(), 10, getId());
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