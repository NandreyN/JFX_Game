package classes.tanks;

import classes.tanks.parts.*;

public class CyclicalTank extends AbstractTank {
    private IGun gun;

    public CyclicalTank(ITurret turret, IChassis chassis, int cooldown, int hp, double missileSpeed, double damage) {
        super(turret, chassis, hp);
        gun = new CyclicalGun(cooldown, missileSpeed, damage);
    }

    @Override
    public boolean fire() {
        return gun.fire();
    }

    @Override
    public IGun getGun() {
        return this.gun;
    }

    @Override
    public void dispose() {
        ((CyclicalGun) gun).dispose();
    }
}
