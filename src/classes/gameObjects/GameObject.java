package classes.gameObjects;

import classes.behavior.INotifiable;
import classes.behavior.ViewMotionManager;
import com.sun.istack.internal.Nullable;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import javax.media.j3d.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Represents game object that invokes model of object
 * and it`s appearance
 */

public abstract class GameObject extends Observable {
    private static int INST_COUNT = 0;

    private Point2D paintCoordinates;
    private Image texture;
    double displayedHeight, displayedWidth;
    private int id;
    private double directionAngle;
    private boolean isValid;

    public GameObject(@Nullable Image texture, Point2D centre) {
        this.texture = texture;
        this.paintCoordinates = centre;
        if (texture != null) {
            this.displayedHeight = texture.getHeight();
            this.displayedWidth = texture.getWidth();
        }
        directionAngle = 0;
        id = ++INST_COUNT;
        bindToMotionManager();
    }

    /**
     * @return get original texture Width in pixels
     */
    public double getTextureWidth() {
        return texture.getWidth();
    }

    /**
     * @return get original texture height in pixels
     */
    public double getTextureHeight() {
        return texture.getHeight();
    }

    /**
     * @return get texture height in pixels on grid
     */
    public double getDisplayedHeight() {
        return displayedHeight;
    }

    /**
     * @return get texture width in pixels on grid
     */
    public double getDisplayedWidth() {
        return displayedWidth;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GameObject))
            return false;
        if (o == this)
            return true;
        return getId() == ((GameObject) o).getId();
    }

    public Image getTexture() {
        return texture;
    }

    public Point2D getPaintCoordinates() {
        return paintCoordinates;
    }

    public void setPaintCoordinates(Point2D point) {
        Point2D oldCoordinates = paintCoordinates;

        this.paintCoordinates = point;
        this.setChanged();
        this.notifyObservers(this);
        if (!isValid())
            this.paintCoordinates = oldCoordinates;
    }

    private void bindToMotionManager() {
        ViewMotionManager.getInstance().register(this);
        this.addObserver(ViewMotionManager.getInstance());
    }

    public int getId() {
        return id;
    }

    public double getDirectionAngle() {
        return directionAngle;
    }

    public void setDirectionAngle(double angle) {
        this.directionAngle = angle;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
