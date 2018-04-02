package classes.tanks;

import classes.tanks.parts.DrumGun;
import classes.tanks.parts.IChassis;
import classes.tanks.parts.IGun;
import classes.tanks.parts.ITurret;

public class DrumTank extends AbstractTank {
    private IGun gun;

    public DrumTank(ITurret turret, IChassis chassis, int internalCoolDown,
                    int externalCooldown, int capacity) {
        super(turret, chassis);
        gun = new DrumGun(internalCoolDown, externalCooldown, capacity);
    }

    @Override
    public boolean fire() {
        return gun.fire();
    }
}
