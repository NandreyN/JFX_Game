package classes.behavior;

import classes.gameObjects.GameTankInstance;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import static classes.behavior.PlayerTankManager.DELTA_ANGLE;

public class ViewMotionManager {
    public static void rotateTurret(PlayerTankManager manager, double toAngle) {
        manager.turretRotation.setAngle(Math.toDegrees(toAngle) + 90);
        manager.tankInstance.getGameTurret().getTurret().rotate(
                Math.toRadians(manager.turretRotation.getAngle()));
    }

    public static void forwardMove(PlayerTankManager manager) {
        double a = Math.toRadians(manager.chassisRotation.getAngle()),
                forwardSpeed = manager.tankInstance.getGameChassis().getChassis().getForwardSpeed();
        moveTankImage(manager, forwardSpeed, a);
    }

    public static void turnLeft(PlayerTankManager manager) {
        manager.tankInstance.turnLeft(DELTA_ANGLE);
        manager.chassisRotation.setAngle(manager.chassisRotation.getAngle() - Math.toDegrees(DELTA_ANGLE));
        manager.chassisRotation.setAngle(normalizeAngle(manager.chassisRotation.getAngle()));
    }

    public static void backwardsMove(PlayerTankManager manager) {

        double ang = Math.PI + Math.toRadians(manager.chassisRotation.getAngle()),
                backwardsSpeed = manager.tankInstance.getGameChassis().getChassis().getBackwardsSpeed();
        moveTankImage(manager, backwardsSpeed, ang);
    }

    public static void turnRight(PlayerTankManager manager) {
        manager.tankInstance.turnRight(DELTA_ANGLE);
        manager.chassisRotation.setAngle(manager.chassisRotation.getAngle() + Math.toDegrees(DELTA_ANGLE));
        manager.chassisRotation.setAngle(normalizeAngle(manager.chassisRotation.getAngle()));
    }

    private static double normalizeAngle(double angle) {
        double newAngle = angle;
        while (newAngle <= -180) newAngle += 360;
        while (newAngle > 180) newAngle -= 360;
        return newAngle;
    }

    public static void fire(PlayerTankManager manager) {
        manager.tankInstance.fire();
    }

    private static void createMovementPath(Node node, Point2D oldPoint, Point2D newPoint) {
        Path path = new Path();

        path.getElements().add(new MoveTo(oldPoint.getX(), oldPoint.getY()));
        path.getElements().add(new LineTo(newPoint.getX(), newPoint.getY()));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(100));
        pathTransition.setNode(node);
        pathTransition.setPath(path);
        pathTransition.setOrientation(PathTransition.OrientationType.NONE);
        pathTransition.setAutoReverse(false);
        pathTransition.play();
    }

    private static void moveTankImage(PlayerTankManager manager, double speed, double a) {
        double dx = Math.abs(speed * Math.sin(a));
        double dy = Math.abs(speed * Math.cos(a));

        if (-Math.PI / 2 <= a && a <= 0) {
        } else if (-Math.PI <= a && a <= -Math.PI / 2) {
            dy = -dy;
        } else if (Math.PI / 2 <= a && a <= Math.PI) {
            dx = -dx;
            dy = -dy;
        } else if (0 <= a && a <= Math.PI / 2) {
            dx = -dx;
        }


        Point2D oldChassis = manager.tankInstance.getGameChassis().getPaintCoordinates();
        Point2D newChassis = new Point2D(oldChassis.getX() + dx, oldChassis.getY() + dy);
        manager.tankInstance.getGameChassis().setPaintCoordinates(newChassis);
        createMovementPath(manager.chassisView, new Point2D(oldChassis.getX() + 33, oldChassis.getY() + 62),
                new Point2D(newChassis.getX() + 33, newChassis.getY() + 62));

        Point2D oldTurret = manager.tankInstance.getGameTurret().getPaintCoordinates();
        Point2D newTurret = new Point2D(oldTurret.getX() + dx, oldTurret.getY() + dy);
        manager.tankInstance.getGameTurret().setPaintCoordinates(newTurret);
        createMovementPath(manager.turretView, new Point2D(oldTurret.getX() + 26, oldTurret.getY() + 70),
                new Point2D(newTurret.getX() + 26, newTurret.getY() + 70));
    }

}
