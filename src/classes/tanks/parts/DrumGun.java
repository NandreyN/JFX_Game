package classes.tanks.parts;

import com.sun.media.jfxmediaimpl.MediaDisposer;

import javax.swing.*;

public class DrumGun implements IGun, MediaDisposer.Disposable {
    private final int INTERNAL_COOLDOWN;
    private final int EXTERNAL_COOLDOWN;
    private final int CAPACITY;
    private int missilesAvailable;

    private Timer externalTimer, internalTimer;
    private double missileSpeed, damage;

    /**
     * Initializes DrumGun class with its fire properties
     *
     * @param internal_cooldown Cooldown in sec inside drum
     * @param external_cooldown Cooldown of drum in sec
     * @param capacity          Total missiles available in drum in bounds of external cooldown
     * @param missileSpeed      Missile speed game info
     * @param damage            Damage missile causes on hit
     */
    public DrumGun(int internal_cooldown, int external_cooldown, int capacity, double missileSpeed, double damage) {
        INTERNAL_COOLDOWN = internal_cooldown * 1000;
        EXTERNAL_COOLDOWN = external_cooldown * 1000;
        CAPACITY = capacity;
        this.missileSpeed = missileSpeed;
        this.damage = damage;
        missilesAvailable = 0;
        setupCooldownSystem();
        externalTimer.restart();
    }

    /**
     * Setups timers for cooldown system
     */
    private void setupCooldownSystem() {
        externalTimer = new Timer(EXTERNAL_COOLDOWN, (e) -> {
            missilesAvailable = CAPACITY;
            externalTimer.stop();
        });

        internalTimer = new Timer(INTERNAL_COOLDOWN, (e) -> {
            internalTimer.stop();
        });
    }

    /**
     * Emulates fire event
     *
     * @return Was fire succeeded
     */
    @Override
    public boolean fire() {
        if (missilesAvailable > 1 && !internalTimer.isRunning()) {
            missilesAvailable--;
            internalTimer.restart();
            return true;
        } else if (missilesAvailable == 1 && !internalTimer.isRunning()) {
            missilesAvailable = 0;
            externalTimer.restart();
            return true;
        }

        return false;
    }

    /**
     * Is ready for a shot
     *
     * @return Ready for a shot or not
     */
    @Override
    public boolean isReady() {
        return missilesAvailable > 0;
    }

    @Override
    public double getNextCooldown() {
        return (missilesAvailable > 0) ? INTERNAL_COOLDOWN : EXTERNAL_COOLDOWN;
    }

    @Override
    public double getMissileSpeed() {
        return missileSpeed;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    /**
     * @return Get missiles count available in bounds of external cooldown
     */
    public int getMissilesAvailable() {
        return missilesAvailable;
    }


    /**
     * Safe class deleting
     */
    @Override
    public void dispose() {
        if (externalTimer.isRunning())
            externalTimer.stop();
        if (internalTimer.isRunning())
            internalTimer.stop();
    }
}
