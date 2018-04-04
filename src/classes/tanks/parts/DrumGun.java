package classes.tanks.parts;

import javax.swing.*;

public class DrumGun implements IGun, AutoCloseable {
    private final int INTERNAL_COOLDOWN;
    private final int EXTERNAL_COOLDOWN;
    private final int CAPACITY;
    private int missilesAvailable;

    private Timer externalTimer, internalTimer;

    /**
     * Initializes DrumGun class with its fire properties
     * @param internal_cooldown Cooldown in ms inside drum
     * @param external_cooldown Cooldown of drum in ms
     * @param capacity Total missiles available in drum in bounds of external cooldown
     */
    public DrumGun(int internal_cooldown, int external_cooldown, int capacity) {
        INTERNAL_COOLDOWN = internal_cooldown;
        EXTERNAL_COOLDOWN = external_cooldown;
        CAPACITY = capacity;

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
        }

        return false;
    }

    /**
     * Is ready for a shot
     * @return Ready for a shot or not
     */
    @Override
    public boolean isReady() {
        return missilesAvailable > 0;
    }

    /**
     * Safe class deleting
     */
    @Override
    public void close() {
        if (externalTimer.isRunning())
            externalTimer.stop();
        if (internalTimer.isRunning())
            internalTimer.stop();
    }

    /**
     *
     * @return Get missiles count available in bounds of external cooldown
     */
    public int getMissilesAvailable() {
        return missilesAvailable;
    }
}