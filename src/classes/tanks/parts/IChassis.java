package classes.tanks.parts;

public interface IChassis {
    // Ходовая
    double getForwardSpeed();

    double getBackwardsSpeed();

    double getRotationTime360();

    double getDirectionAngle();

    /**
     * @param absDeltaAngle rotation angle respectively OY positive direction in
     *                      Cartesian coordinate system. Rotation clockwise
     */
    void turnLeft(double absDeltaAngle);

    /**
     * @param absDeltaAngle rotation angle respectively OY positive direction in
     *                      Cartesian coordinate system. Rotation clockwise
     */
    void turnRight(double absDeltaAngle);
}
