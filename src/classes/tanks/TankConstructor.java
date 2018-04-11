package classes.tanks;

import classes.tanks.parts.Chassis;
import classes.tanks.parts.IChassis;
import classes.tanks.parts.ITurret;
import classes.tanks.parts.TankTurret;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class TankConstructor {
    public static ITank createDrumTank() {
        ITurret turret = new TankTurret(10);
        IChassis chassis = new Chassis(5, 2, 10);
        ITank tank = new DrumTank(turret, chassis, 3, 10, 5, 1000);
        return tank;
    }

    public static ITank createCyclicalTank() {
        ITurret turret = new TankTurret(20);
        IChassis chassis = new Chassis(3, 1, 20);
        ITank tank = new CyclicalTank(turret, chassis, 8, 1000);
        return tank;
    }

    public static List<ITank> getStockTanksList() {
        throw new NotImplementedException();
    }
}
