package classes.tanks;

import classes.gameObjects.GameObject;
import classes.tanks.parts.IChassis;
import classes.tanks.parts.IGun;
import classes.tanks.parts.ITurret;

public abstract class AbstractTank implements ITank {
    ITurret turret;
    IChassis chassis;

    public AbstractTank(ITurret turret, IChassis chassis) {
        this.turret = turret;
        this.chassis = chassis;
        if (turret == null || chassis == null)
            throw new NullPointerException("Some tank parts are null");
    }

    @Override
    public void turnLeft(double absDeltaAngle) {
        chassis.turnLeft(absDeltaAngle);
    }

    @Override
    public void turnRight(double absDeltaAngle) {
        chassis.turnRight(absDeltaAngle);
    }

    @Override
    public double getTurretDirection() {
        return turret.getDirectionAngle();
    }

    @Override
    public double getChassisDirection() {
        return chassis.getDirectionAngle();
    }

    @Override
    public void rotateTurret(double toAngle) {
        turret.rotate(toAngle);
    }

    @Override
    public abstract boolean fire();

    @Override
    public double getForwardSpeed() {
        return chassis.getForwardSpeed();
    }

    @Override
    public double getBackwardsSpeed() {
        return chassis.getBackwardsSpeed();
    }

    @Override
    public ITurret getTurret() {
        return turret;
    }

    @Override
    public IChassis getChassis() {
        return chassis;
    }
}
