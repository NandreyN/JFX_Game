package classes.tanks;

import classes.tanks.parts.DrumGun;
import classes.tanks.parts.IChassis;
import classes.tanks.parts.IGun;
import classes.tanks.parts.ITurret;

public class DrumTank extends AbstractTank {
    private IGun gun;

    public DrumTank(ITurret turret, IChassis chassis, int internalCoolDown,
                    int externalCooldown, int capacity, int hp, double missileSpeed, double damage) {
        super(turret, chassis, hp);
        gun = new DrumGun(internalCoolDown, externalCooldown, capacity, missileSpeed, damage);
    }

    @Override
    public boolean fire() {
        return gun.fire();
    }

    @Override
    public IGun getGun() {
        return gun;
    }

    @Override
    public void dispose() {
        ((DrumGun) gun).dispose();
    }
}
