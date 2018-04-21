package classes.tanks.parts;

import com.sun.media.jfxmediaimpl.MediaDisposer;

import javax.swing.*;

public class CyclicalGun implements IGun, MediaDisposer.Disposable {
    private final int COOLDOWN_MS;
    private Timer cooldownMaster;
    private boolean ready;
    private double missileSpeed, damage;

    public CyclicalGun(int cooldown, double missileSpeed, double damage) {
        COOLDOWN_MS = cooldown * 1000;
        ready = false;
        setupCooldownSystem();
        cooldownMaster.start();
        this.missileSpeed = missileSpeed;
        this.damage = damage;
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

    @Override
    public double getNextCooldown() {
        return COOLDOWN_MS;
    }

    @Override
    public double getMissileSpeed() {
        return this.missileSpeed;
    }

    @Override
    public double getDamage() {
        return this.damage;
    }

    /**
     * Stops timer. Object can be deleted after that
     */
    @Override
    public void dispose() {
        if (cooldownMaster.isRunning())
            cooldownMaster.stop();
    }
}
