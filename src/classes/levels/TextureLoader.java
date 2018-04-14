package classes.levels;

import javafx.scene.image.Image;

public class TextureLoader {
    private static final String TANK_SKINS = "game_textures/Cut/";
    private static final String BOX_TEXTURE = "game_textures/Boxes/box.jpg";

    public static Image getTurretTexture(int id) {
        return new Image("file:" + TANK_SKINS + "turret_" + id + ".png");
    }

    public static Image getChassisTexture(int id) {
        return new Image("file:" + TANK_SKINS + "chassis_" + id + ".png");
    }

    public static Image getBoxTexture() {
        return new Image("file:" + BOX_TEXTURE);
    }
}
