package classes.gameObjects;

import classes.tanks.parts.ITurret;
import com.sun.istack.internal.Nullable;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/*
Keeps params of Turret logic model
 */
public class GameTurret extends GameObject {
    private ITurret turret;

    public GameTurret(@Nullable Image texture, Point2D point, double directionAngle) {
        super(texture, point, directionAngle);
        Point2D offset = GameConstants.turretOnChassis;
        super.setLeftUpper(point.add(offset.multiply(0.5)));
    }

    public void setTurret(ITurret turret) {
        this.turret = turret;
    }

    public ITurret getTurret() {
        return turret;
    }

    @Override
    public void dispose() {

    }
}
