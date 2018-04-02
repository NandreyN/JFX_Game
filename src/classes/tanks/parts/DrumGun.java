package classes.tanks.parts;

import javax.swing.*;

public class DrumGun implements IGun, AutoCloseable {
    private final int INTERNAL_COOLDOWN;
    private final int EXTERNAL_COOLDOWN;
    private final int CAPACITY;
    private int missilesAvailable;

    private Timer externalTimer, internalTimer;

    public DrumGun(int internal_cooldown, int external_cooldown, int capacity) {
        INTERNAL_COOLDOWN = internal_cooldown;
        EXTERNAL_COOLDOWN = external_cooldown;
        CAPACITY = capacity;

        missilesAvailable = 0;
        setupCooldownSystem();
        externalTimer.restart();
    }

    private void setupCooldownSystem() {
        externalTimer = new Timer(EXTERNAL_COOLDOWN, (e) -> {
            missilesAvailable = CAPACITY;
            externalTimer.stop();
        });

        internalTimer = new Timer(INTERNAL_COOLDOWN, (e) -> {
            internalTimer.stop();
        });
    }

    @Override
    public boolean fire() {
        if (missilesAvailable > 1 && !internalTimer.isRunning()) {
            missilesAvailable--;
            internalTimer.restart();
            return true;
        } else if (missilesAvailable == 1 && !internalTimer.isRunning()) {
            missilesAvailable = 0;
            externalTimer.restart();
        }

        return false;
    }

    @Override
    public boolean isReady() {
        return missilesAvailable > 0;
    }

    @Override
    public void close() {
        if (externalTimer.isRunning())
            externalTimer.stop();
        if (internalTimer.isRunning())
            internalTimer.stop();
    }

    public int getMissilesAvailable() {
        return missilesAvailable;
    }
}
