package classes.tanks;

import classes.tanks.parts.*;

public class CyclicalTank extends AbstractTank {
    private IGun gun;

    public CyclicalTank(ITurret turret, IChassis chassis, int cooldown, int hp) {
        super(turret, chassis,hp);
        gun = new CyclicalGun(cooldown);
    }

    @Override
    public boolean fire() {
        return gun.fire();
    }
}
