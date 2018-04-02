package classes.tanks.parts;

/**
 * Motionless turret, but still has direction angle
 * equals to chassis direction angle
 */
public class SAUTurret implements ITurret {
    private double directionAngle;

    public SAUTurret() {
        directionAngle = 0d;
    }

    @Override
    public double getDirectionAngle() {
        return directionAngle;
    }

    /**
     * @return Always zero
     */
    @Override
    public double getRotationTime360() {
        return 0;
    }

    @Deprecated
    @Override
    public void rotate(double toAngle) {
    }
}
