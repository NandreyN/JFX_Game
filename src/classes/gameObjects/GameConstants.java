package classes.gameObjects;

import javafx.geometry.Point2D;
import javafx.util.Pair;

/**
 * Constants used by View component
 */
public class GameConstants {
    public final static Point2D missileCentre = new Point2D(5d, 5d);
    public final static Point2D chassisCentre = new Point2D(31.5, 61.5);
    public final static Point2D turretConnectionPoint = new Point2D(26.5, 30);
    public final static Point2D turretOnChassis = new Point2D(31, 66);
    public final static Point2D turretTextureCentre = new Point2D(26.5, 70);
    public final static Point2D missileStartRelativeTurret = new Point2D(26.5, 134);
    public final static Pair<Double, Double> gunFlameSize = new Pair<>(20d,20d);
    public final static double playerMissileSpeed = 6;
}
