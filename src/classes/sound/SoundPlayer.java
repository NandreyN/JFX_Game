package classes.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {
    private static final String SHOT_SOUND = "sounds/shot.mp3";
    private static final String MOVE_SOUND = "sounds/move.mp3";
    private static final String EXPLOSION_SOUND = "sounds/explosion.mp3";
    private AudioClip moveSound;

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
        moveSound = new AudioClip(new File(MOVE_SOUND).toURI().toString());
    }

    public void play(SoundTypes type) {
        switch (type) {
            case EXPLOSION:
                new MediaPlayer(new Media(new File(EXPLOSION_SOUND).toURI().toString())).play();
                break;
            case MOVE:
                moveSound.play();
                break;
            case SHOT:
                new MediaPlayer(new Media(new File(SHOT_SOUND).toURI().toString())).play();
                break;
        }
    }
}
