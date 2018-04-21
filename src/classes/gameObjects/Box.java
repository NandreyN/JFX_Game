package classes.gameObjects;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

/**
 * Class represents Boxes on game scene.
 * Kind of shelter
 */
public class Box extends GameObject {
    private double height, width;

    public Box(Image texture, Point2D centre, double width, double height, double directionAngle) {
        super(texture, centre, directionAngle);
        this.height = height;
        this.width = width;
    }

    @Override
    public double getDisplayedWidth() {
        return this.width;
    }

    @Override
    public double getDisplayedHeight() {
        return this.height;
    }

    @Override
    public double getTextureHeight() {
        return getDisplayedHeight();
    }

    @Override
    public double getTextureWidth() {
        return getDisplayedWidth();
    }

    @Override
    public void dispose() {
    }
}
