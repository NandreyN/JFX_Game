package classes.sound;

import classes.behavior.ResourceDisposer;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Class is responsible for playing game sounds on some events
 * Will be refactored using Events
 */
public class SoundPlayer implements MediaDisposer.Disposable {
    private static final String SHOT_SOUND = "sounds/shot.mp3";
    private static final String MOVE_SOUND = "sounds/move.mp3";
    private static final String EXPLOSION_SOUND = "sounds/explosion.mp3";
    private AudioClip moveSound;

    @Override
    public void dispose() {
        moveSound.stop();
    }

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

    private SoundPlayer()
    {
        ResourceDisposer.getInstance().add(this);
        moveSound = new AudioClip(new File(MOVE_SOUND).toURI().toString());
    }

    /**
     * Play sound
     *
     * @param type Type of sound
     */
    public void play(SoundTypes type) {
        switch (type) {
            case EXPLOSION:
                new MediaPlayer(new Media(new File(EXPLOSION_SOUND).toURI().toString())).play();
                break;
            case MOVE:
                if (!moveSound.isPlaying())
                    moveSound.play();
                break;
            case SHOT:
                new MediaPlayer(new Media(new File(SHOT_SOUND).toURI().toString())).play();
                break;
        }
    }

    /**
     * Stop playing move sound on tank stop
     * Move sound is rather long to sound acceptably
     */
    public void stopMoveSound() {
        moveSound.stop();
    }
}
