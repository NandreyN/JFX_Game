package classes.gameObjects;

import classes.behavior.INotifiable;
import classes.behavior.ViewMotionManager;
import com.sun.istack.internal.Nullable;
import javafx.beans.InvalidationListener;
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
    private Point2D paintCoordinates;
    private Image texture;
    private double displayedHeight, displayedWidth;

    public GameObject(@Nullable Image texture, Point2D centre, double dispHeight, double dispWidth) {
        this.texture = texture;
        this.paintCoordinates = centre;
        this.displayedHeight = dispHeight;
        this.displayedWidth = dispWidth;

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

    public Image getTexture() {
        return texture;
    }

    public Point2D getPaintCoordinates() {
        return paintCoordinates;
    }

    public void setPaintCoordinates(Point2D point) {
        this.paintCoordinates = point;
        this.setChanged();
        this.notifyObservers(this);
    }

    private void bindToMotionManager() {
        ViewMotionManager.getInstance().register(this);
        this.addObserver(ViewMotionManager.getInstance());
    }
}
