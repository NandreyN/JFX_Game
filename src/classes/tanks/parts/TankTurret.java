package classes.tanks.parts;

public class TankTurret implements ITurret {
    private final double ROTATION_TIME;
    private double directionAngle;

    public TankTurret(int rotation_time) {
        ROTATION_TIME = rotation_time;
        directionAngle = 0d;
    }

    @Override
    public double getDirectionAngle() {
        return directionAngle;
    }

    @Override
    public double getRotationTime360() {
        return ROTATION_TIME;
    }

    @Override
    public void rotate(double toAngle) {
        directionAngle = toAngle;
    }
}
