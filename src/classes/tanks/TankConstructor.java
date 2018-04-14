package classes.tanks;

import classes.tanks.parts.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class TankConstructor {
    public static ITank createDrumTank(double internalCooldown, double externalCooldown, int capacity,
                                       double hp, double speedF, double speedB, boolean isSAU) {
        ITurret turret = (isSAU) ? new SAUTurret() : new TankTurret(10);
        IChassis chassis = new Chassis(speedF, speedB, 10);
        return new DrumTank(turret, chassis, (int) internalCooldown, (int) externalCooldown, capacity, (int) hp);
    }

    public static ITank createCyclicalTank(double coolDown,
                                           double hp, double speedF, double speedB, boolean isSAU) {
        ITurret turret = (isSAU) ? new SAUTurret() : new TankTurret(20);
        IChassis chassis = new Chassis(speedF, speedB, 20);
        return new CyclicalTank(turret, chassis, (int) coolDown, (int) hp);
    }
}
