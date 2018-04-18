package classes.gameObjects;

import classes.tanks.parts.IChassis;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.beans.InvalidationListener;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.awt.*;

/**
 * Class for keeping params of tank chassis
 */
public class GameChassis extends GameObject {
    private IChassis chassis;

    public GameChassis(@Nullable Image texture, Point2D centre, double directionAngle) {
        super(texture, centre, directionAngle);
    }

    /**
     * @param chassis Chassis logic model
     */
    public void setChassis(IChassis chassis) {
        this.chassis = chassis;
    }

    /**
     * @return Chassis logic model
     */
    public IChassis getChassis() {
        return chassis;
    }

}
