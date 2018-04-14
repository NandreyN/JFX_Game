package classes.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {
    private static final String SHOT_SOUND = "file:sounds/shot.mp3";
    private static final String MOVE_SOUND = "file:sounds/move.mp3";
    private static final String EXPLOSION_SOUND = "file:sounds/explosion.mp3";

    public enum SoundTypes {
        SHOT, MOVE, EXPLOSION
    }

    private static SoundPlayer soundPlayer;
    private Map<SoundTypes, MediaPlayer> soundMap;

    public static SoundPlayer getInstance() {
        if (soundPlayer == null)
            soundPlayer = new SoundPlayer();
        return soundPlayer;
    }

    private SoundPlayer() {
        fillSoundMap();
    }

    private void fillSoundMap() {
        soundMap = new HashMap<>();
        soundMap.put(SoundTypes.SHOT, new MediaPlayer(new Media(new File(SHOT_SOUND).toURI().toString())));
        soundMap.put(SoundTypes.MOVE, new MediaPlayer(new Media(new File(MOVE_SOUND).toURI().toString())));
        soundMap.put(SoundTypes.EXPLOSION, new MediaPlayer(new Media(new File(EXPLOSION_SOUND).toURI().toString())));
    }
}
