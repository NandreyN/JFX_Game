package classes.tanks;

import classes.tanks.parts.IChassis;
import classes.tanks.parts.IGun;
import classes.tanks.parts.ITurret;

public interface ITank {
    enum TankType {
        CYCLICAL, DRUM
    }

    ;

    double getHP();

    void decreaseHP(double byValue);

    boolean isAlive();

    double getFullHPValue();

    void turnLeft(double absDeltaAngle);

    void turnRight(double absDeltaAngle);

    double getTurretDirection();

    double getChassisDirection();

    void rotateTurret(double toAngle);

    boolean fire();

    double getForwardSpeed();

    double getBackwardsSpeed();

    ITurret getTurret();

    IChassis getChassis();

    IGun getGun();
}
