package classes.behavior;

import classes.gameObjects.GameObject;
import classes.gameObjects.GameTankInstance;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static classes.behavior.PlayerTankManager.DELTA_ANGLE;

public class ViewMotionManager implements Observer {
    private List<GameTankInstance> observables = new ArrayList<>();
    private static ViewMotionManager instance;

    public static synchronized ViewMotionManager getInstance() {
        if (instance == null) {
            instance = new ViewMotionManager();
        }
        return instance;
    }

    public void rotateTurret(PlayerTankManager manager, double toAngle) {
        manager.turretRotation.angleProperty().set(Math.toDegrees(toAngle) + 90);
        manager.tankInstance.getGameTurret().getTurret().rotate(
                Math.toRadians(manager.turretRotation.angleProperty().get()));
    }

    public void forwardMove(PlayerTankManager manager) {
        double a = Math.toRadians(manager.chassisRotation.angleProperty().get()),
                forwardSpeed = manager.tankInstance.getGameChassis().getChassis().getForwardSpeed();
        moveTankImage(manager, forwardSpeed, a);
    }

    public void turnLeft(PlayerTankManager manager) {
        manager.tankInstance.turnLeft(DELTA_ANGLE);
        manager.chassisRotation.angleProperty().set(manager.chassisRotation.angleProperty().get() - Math.toDegrees(DELTA_ANGLE));
        manager.chassisRotation.angleProperty().set(normalizeAngle(manager.chassisRotation.angleProperty().get()));
    }

    public void backwardsMove(PlayerTankManager manager) {

        double ang = Math.PI + Math.toRadians(manager.chassisRotation.angleProperty().get()),
                backwardsSpeed = manager.tankInstance.getGameChassis().getChassis().getBackwardsSpeed();
        moveTankImage(manager, backwardsSpeed, ang);
    }

    public void turnRight(PlayerTankManager manager) {
        manager.tankInstance.turnRight(DELTA_ANGLE);
        manager.chassisRotation.angleProperty().set(manager.chassisRotation.angleProperty().get() + Math.toDegrees(DELTA_ANGLE));
        manager.chassisRotation.angleProperty().set(normalizeAngle(manager.chassisRotation.angleProperty().get()));
    }

    private double normalizeAngle(double angle) {
        double newAngle = angle;
        while (newAngle <= -180) newAngle += 360;
        while (newAngle > 180) newAngle -= 360;
        return newAngle;
    }

    public void fire(PlayerTankManager manager) {
        manager.tankInstance.fire();
    }

    private PathTransition createMovementPath(Node node, Point2D oldPoint, Point2D newPoint) {
        Path path = new Path();

        path.getElements().add(new MoveTo(oldPoint.getX(), oldPoint.getY()));
        path.getElements().add(new LineTo(newPoint.getX(), newPoint.getY()));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(100));
        pathTransition.setNode(node);
        pathTransition.setPath(path);
        pathTransition.setOrientation(PathTransition.OrientationType.NONE);
        pathTransition.setAutoReverse(false);
        return pathTransition;
    }

    private void moveTankImage(PlayerTankManager manager, double speed, double a) {
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
        PathTransition chassisTransition = createMovementPath(manager.chassisView, new Point2D(oldChassis.getX() + 33, oldChassis.getY() + 62),
                new Point2D(newChassis.getX() + 33, newChassis.getY() + 62));

        Point2D oldTurret = manager.tankInstance.getGameTurret().getPaintCoordinates();
        Point2D newTurret = new Point2D(oldTurret.getX() + dx, oldTurret.getY() + dy);
        manager.tankInstance.getGameTurret().setPaintCoordinates(newTurret);
        PathTransition turretTransition = createMovementPath(manager.turretView, new Point2D(oldTurret.getX() + 26, oldTurret.getY() + 70),
                new Point2D(newTurret.getX() + 26, newTurret.getY() + 70));

        manager.tankInstance.setPaintCoordinates(newChassis);

        if (!manager.tankInstance.isValid()) {
            manager.tankInstance.getGameChassis().setPaintCoordinates(oldChassis);
            manager.tankInstance.getGameTurret().setPaintCoordinates(oldTurret);
            manager.tankInstance.setPaintCoordinates(oldChassis);
        } else {
            turretTransition.play();
            chassisTransition.play();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == null || !(o instanceof GameObject) || arg == null)
            return;
        boolean intersect = intersects((GameObject) o);
        if (!intersect) {
            ((GameObject) o).setValid(true);
            return;
        }
        ((GameObject) o).setValid(false);
        System.out.println("Intersection!!");
    }

    public void register(GameObject o) {
        if (o instanceof GameTankInstance)
            this.observables.add((GameTankInstance) o);
    }

    private boolean intersects(GameObject object) {
        Shape originalObjShape = getGameObjectShape(object);

        for (GameObject gO : observables) {
            Shape shape = getGameObjectShape(gO);
            if ((object instanceof GameTankInstance) && !gO.equals(object) && shape.getBoundsInParent().intersects(originalObjShape.getBoundsInParent()))
                return true;
        }

        return false;
    }

    private static Shape getGameObjectShape(GameObject o) {
        Shape s = new Rectangle(o.getPaintCoordinates().getX(), o.getPaintCoordinates().getY(),
                o.getDisplayedWidth(), o.getDisplayedHeight());

        Rotate shapeRotation = new Rotate();
        shapeRotation.setPivotX(o.getPaintCoordinates().getX() + o.getDisplayedWidth() / 2);
        shapeRotation.setPivotY(o.getPaintCoordinates().getY() + o.getDisplayedHeight() / 2);
        shapeRotation.setAngle(o.directionAngleProperty().getValue());
        s.getTransforms().add(shapeRotation);

        return s;
    }
}
