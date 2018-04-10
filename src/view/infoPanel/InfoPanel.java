package view.infoPanel;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

import javax.swing.*;

public class InfoPanel extends HBox {
    private ProgressBar hpIndicator;
    private ProgressIndicator cooldownIndicator;
    private final int HP_COUNT;
    private static final int TIMER_TICK = 50;

    public InfoPanel(int hpCount) {
        HP_COUNT = hpCount;
        this.cooldownIndicator = new ProgressIndicator(0);
        this.hpIndicator = new ProgressBar(0);
    }

    public void displayCooldown(double duration) {
        new Timer(TIMER_TICK, (e) -> {
            cooldownIndicator.setProgress(cooldownIndicator.getProgress() + TIMER_TICK / duration);
            if (cooldownIndicator.getProgress() >= 1d)
                ((Timer) e.getSource()).stop();
        }).start();
    }

    public boolean decreaseHP(double byValue) {
        hpIndicator.setProgress(hpIndicator.getProgress() - byValue / HP_COUNT);
        if (hpIndicator.getProgress() <= 0d)
            return false;
        return true;
    }
}