package classes.gameObjects;

import classes.tanks.parts.IChassis;
import com.sun.istack.internal.NotNull;
import javafx.scene.image.Image;

import java.awt.*;

public class GameChassis extends GameObject {
    private IChassis chassis;

    public GameChassis(@NotNull Image texture, Point centre, double dispHeight, double dispWidth) {
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
