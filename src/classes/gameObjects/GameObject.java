package classes.gameObjects;

import classes.behavior.INotifiable;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.scene.image.Image;

import java.awt.*;

/**
 * Represents game object that invokes model of object
 * and it`s appearance
 */

public abstract class GameObject {
    private INotifiable listener;

    private Point paintCoordinates;
    private Image texture;
    private double displayedHeight, displayedWidth;

    public GameObject(@Nullable Image texture, Point centre, double dispHeight, double dispWidth) {
        this.texture = texture;
        this.paintCoordinates = centre;
        this.displayedHeight = dispHeight;
        this.displayedWidth = dispWidth;
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
    public abstract double getDisplayedHeight();

    /**
     * @return get texture width in pixels on grid
     */
    public abstract double getDisplayedWidth();

    /**
     * Notifies drawer to paint model on the grid
     */
    public void paint() {
        if (listener == null)
            throw new NullPointerException("Listener in not set");
        listener.notify();
    }

    public Image getTexture() {
        return texture;
    }

    public Point getPaintCoordinates() {
        return paintCoordinates;
    }

    public void setPaintCoordinates(Point point) {
        this.paintCoordinates = point;
    }
}
