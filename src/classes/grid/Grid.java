package classes.grid;

import classes.behavior.INotifiable;
import classes.gameObjects.GameObject;

import java.util.Collection;
import java.util.List;

public class Grid implements INotifiable{
    private List<List<GridCell>> gridCells;
    private Collection<GameObject> gameObjects;
}
