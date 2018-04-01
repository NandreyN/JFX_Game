package classes.gameObjects;

import classes.behavior.INotifiable;
import javafx.scene.image.Image;

import java.awt.*;

public class GameObject {
    private INotifiable listener;

    private Point coordinates;
    private Image texture;
}
