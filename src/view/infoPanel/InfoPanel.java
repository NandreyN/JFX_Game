package view.infoPanel;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

import javax.swing.*;

public class InfoPanel extends HBox implements ITankStateUI {
    private ProgressBar hpIndicator;
    private ProgressIndicator cooldownIndicator;
    private final int HP_COUNT;
    private static final int TIMER_TICK = 50;
    private Timer cooldownTimer;


    public InfoPanel(int hpCount) {
        HP_COUNT = hpCount;
        this.cooldownIndicator = new ProgressIndicator(0);
        this.hpIndicator = new ProgressBar(1);
        this.getChildren().addAll(cooldownIndicator, hpIndicator);
    }

    public void reset() {
        if (cooldownTimer != null && cooldownTimer.isRunning())
            this.cooldownTimer.stop();
        this.cooldownIndicator.setProgress(0);
        this.hpIndicator.setProgress(1);
    }

    public void cooldown(double duration) {
        cooldownIndicator.setProgress(0);
        cooldownTimer = new Timer(TIMER_TICK, (e) -> {
            cooldownIndicator.setProgress(cooldownIndicator.getProgress() + TIMER_TICK / duration);
            if (cooldownIndicator.getProgress() >= 1d)
                ((Timer) e.getSource()).stop();
        });
        cooldownTimer.start();
    }

    public boolean decreaseHP(double byValue) {
        hpIndicator.setProgress(hpIndicator.getProgress() - byValue / HP_COUNT);
        if (hpIndicator.getProgress() <= 0d)
            return false;
        return true;
    }
}