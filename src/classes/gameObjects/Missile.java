package classes.gameObjects;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.awt.*;

public class Missile extends GameObject {
    public Missile(@Nullable Image texture, Point2D centre) {
        super(texture, centre);
    }


}
