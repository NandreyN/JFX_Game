package classes.levels;

import javafx.scene.image.Image;

/**
 * Class for getting textures from resource files
 */
public class TextureLoader {
    private static final String TANK_SKINS = "game_textures/Cut/";
    private static final String BOX_TEXTURE = "game_textures/Boxes/box.jpg";

    /**
     * Get new turret texture image with the id provided
     *
     * @param id Texture ID
     * @return Texture itself
     */
    public static Image getTurretTexture(int id) {
        return new Image("file:" + TANK_SKINS + "turret_" + id + ".png");
    }

    /**
     * Get new chassis texture image with the id provided
     *
     * @param id Texture ID
     * @return Texture itself
     */
    public static Image getChassisTexture(int id) {
        return new Image("file:" + TANK_SKINS + "chassis_" + id + ".png");
    }

    /**
     * Get new box texture
     *
     * @return Texture itself
     */
    public static Image getBoxTexture() {
        return new Image("file:" + BOX_TEXTURE);
    }
}
