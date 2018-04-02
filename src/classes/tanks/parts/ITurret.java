package classes.tanks.parts;

public interface ITurret {
    /**
     * @return current direction in radians respectively to Oy axis of
     * Cartesian coordinate system
     */
    double getDirectionAngle();

    /**
     * @return Time in seconds of full rotation
     */
    double getRotationTime360();

    /**
     * @param toAngle angle respectively to positive direction of OY
     *                Cartesian coordinate system axis
     */
    void rotate(double toAngle);
}
