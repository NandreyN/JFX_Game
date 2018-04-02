package classes.tanks.parts;

import javax.swing.*;

public class CyclicalGun implements IGun, AutoCloseable {
    private final int COOLDOWN_MS;
    private Timer cooldownMaster;
    private boolean ready;

    public CyclicalGun(int cooldown) {
        COOLDOWN_MS = cooldown;
        ready = false;
        setupCooldownSystem();
        cooldownMaster.start();
    }

    /**
     * Setups timer for cooldown. When it ticks, the gun is reloaded
     */
    private void setupCooldownSystem() {
        cooldownMaster = new Timer(COOLDOWN_MS, e -> {
            ready = true;
            cooldownMaster.stop();
        });
    }

    /**
     * Fires gun
     */
    @Override
    public boolean fire() {
        if (!ready)
            return false;

        ready = false;
        if (!cooldownMaster.isRunning())
            cooldownMaster.start();
        return true;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    /**
     * Stops timer. Object can be deleted after that
     */
    @Override
    public void close() {
        if (cooldownMaster.isRunning())
            cooldownMaster.stop();
    }
}
