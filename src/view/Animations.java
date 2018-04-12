package view;

import javafx.scene.image.Image;

public class Animations {
    private static Image explosion;

    static {
        makeExplosionAnimation();
    }

    public static Image getExplosionAnimation() {
        return new Image("file:game_textures\\Animation\\explosion_transparent.gif");
    }

    private static void makeExplosionAnimation() {
        explosion = new Image("file:game_textures\\Animation\\explosion_transparent.gif");
    }
}
