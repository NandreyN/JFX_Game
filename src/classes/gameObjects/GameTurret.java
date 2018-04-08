package classes.gameObjects;

import classes.tanks.parts.ITurret;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.awt.*;

public class GameTurret extends GameObject {
    private ITurret turret;
    private final Point2D turretCentreOnChassis;

    public GameTurret(@Nullable Image texture, Point2D point) {
        super(texture, point);
        turretCentreOnChassis = new Point2D(point.getX() + getTexture().getWidth() / 10,
                point.getY() + getTexture().getHeight() * 1 / 3);
        super.setPaintCoordinates(turretCentreOnChassis);
    }

    public void setTurret(ITurret turret) {
        this.turret = turret;
    }

    public ITurret getTurret() {
        return turret;
    }

    Point2D getTurretCentreOnChassis() {
        return turretCentreOnChassis;
    }
}
