package classes.gameObjects;

import classes.tanks.parts.IChassis;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.beans.InvalidationListener;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.awt.*;

public class GameChassis extends GameObject {
    private IChassis chassis;

    public GameChassis(@Nullable Image texture, Point2D centre) {
        super(texture, centre);
    }

    public void setChassis(IChassis chassis) {
        this.chassis = chassis;
    }

    public IChassis getChassis() {
        return chassis;
    }

}
