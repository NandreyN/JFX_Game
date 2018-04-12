package classes.gameObjects;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class GameObjectDistributor {
    public static List<Box> boxes;

    static {
        boxes = new ArrayList<>();
        boxes.add(new Box(null, new Point2D(500, 500), 100, 100));
    }

    public static List<Box> getBoxes() {
        return boxes;
    }
}
