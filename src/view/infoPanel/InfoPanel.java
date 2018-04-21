package view.infoPanel;

import classes.behavior.ResourceDisposer;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * UI component for displaying tank cooldown and current HP
 */
public class InfoPanel extends HBox implements ITankStateUI, MediaDisposer.Disposable {
    private ProgressBar hpIndicator;
    private ProgressIndicator cooldownIndicator;
    private final int HP_COUNT;
    private static final int TIMER_TICK = 50;
    private Timeline cooldownTimeline;

    /**
     * @param hpCount Total initial HP count of PLayer`s tank
     */
    public InfoPanel(int hpCount) {
        HP_COUNT = hpCount;
        this.cooldownIndicator = new ProgressIndicator(0);
        this.hpIndicator = new ProgressBar(1);
        this.getChildren().addAll(cooldownIndicator, hpIndicator);
        ResourceDisposer.getInstance().add(this);
    }

    /**
     * Reset UI elements
     */
    public void reset() {
        if (cooldownTimeline != null)
            this.cooldownTimeline.stop();
        this.cooldownIndicator.setProgress(0);
        this.hpIndicator.setProgress(1);
    }

    /**
     * Start animating cooldown progress
     *
     * @param duration Cooldown duration
     */
    public void cooldown(double duration) {
        cooldownIndicator.setProgress(0);
        cooldownTimeline = new Timeline(new KeyFrame(Duration.millis(TIMER_TICK),
                event -> cooldownIndicator.setProgress(cooldownIndicator.getProgress() + TIMER_TICK / duration)));
        cooldownTimeline.setOnFinished(event -> {
            if (cooldownIndicator.getProgress() < 1d)
                cooldownTimeline.play();
        });
        cooldownTimeline.setCycleCount(1);
        cooldownTimeline.play();
    }

    /**
     * Decrease HP displayed by value provided
     *
     * @param byValue HP damages
     * @return Is tank still alive or not
     */
    @Override
    public boolean decreaseHP(double byValue) {
        hpIndicator.setProgress(hpIndicator.getProgress() - byValue / HP_COUNT);
        return !(hpIndicator.getProgress() <= 0d);
    }

    @Override
    public void dispose() {
        if (cooldownTimeline != null)
            cooldownTimeline.stop();
    }
}