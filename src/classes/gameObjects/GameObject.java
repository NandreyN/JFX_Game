package classes.gameObjects;

import classes.behavior.ViewMotionManager;
import com.sun.istack.internal.Nullable;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.Observable;


/**
 * Represents game object that invokes model of object
 * and it`s appearance
 */

public abstract class GameObject extends Observable implements MediaDisposer.Disposable{
    private static int INST_COUNT = 0;

    private Point2D leftUpper;
    private Image texture;
    double displayedHeight, displayedWidth;
    private int id;
    private double directionAngle;
    private boolean isValid;

    public GameObject(@Nullable Image texture, Point2D centre, double directionAngle) {
        this.texture = texture;
        this.leftUpper = centre;
        if (texture != null) {
            this.displayedHeight = texture.getHeight();
            this.displayedWidth = texture.getWidth();
        }
        this.directionAngle = directionAngle;
        id = ++INST_COUNT;
        bindToMotionManager();
        isValid = true;
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

    /**
     * Get texture of GameObject
     *
     * @return Texture
     */
    public Image getTexture() {
        return texture;
    }

    /**
     * Get left-upper draw coordinate
     *
     * @return Left-Upper coordinate
     */
    public Point2D getLeftUpper() {
        return leftUpper;
    }

    /**
     * Tries to apply left-upper paint coordinate.
     * Success if and only if no overlaps detected.
     * Redirects response to ViewMotionManager
     *
     * @param point Left-Upper draw point to set
     */
    public void setLeftUpper(Point2D point) {
        Point2D oldCoordinates = leftUpper;

        this.leftUpper = point;
        this.setChanged();
        this.notifyObservers(this);
        if (!isValid())
            this.leftUpper = oldCoordinates;
    }

    /**
     * Binds GameObject with MotionManager
     * Created for adding GameObject to object track list
     */
    private void bindToMotionManager() {
        ViewMotionManager.getInstance().register(this);
        this.addObserver(ViewMotionManager.getInstance());
    }

    /**
     * Get object unique ID
     *
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return direction angle of that GameObject
     */
    public double getDirectionAngle() {
        return directionAngle;
    }

    /*
    Setter for direction angle
     */
    public void setDirectionAngle(double angle) {
        this.directionAngle = angle;
    }

    /**
     * @return true if no intersection detected,otherwise false
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Set valid property
     *
     * @param valid Is position valid
     */
    public void setValid(boolean valid) {
        isValid = valid;
    }
}
