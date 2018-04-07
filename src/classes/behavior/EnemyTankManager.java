package classes.behavior;

import classes.events.GameEvent;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EnemyTankManager extends TankManager {
    private List<PlayerTankManager> enemyTanks;
    private static final int ENEMY_COUNT = 1;

    public EnemyTankManager(AnchorPane pane) {
        enemyTanks = new ArrayList<>();
        for (int i = 0; i < ENEMY_COUNT; i++)
            enemyTanks.add(initialize(pane, new Point2D(300, 300), 90));
    }

    private PlayerTankManager initialize(AnchorPane pane, Point2D startPos, double orientAngle) {
        return new PlayerTankManager(pane, startPos, orientAngle, 2);
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
