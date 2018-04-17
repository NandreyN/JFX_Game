package classes.gameObjects;

import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Missile, main and only damage object
 */
public class Missile extends GameObject {
    private double speed;
    private int srcTankId;
    private final double DAMAGE;
    private GameObject objectHit;

    public Missile(@Nullable Image texture, Point2D centre, double speed, int srcTankId, double damage) {
        super(texture, centre);
        this.speed = speed;
        this.srcTankId = srcTankId;
        this.DAMAGE = damage;
    }

    /**
     * @return Missile speed
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Gets tank source id
     *
     * @return ID
     */
    public int getSrcTankId() {
        return srcTankId;
    }

    /**
     * @return Missile damage in HP
     */
    public double getDamage() {
        return DAMAGE;
    }

    /**
     * Keeps object hit by the missile
     *
     * @param objectHit
     */
    public void setObjectHit(GameObject objectHit) {
        this.objectHit = objectHit;
    }

    public GameObject getObjectHit() {
        return objectHit;
    }
}
