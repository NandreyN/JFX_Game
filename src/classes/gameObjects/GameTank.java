package classes.gameObjects;

import classes.levels.TextureLoader;
import classes.tanks.ITank;
import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import view.infoPanel.ITankStateUI;

/**
 * Game tank. Incapsulates tank parameters
 */
public class GameTank extends GameObject {
    private ITank tankDataModel;
    private GameTurret turret;
    private GameChassis chassis;
    private ITankStateUI tankStateUI;

    public GameTank(ITank tankModel,
                    @Nullable Image texture, int textureId, Point2D leftUpper, double directionAngle) {
        super(texture, leftUpper, directionAngle);

        chassis = new GameChassis(TextureLoader.getChassisTexture(textureId),
                leftUpper, directionAngle);
        turret = new GameTurret(TextureLoader.getTurretTexture(textureId),
                leftUpper,directionAngle);

        this.tankDataModel = tankModel;
        chassis.setChassis(tankModel.getChassis());
        turret.setTurret(tankModel.getTurret());

        displayedHeight = chassis.getDisplayedHeight();
        displayedWidth = chassis.getDisplayedWidth();
        setValid(true);
    }

    public GameTurret getGameTurret() {
        return turret;
    }

    public GameChassis getGameChassis() {
        return chassis;
    }

    /**
     * Checks whether tank is able to fire on not
     *
     * @return Missile object on success, otherwise null
     */
    public Missile fire() {
        boolean success = tankDataModel.fire();
        if (!success)
            return null;

        double damage = 100;
        double missileSpeed = 3;

        if (tankStateUI != null) {
            tankStateUI.cooldown(tankDataModel.getGun().getNextCooldown());
            damage = 1000;
            missileSpeed = GameConstants.playerMissileSpeed;
        }

        return new Missile(new Image("file:game_textures/Cut/missile.png"), turret.getLeftUpper(),
                missileSpeed, getId(), damage,0);
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

    /**
     * Decrease tank HP on missile hit
     *
     * @param missile Missile object that had hit that tank
     */
    public void damage(Missile missile) {
        tankDataModel.decreaseHP(missile.getDamage());
        if (tankStateUI != null)
            tankStateUI.decreaseHP(missile.getDamage());
    }
}