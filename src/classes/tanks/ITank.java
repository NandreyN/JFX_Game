package classes.tanks;

public interface ITank {
    enum TankType {
        CYCLICAL, DRUM
    }

    ;

    void turnLeft(double absDeltaAngle);

    void turnRight(double absDeltaAngle);

    double getTurretDirection();

    double getChassisDirection();

    void rotateTurret(double toAngle);

    boolean fire();

    double getForwardSpeed();

    double getBackwardsSpeed();
}
