package classes.tanks;

import classes.tanks.parts.*;

public class CyclicalTank extends AbstractTank {
    private IGun gun;

    public CyclicalTank(ITurret turret, IChassis chassis, int cooldown) {
        super(turret, chassis);
        gun = new CyclicalGun(cooldown);
    }

    @Override
    public boolean fire() {
        return gun.fire();
    }
}
