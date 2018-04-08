package classes.gameObjects;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.awt.*;

public class Missile extends GameObject {
    private double speed;
    private int srcTankId;

    public Missile(@Nullable Image texture, Point2D centre, double speed, int srcTankId) {
        super(texture, centre);
        this.speed = speed;
        this.srcTankId = srcTankId;
    }


    public double getSpeed() {
        return speed;
    }

    public int getSrcTankId() {
        return srcTankId;
    }
}
