package classes.tanks.parts;

/**
 * Emulates chassis behavior, each type of chassis has it`s own
 * parameters of speed
 */
public class Chassis implements IChassis {
    private final double FORWARD_SPEED, BACKWARDS_SPEED, ROTATION_SECONDS;

    public Chassis(double forward_speed, double backwards_speed, double rotation_seconds) {
        FORWARD_SPEED = forward_speed;
        BACKWARDS_SPEED = backwards_speed;
        ROTATION_SECONDS = rotation_seconds;
    }

    /**
     * @return get forward speed
     */
    @Override
    public double getForwardSpeed() {
        return FORWARD_SPEED;
    }

    /**
     * @return get reverse speed
     */
    @Override
    public double getBackwardsSpeed() {
        return BACKWARDS_SPEED;
    }

    /**
     * @return get time which chassis takes to make full rotation
     * in seconds
     */
    @Override
    public double getRotationTime360() {
        return ROTATION_SECONDS;
    }
}
