package classes.behavior;

import classes.gameObjects.*;
import classes.gameObjects.Box;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.util.Pair;
import view.Animations;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static classes.behavior.TankController.DELTA_ANGLE;

public class ViewMotionManager implements Observer {
    private List<GameObject> observables = new ArrayList<>();
    private static ViewMotionManager instance;
    private static int MISSILE_MOVE_DELAY = 10;
    private static AnchorPane parent;

    public static synchronized ViewMotionManager getInstance() {
        if (instance == null) {
            instance = new ViewMotionManager();
            setupBoxes();
        }
        return instance;
    }

    private static void setupBoxes() {
        for (Box b : GameObjectDistributor.getBoxes()) {
            ImageView bView = new ImageView(new Image("file:game_textures\\Boxes\\box.jpg"));
            bView.setFitWidth(b.getDisplayedWidth());
            bView.setFitHeight(b.getDisplayedHeight());
            bView.setTranslateX(b.getLeftUpper().getX());
            bView.setTranslateY(b.getLeftUpper().getY());
            instance.observables.add(b);
            parent.getChildren().add(bView);
        }
    }

    public static void setParent(AnchorPane p) {
        parent = p;
    }

    public double rotateTurret(TankController manager, double toAngle) {
        manager.turretRotation.setAngle(Math.toDegrees(toAngle) + 90);
        manager.tankModel.getGameTurret().getTurret().rotate(
                Math.toRadians(manager.turretRotation.getAngle()));
        return manager.turretRotation.getAngle();
    }

    public void forwardMove(TankController manager) {
        double a = Math.toRadians(manager.chassisRotation.getAngle()),
                forwardSpeed = manager.tankModel.getGameChassis().getChassis().getForwardSpeed();

        moveTankImage(manager, forwardSpeed, a);
        if (manager.previousMouseEvent != null)
            manager.handle(manager.previousMouseEvent);
    }

    public double turnLeft(TankController manager) {
        manager.tankModel.turnLeft(DELTA_ANGLE);
        manager.chassisRotation.setAngle(manager.chassisRotation.getAngle() - Math.toDegrees(DELTA_ANGLE));
        manager.chassisRotation.setAngle(normalizeAngle(manager.chassisRotation.getAngle()));
        return manager.chassisRotation.getAngle();
    }

    public void backwardsMove(TankController manager) {
        double a = Math.toRadians(manager.chassisRotation.getAngle()),
                backwardsSpeed = manager.tankModel.getGameChassis().getChassis().getBackwardsSpeed();

        if (-Math.PI / 2 <= a && a <= 0) {
            a += Math.PI;
        } else if (-Math.PI <= a && a <= -Math.PI / 2) {
            a += Math.PI;
        } else if (Math.PI / 2 <= a && a <= Math.PI) {
            a = -(Math.PI - a);
        } else if (0 <= a && a <= Math.PI / 2) {
            a = -Math.PI / 2 - (Math.PI / 2 - a);
        }
        moveTankImage(manager, backwardsSpeed, a);
        if (manager.previousMouseEvent != null)
            manager.handle(manager.previousMouseEvent);
    }

    public double turnRight(TankController manager) {
        manager.tankModel.turnRight(DELTA_ANGLE);
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
        Missile missile = manager.tankModel.fire();
        if (missile == null) {
            System.out.println("Still loading");
            return;
        }
        System.out.println("Fired");
        //System.out.println("Turret : " + manager.tankModel.getGameTurret().getDirectionAngle() + "\nRotation : " + manager.turretRotation.getAngle() + "\n");
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
        missileImView.setTranslateX(missile.getLeftUpper().getX());
        missileImView.setTranslateY(missile.getLeftUpper().getY());
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

            Point2D oldMissile = missile.getLeftUpper();
            Point2D newMissile = new Point2D(oldMissile.getX() + dx, oldMissile.getY() + dy);
            missile.setLeftUpper(newMissile);
            Point2D missileConst = GameConstants.missileCentre;

            PathTransition missileTransition = createMovementPath(missileImView,
                    new Point2D(oldMissile.getX() + missileConst.getX(), oldMissile.getY() + missileConst.getY()),
                    new Point2D(newMissile.getX() + missileConst.getX(), newMissile.getY() + missileConst.getY()));
            if (!missile.isValid()) {
                ((Timer) (e.getSource())).stop();
                Platform.runLater(() -> {
                    parent.getChildren().remove(missileImView);
                    observables.remove(missile);
                });

                if (missile.getObjectHit() != null) {
                    ImageView flame = new ImageView(Animations.getExplosionAnimation());
                    flame.setTranslateX(missile.getObjectHit().getLeftUpper().getX());
                    flame.setTranslateY(missile.getObjectHit().getLeftUpper().getY());

                    flame.setFitHeight(missile.getObjectHit().getDisplayedWidth());
                    flame.setFitWidth(missile.getObjectHit().getDisplayedHeight());

                    startAnimation(flame, 1000);

                    if (missile.getObjectHit() != null && missile.getObjectHit() instanceof GameTank) {
                        ((GameTank) missile.getObjectHit()).damage(missile);
                        if (!((GameTank) missile.getObjectHit()).getTank().isAlive()) {
                            System.out.println("target destroyed");

                        }
                    }
                }
            } else {
                missileTransition.play();
            }
        }).start();
    }

    private void startAnimation(ImageView image, int duration) {
        Platform.runLater(() -> {
            parent.getChildren().add(image);
            new Timer(duration, (event) -> {
                Platform.runLater(() -> {
                    parent.getChildren().remove(image);
                });
                ((Timer) event.getSource()).stop();
            }).start();
        });
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


        Point2D oldChassis = manager.tankModel.getGameChassis().getLeftUpper();
        Point2D newChassis = oldChassis.add(new Point2D(dx, dy));
        manager.tankModel.getGameChassis().setLeftUpper(newChassis);
        PathTransition chassisTransition = createMovementPath(manager.chassisView,
                oldChassis.add(chassisCentre),
                newChassis.add(chassisCentre));

        Point2D oldTurret = manager.tankModel.getGameTurret().getLeftUpper();
        Point2D newTurret = oldTurret.add(new Point2D(dx, dy));
        manager.tankModel.getGameTurret().setLeftUpper(newTurret);
        PathTransition turretTransition = createMovementPath(manager.turretView,
                oldTurret.add(turretCentre),
                newTurret.add(turretCentre));

        manager.tankModel.setLeftUpper(newChassis);

        if (!manager.tankModel.isValid()) {
            manager.tankModel.getGameChassis().setLeftUpper(oldChassis);
            manager.tankModel.getGameTurret().setLeftUpper(oldTurret);
            manager.tankModel.setLeftUpper(oldChassis);

            manager.tankModel.setValid(true);
            manager.tankModel.getGameTurret().setValid(true);
            manager.tankModel.getGameChassis().setValid(true);
        } else {
            turretTransition.play();
            chassisTransition.play();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == null || !(o instanceof GameObject) || arg == null)
            return;

        Pair<Boolean, GameObject> intersect = intersects((GameObject) o);
        if (!intersect.getKey()) {
            ((GameObject) o).setValid(true);
            return;
        } else if (o instanceof Missile) {
            ((Missile) o).setObjectHit(intersect.getValue());
        }
        ((GameObject) o).setValid(false);
    }

    public void register(GameObject o) {
        if (o instanceof GameTank)
            this.observables.add(o);
    }

    public void unRegister(GameObject o) {
        if (this.observables.contains(o))
            this.observables.remove(o);
    }

    private Pair<Boolean, GameObject> intersects(GameObject object) {
        Shape originalObjShape = getGameObjectShape(object);
        if (intersectsGameBorder(object).getKey()) {
            return new Pair<>(true, null);
        }
        for (GameObject gO : observables) {
            Shape shape = getGameObjectShape(gO);
            if ((object instanceof GameTank) && !(gO instanceof Missile) && !gO.equals(object) && shape.getBoundsInParent().intersects(originalObjShape.getBoundsInParent()))
                return new Pair<>(true, gO);
            else if (object instanceof Missile && (gO instanceof GameTank) && ((Missile) object).getSrcTankId() != gO.getId() && shape.getBoundsInParent().intersects(originalObjShape.getBoundsInParent()))
                return new Pair<>(true, gO);
            else if (object instanceof Missile && gO instanceof Box && shape.getBoundsInParent().intersects(originalObjShape.getBoundsInParent()))
                return new Pair<>(true, gO);
        }

        return new Pair<>(false, null);
    }

    private Pair<Boolean, Line> intersectsGameBorder(GameObject o) {
        double w = parent.getPrefWidth(), h = parent.getPrefHeight();
        Line left = new Line(0, 0, 0, h),
                top = new Line(0, 0, w, 0),
                right = new Line(w, 0, w, h),
                bottom = new Line(0, h, w, h);
        Shape s = getGameObjectShape(o);

        Line intersectionLine = null;
        if (left.getBoundsInParent().intersects(s.getBoundsInParent()))
            intersectionLine = left;
        else if (top.getBoundsInParent().intersects(s.getBoundsInParent()))
            intersectionLine = top;
        else if (right.getBoundsInParent().intersects(s.getBoundsInParent()))
            intersectionLine = right;
        else if (bottom.getBoundsInParent().intersects(s.getBoundsInParent()))
            intersectionLine = bottom;

        return new Pair<>(intersectionLine != null, intersectionLine);
    }

    public static Shape getGameObjectShape(GameObject o) {
        Shape s = new Rectangle(o.getLeftUpper().getX(), o.getLeftUpper().getY(),
                o.getDisplayedWidth(), o.getDisplayedHeight());

        Rotate shapeRotation = new Rotate();
        shapeRotation.setPivotX(o.getLeftUpper().getX() + o.getDisplayedWidth() / 2);
        shapeRotation.setPivotY(o.getLeftUpper().getY() + o.getDisplayedHeight() / 2);
        shapeRotation.setAngle(o.getDirectionAngle());
        s.getTransforms().add(shapeRotation);

        return s;
    }
}
