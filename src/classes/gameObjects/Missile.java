package classes.gameObjects;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.awt.*;

public class Missile extends GameObject {
    private double speed;
    private int srcTankId;
    private final double DAMAGE;
    private GameTank tankHit;

    public Missile(@Nullable Image texture, Point2D centre, double speed, int srcTankId, double damage) {
        super(texture, centre);
        this.speed = speed;
        this.srcTankId = srcTankId;
        this.DAMAGE = damage;
    }


    public double getSpeed() {
        return speed;
    }

    public int getSrcTankId() {
        return srcTankId;
    }

    public double getDamage() {
        return DAMAGE;
    }

    public void setTankHit(GameTank tankHit) {
        this.tankHit = tankHit;
    }

    public GameTank getTankHit() {
        return tankHit;
    }
}
