package classes.gameObjects;

import com.sun.istack.internal.NotNull;
import javafx.scene.image.Image;

import java.awt.*;

public class Missile extends GameObject {
    public Missile(@NotNull Image texture, Point centre, double dispHeight, double dispWidth) {
        super(texture, centre, dispHeight, dispWidth);
    }

    @Override
    public double getDisplayedHeight() {
        return 0;
    }

    @Override
    public double getDisplayedWidth() {
        return 0;
    }
}
