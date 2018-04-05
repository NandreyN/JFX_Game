package classes.gameObjects;

import classes.tanks.parts.ITurret;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.scene.image.Image;

import java.awt.*;

public class GameTurret extends GameObject {
    private ITurret turret;
    private final Point turretCentreOnChassis;

    public GameTurret(@Nullable Image texture, Point point, double dispHeight, double dispWidth) {
        super(texture, point,
                dispHeight, dispWidth);
        turretCentreOnChassis = new Point((int) (point.x + getTexture().getWidth() / 10),
                (int) (point.y + getTexture().getHeight() * 1/3));
        super.setPaintCoordinates(turretCentreOnChassis);
    }

    @Override
    public double getDisplayedHeight() {
        return 0;
    }

    @Override
    public double getDisplayedWidth() {
        return 0;
    }

    public void setTurret(ITurret turret) {
        this.turret = turret;
    }

    public ITurret getTurret() {
        return turret;
    }
}
