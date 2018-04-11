package classes.gameObjects;

import classes.tanks.ITank;
import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import view.infoPanel.ITankStateUI;

public class GameTank extends GameObject {
    private ITank tankDataModel;
    private GameTurret turret;
    private GameChassis chassis;
    private ITankStateUI tankStateUI;

    public GameTank(ITank tankModel,
                    @Nullable Image texture, int textureId, Point2D leftUpper) {
        super(texture, leftUpper);

        chassis = new GameChassis(new Image("file:game_textures/Cut/chassis_" + textureId + ".png"),
                leftUpper);
        turret = new GameTurret(new Image("file:game_textures/Cut/turret_" + textureId + ".png"),
                leftUpper);

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
        if (tankStateUI != null)
            tankStateUI.cooldown(tankDataModel.getGun().getNextCooldown());

        return new Missile(new Image("file:game_textures/Cut/missile.png"), turret.getLeftUpper(), 100, getId());
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

    public void setTankStateUI(ITankStateUI stateUI) {
        this.tankStateUI = stateUI;
    }

    public ITank getTank() {
        return this.tankDataModel;
    }
}