package classes.behavior;

import classes.events.GameEvent;
import javafx.event.Event;
import javafx.scene.layout.AnchorPane;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public class EnemyTankManager extends TankManager {
    private List<PlayerTankManager> enemyTanks;
    private static final int ENEMY_COUNT = 2;

    public EnemyTankManager(AnchorPane pane) {
        for (int i = 0; i < ENEMY_COUNT; i++)
            enemyTanks.add(initialize(pane));
    }

    private PlayerTankManager initialize(AnchorPane pane) {
        return new PlayerTankManager(pane);
    }

    @Override
    public void handle(Event event) {

    }

    @Override
    public void handleKeyboardEvent(javafx.scene.input.KeyEvent event) {

    }

    @Override
    public void handleMouseClickEvent(javafx.scene.input.MouseEvent event) {

    }

    @Override
    public void handleMouseMotionEvent(javafx.scene.input.MouseEvent event) {

    }
}
