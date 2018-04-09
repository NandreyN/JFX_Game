package classes.behavior;

import classes.gameObjects.GameConstants;
import classes.gameObjects.GameObject;
import classes.gameObjects.GameTankInstance;
import classes.gameObjects.Missile;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static classes.behavior.TankController.DELTA_ANGLE;

public class ViewMotionManager implements Observer {
    private List<GameObject> observables = new ArrayList<>();
    private static ViewMotionManager instance;
    private static int MISSILE_MOVE_DELAY = 30;
    private static AnchorPane parent;

    public static synchronized ViewMotionManager getInstance() {
        if (instance == null) {
            instance = new ViewMotionManager();
        }
        return instance;
    }

    public static void setParent(AnchorPane p) {
        parent = p;
    }

    public double rotateTurret(TankController manager, double toAngle) {
        manager.turretRotation.setAngle(Math.toDegrees(toAngle) + 90);
        manager.tankInstance.getGameTurret().getTurret().rotate(
                Math.toRadians(manager.turretRotation.getAngle()));
        return manager.turretRotation.getAngle();
    }

    public void forwardMove(TankController manager) {
        double a = Math.toRadians(manager.chassisRotation.getAngle()),
                forwardSpeed = manager.tankInstance.getGameChassis().getChassis().getForwardSpeed();
        moveTankImage(manager, forwardSpeed, a);
        if (manager.previousMouseEvent != null)
            manager.handle(manager.previousMouseEvent);
    }

    public double turnLeft(TankController manager) {
        manager.tankInstance.turnLeft(DELTA_ANGLE);
        manager.chassisRotation.setAngle(manager.chassisRotation.getAngle() - Math.toDegrees(DELTA_ANGLE));
        manager.chassisRotation.setAngle(normalizeAngle(manager.chassisRotation.getAngle()));
        return manager.chassisRotation.getAngle();
    }

    public void backwardsMove(TankController manager) {
        double ang = Math.PI + Math.toRadians(manager.chassisRotation.getAngle()),
                backwardsSpeed = manager.tankInstance.getGameChassis().getChassis().getBackwardsSpeed();
        moveTankImage(manager, backwardsSpeed, ang);
        if (manager.previousMouseEvent != null)
            manager.handle(manager.previousMouseEvent);
    }

    public double turnRight(TankController manager) {
        manager.tankInstance.turnRight(DELTA_ANGLE);
        manager.chassisRotation.setAngle(manager.chassisRotation.getAngle() + Math.toDegrees(DELTA_ANGLE));
        manager.chassisRotation.setAngle(normalizeAngle(manager.chassisRotation.getAngle()));
        return manager.chassisRotation.getAngle();
    }

    private double normalizeAngle(double angle) {
        double newAngle = angle;
        while (newAngle <= -180) newAngle += 360;
        while (newAngle > 180) newAngle -= 360;
        return newAngle;
    }

    public void fire(TankController manager) {
        Missile missile = manager.tankInstance.fire();
        if (missile == null) {
            System.out.println("Still loading");
            return;
        }
        System.out.println("Fired");
        //System.out.println("Turret : " + manager.tankInstance.getGameTurret().getDirectionAngle() + "\nRotation : " + manager.turretRotation.getAngle() + "\n");
        missile.setDirectionAngle(manager.turretRotation.getAngle());
        moveMissile(missile, Math.toRadians(manager.turretRotation.getAngle()));
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

    private void moveMissile(Missile missile, double a) {
        observables.add(missile);
        ImageView missileImView = new ImageView(missile.getTexture());
        missileImView.setTranslateX(missile.getPaintCoordinates().getX());
        missileImView.setTranslateY(missile.getPaintCoordinates().getY());
        parent.getChildren().add(missileImView);
        missileImView.setVisible(false);
        new Timer(MISSILE_MOVE_DELAY, (e) -> {
            //move
            double dx = Math.abs(missile.getSpeed() * Math.sin(a));
            double dy = Math.abs(missile.getSpeed() * Math.cos(a));

            if (-Math.PI / 2 <= a && a <= 0) {
            } else if (Math.PI <= a && a <= 3 * Math.PI / 2) {
                dy = -dy;
            } else if (Math.PI / 2 <= a && a <= Math.PI) {
                dx = -dx;
                dy = -dy;
            } else if (0 <= a && a <= Math.PI / 2) {
                dx = -dx;
            }

            Point2D oldMissile = missile.getPaintCoordinates();
            Point2D newMissile = new Point2D(oldMissile.getX() + dx, oldMissile.getY() + dy);
            missile.setPaintCoordinates(newMissile);
            Point2D missileConst = GameConstants.missileCentre;

            PathTransition missileTransition = createMovementPath(missileImView,
                    new Point2D(oldMissile.getX() + missileConst.getX(), oldMissile.getY() + missileConst.getY()),
                    new Point2D(newMissile.getX() + missileConst.getX(), newMissile.getY() + missileConst.getY()));
            if (!missile.isValid()) {
                ((Timer) (e.getSource())).stop();
                observables.remove(missile);
                Platform.runLater(() -> parent.getChildren().remove(missileImView));
                System.out.println("Hit");
            } else {
                missileTransition.play();
            }
        }).start();
    }

    private void moveTankImage(TankController manager, double speed, double a) {
        double dx = Math.abs(speed * Math.sin(a));
        double dy = Math.abs(speed * Math.cos(a));

        Point2D chassisCentre = GameConstants.chassisCentre, turretCentre = GameConstants.turretTextureCentre;

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
        Point2D newChassis = oldChassis.add(new Point2D(dx, dy));
        manager.tankInstance.getGameChassis().setPaintCoordinates(newChassis);
        PathTransition chassisTransition = createMovementPath(manager.chassisView,
                oldChassis.add(chassisCentre),
                newChassis.add(chassisCentre));

        Point2D oldTurret = manager.tankInstance.getGameTurret().getPaintCoordinates();
        Point2D newTurret = oldTurret.add(new Point2D(dx, dy));
        manager.tankInstance.getGameTurret().setPaintCoordinates(newTurret);
        PathTransition turretTransition = createMovementPath(manager.turretView,
                oldTurret.add(turretCentre),
                newTurret.add(turretCentre));

        manager.tankInstance.setPaintCoordinates(newChassis);

        if (!manager.tankInstance.isValid()) {
            manager.tankInstance.getGameChassis().setPaintCoordinates(oldChassis);
            manager.tankInstance.getGameTurret().setPaintCoordinates(oldTurret);
            manager.tankInstance.setPaintCoordinates(oldChassis);

            manager.tankInstance.setValid(true);
            manager.tankInstance.getGameTurret().setValid(true);
            manager.tankInstance.getGameChassis().setValid(true);
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
    }

    public void register(GameObject o) {
        if (o instanceof GameTankInstance)
            this.observables.add((GameTankInstance) o);
    }

    private boolean intersects(GameObject object) {
        Shape originalObjShape = getGameObjectShape(object);

        for (GameObject gO : observables) {
            Shape shape = getGameObjectShape(gO);
            if ((object instanceof GameTankInstance) && !(gO instanceof Missile) && !gO.equals(object) && shape.getBoundsInParent().intersects(originalObjShape.getBoundsInParent()))
                return true;
            else if (object instanceof Missile && gO instanceof GameTankInstance && ((Missile) object).getSrcTankId() != gO.getId() && shape.getBoundsInParent().intersects(originalObjShape.getBoundsInParent()))
                return true;
        }

        return false;
    }

    public static Shape getGameObjectShape(GameObject o) {
        Shape s = new Rectangle(o.getPaintCoordinates().getX(), o.getPaintCoordinates().getY(),
                o.getDisplayedWidth(), o.getDisplayedHeight());

        Rotate shapeRotation = new Rotate();
        shapeRotation.setPivotX(o.getPaintCoordinates().getX() + o.getDisplayedWidth() / 2);
        shapeRotation.setPivotY(o.getPaintCoordinates().getY() + o.getDisplayedHeight() / 2);
        shapeRotation.setAngle(o.getDirectionAngle());
        s.getTransforms().add(shapeRotation);

        return s;
    }
}
