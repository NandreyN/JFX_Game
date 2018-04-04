package classes.behavior;

import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.List;

public class UserInputHandler {
    private Canvas canvas;
    private TankManager playerTankManager;

    public UserInputHandler(Canvas canvas) {
        if (canvas == null)
            throw new NullPointerException("canvas");
        this.canvas = canvas;
        playerTankManager = new PlayerTankManager();

        setupKeyboardListener();
        setupMouseClickListeners();
        setupMouseMotionListeners();
    }

    private void setupKeyboardListener() {
        canvas.setOnKeyPressed(playerTankManager);
    }

    private void setupMouseClickListeners() {
        canvas.setOnMouseClicked(playerTankManager);
    }

    private void setupMouseMotionListeners() {
        canvas.setOnMouseMoved(playerTankManager);
    }
}
