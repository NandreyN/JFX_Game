package classes.behavior;

import classes.gameObjects.*;
import classes.gameObjects.Box;
import classes.sound.SoundPlayer;
import classes.tanks.parts.SAUTurret;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
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

import javax.media.j3d.View;
import javax.swing.Timer;
import java.util.*;
import java.util.stream.Collectors;

import static classes.behavior.TankController.DELTA_ANGLE;

/**
 * Main game class. Reflects controller changes on View
 * Singleton
 */
public class ViewMotionManager implements Observer {
    private List<GameObject> observables = Collections.synchronizedList(new ArrayList<>());
    private List<ImageView> gameBoxes = Collections.synchronizedList(new ArrayList<>());

    private static ViewMotionManager instance;
    private static int MISSILE_MOVE_DELAY = 10;
    private static AnchorPane parent;

    public static synchronized ViewMotionManager getInstance() {
        if (instance == null) {
            instance = new ViewMotionManager();
        }
        return instance;
    }

    /**
     * Add gameBoxes objects to game scene
     *
     * @param boxes List of GameBox
     */
    public static synchronized void setupBoxes(List<Box> boxes) {
        for (Box b : boxes) {
            ImageView bView = new ImageView(b.getTexture());
            bView.setVisible(true);
            bView.setFitWidth(b.getDisplayedWidth());
            bView.setFitHeight(b.getDisplayedHeight());
            bView.setTranslateX(b.getLeftUpper().getX());
            bView.setTranslateY(b.getLeftUpper().getY());
            instance.observables.add(b);
            parent.getChildren().add(bView);
            instance.gameBoxes.add(bView);
        }
    }

    public void dropBoxes() {
       /* parent.getChildren().removeAll(gameBoxes);
        gameBoxes.clear();
        List<GameObject> boxes = observables.stream().filter((x) -> x instanceof Box).collect(Collectors.toList());
        observables.removeAll(boxes);*/
        AnchorPane parentCopy = parent;
        instance = null;
        parent = null;
        parentCopy.getChildren().clear();
        ViewMotionManager.getInstance();
        ViewMotionManager.setParent(parentCopy);
    }

    /**
     * @param p Apply Game scene
     */
    public static synchronized void setParent(AnchorPane p) {
        parent = p;
    }

    /**
     * Reflect turret rotation on View
     *
     * @param manager Tank controller, source of action
     * @param toAngle Target angle
     * @return new angle, same as @param toAngle
     */
    public double rotateTurret(TankController manager, double toAngle) {
        if (manager.isDisposed)
            return 0;

        manager.turretRotation.setAngle(Math.toDegrees(toAngle) + 90);
        manager.tankModel.getGameTurret().getTurret().rotate(
                Math.toRadians(manager.turretRotation.getAngle()));
        return manager.turretRotation.getAngle();
    }

    /**
     * Move tank forward the direction angle
     *
     * @param manager Tank controller, source of action
     */
    public void forwardMove(TankController manager) {
        if (manager.isDisposed)
            return;

        double a = Math.toRadians(manager.chassisRotation.getAngle()),
                forwardSpeed = manager.tankModel.getGameChassis().getChassis().getForwardSpeed();

        moveTankImage(manager, forwardSpeed, a);
        if (manager.previousMouseEvent != null)
            manager.handle(manager.previousMouseEvent);
    }

    /**
     * Turn tank view to the left
     *
     * @param manager Tank controller , source of action
     */
    public void turnLeft(TankController manager) {
        if (manager.isDisposed)
            return;

        manager.tankModel.turnLeft(DELTA_ANGLE);
        double srcAngle = manager.chassisRotation.getAngle();

        manager.chassisRotation.setAngle(manager.chassisRotation.getAngle() - Math.toDegrees(DELTA_ANGLE));
        manager.chassisRotation.setAngle(normalizeAngle(manager.chassisRotation.getAngle()));

        manager.tankModel.setDirectionAngle(manager.chassisRotation.getAngle());
        manager.tankModel.getGameChassis().setDirectionAngle(manager.chassisRotation.getAngle());

        manager.canRotateLeft = true;
        manager.canMoveForward = true;

        if (intersects(manager.tankModel).getKey() || intersectsGameBorder(manager.tankModel).getKey()) {
            manager.tankModel.turnRight(DELTA_ANGLE);
            manager.chassisRotation.setAngle(srcAngle);
            manager.canRotateLeft = false;
            manager.canMoveForward = false;
            return;
        }

        if (manager.tankModel.getGameTurret().getTurret() instanceof SAUTurret) {
            manager.tankModel.getGameTurret().setDirectionAngle(manager.chassisRotation.getAngle());
            manager.turretRotation.setAngle(manager.chassisRotation.getAngle());
        }
    }

    /**
     * Move the tank backwards the direction angle
     *
     * @param manager Tank controller , source of action
     */
    public void backwardsMove(TankController manager) {
        if (manager.isDisposed)
            return;
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

    /**
     * Move tank view to the right
     *
     * @param manager Tank controller , source of action
     */
    public void turnRight(TankController manager) {
        if (manager.isDisposed)
            return;

        manager.tankModel.turnRight(DELTA_ANGLE);
        double srcAngle = manager.chassisRotation.getAngle();

        manager.chassisRotation.setAngle(manager.chassisRotation.getAngle() + Math.toDegrees(DELTA_ANGLE));
        manager.chassisRotation.setAngle(normalizeAngle(manager.chassisRotation.getAngle()));

        manager.tankModel.setDirectionAngle(manager.chassisRotation.getAngle());
        manager.tankModel.getGameChassis().setDirectionAngle(manager.chassisRotation.getAngle());

        manager.canRotateRight = true;
        manager.canMoveForward = true;

        if (intersects(manager.tankModel).getKey() || intersectsGameBorder(manager.tankModel).getKey()) {
            manager.tankModel.turnLeft(DELTA_ANGLE);
            manager.chassisRotation.setAngle(srcAngle);
            manager.canRotateRight = false;
            manager.canMoveForward = false;
            return;
        }

        if (manager.tankModel.getGameTurret().getTurret() instanceof SAUTurret) {
            manager.tankModel.getGameTurret().setDirectionAngle(manager.chassisRotation.getAngle());
            manager.turretRotation.setAngle(manager.chassisRotation.getAngle());
        }
    }

    /**
     * Reduction angle to the format used by Rotate class
     *
     * @param angle Angle to be reducted
     * @return normalized angle
     */
    private double normalizeAngle(double angle) {
        double newAngle = angle;
        while (newAngle <= -180) newAngle += 360;
        while (newAngle > 180) newAngle -= 360;
        return newAngle;
    }

    /**
     * Starts fire action. If success, move and tracks missile
     * intersection with game objects with consequent explosion animation
     *
     * @param manager tank controller, source of action
     */
    public void fire(TankController manager) {
        if (manager.isDisposed)
            return;
        Missile missile = manager.tankModel.fire();
        if (missile == null) {
            return;
        }
        SoundPlayer.getInstance().play(SoundPlayer.SoundTypes.SHOT);
        //System.out.println("Turret : " + manager.tankModel.getGameTurret().getDirectionAngle() + "\nRotation : " + manager.turretRotation.getAngle() + "\n");
        missile.setDirectionAngle(manager.turretRotation.getAngle());

        Point2D gunEnd = new Point2D(manager.turretView.getTranslateX(), manager.turretView.getTranslateY());
        gunEnd = gunEnd.add(manager.turretRotation.transform(GameConstants.missileStartRelativeTurret));

        missile.setLeftUpper(gunEnd);
        moveMissile(gunEnd.getX(), gunEnd.getY(), missile, Math.toRadians(manager.turretRotation.getAngle()));

        double cX = GameConstants.gunFlameSize.getKey() / 2, cY = GameConstants.gunFlameSize.getValue() / 2;
        startAnimation(configureImageView(Animations.getExplosionAnimation(),
                gunEnd.getX() - cX, gunEnd.getY() - cY, GameConstants.gunFlameSize.getKey(),
                GameConstants.gunFlameSize.getValue(), false), 500);
    }

    /**
     * Configures ImageView to be displayed according to
     * GameObject options
     *
     * @param src    Image to be displayed
     * @param xPos   X left upper coordinate on game scene
     * @param yPos   Y left upper coordinate on game scene
     * @param width  Desired width of the image view
     * @param height Desired height of the image view
     * @return Configured ImageView object
     */
    private ImageView configureImageView(Image src, double xPos, double yPos, double width, double height, boolean overlay) {
        ImageView imView = new ImageView(src);
        if (overlay)
            imView.setBlendMode(BlendMode.SRC_OVER);
        imView.setTranslateX(xPos);
        imView.setTranslateY(yPos);
        imView.setFitHeight(height);
        imView.setFitWidth(width);
        return imView;
    }

    /**
     * Creates path from one point to another
     *
     * @param node     Object to be transfered
     * @param oldPoint old location
     * @param newPoint new location
     * @return path description object
     */
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

    /**
     * Handles missile moves
     *
     * @param x       X spawn coordinate
     * @param y       Y spawn coordinate
     * @param missile Missile object
     * @param a       Direction angle
     */
    private void moveMissile(double x, double y, Missile missile, double a) {
        observables.add(missile);

        ImageView missileImView = configureImageView(missile.getTexture(), x, y,
                missile.getDisplayedWidth(), missile.getDisplayedHeight(), true);
        parent.getChildren().add(missileImView);
        missileImView.setVisible(true);
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
                    ImageView flame = configureImageView(Animations.getExplosionAnimation(), missile.getObjectHit().getLeftUpper().getX(),
                            missile.getObjectHit().getLeftUpper().getY(), missile.getObjectHit().getDisplayedWidth(),
                            missile.getObjectHit().getDisplayedHeight(), false);
                    startAnimation(flame, 1000);
                    SoundPlayer.getInstance().play(SoundPlayer.SoundTypes.EXPLOSION);
                    if (missile.getObjectHit() != null && missile.getObjectHit() instanceof GameTank) {
                        Platform.runLater(() -> {
                            ((GameTank) missile.getObjectHit()).damage(missile);
                            if (!((GameTank) missile.getObjectHit()).getTank().isAlive()) {
                                System.out.println("target destroyed");
                            }
                        });

                    }
                }
            } else {
                missileTransition.play();
            }
        }).start();
    }

    /**
     * Display gif during desired time
     *
     * @param image    GIF image
     * @param duration GIF display duration
     */
    private void startAnimation(ImageView image, int duration) {
        Platform.runLater(() -> {
            parent.getChildren().add(image);
            new Timer(duration, (event) -> {
                ((Timer) event.getSource()).stop();
                Platform.runLater(() -> {
                    parent.getChildren().remove(image);
                });
            }).start();
        });
    }

    /**
     * Logic for moving tank
     *
     * @param manager Tank controller , source of action
     * @param speed   Tank speed
     * @param a       Tank direction angle, move vector
     */
    private void moveTankImage(TankController manager, double speed, double a) {
        if (manager.isDisposed)
            return;

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
            manager.canMoveForward = false;
        } else {
            turretTransition.play();
            chassisTransition.play();
            manager.canMoveForward = true;
        }
    }

    /**
     * Observer feature. Whenever paint coordinates of GameObject
     * are changes , that method is called to validate it
     *
     * @param o   GameObject
     * @param arg Argument
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o == null || !(o instanceof GameObject) || arg == null)
            return;
        if (!observables.contains(o))
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

    /**
     * Register game tank in registry
     *
     * @param o GameTank. If not - ignore
     */
    public void register(GameObject o) {
        if (o instanceof GameTank)
            this.observables.add(o);
    }

    /**
     * Remove GameObject from registry
     *
     * @param o GameObject to delete from track
     */
    public void unRegister(GameObject o) {
        if (this.observables.contains(o))
            this.observables.remove(o);
    }

    /**
     * Checks given GameObject on intersection with all other tracked
     * objects
     *
     * @param object Current object
     * @return Pair IntersectsWhat
     */
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

    /**
     * Intersection with game borders
     *
     * @param o Current GameObject
     * @return Pair IntersectsWhat
     */
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

    /**
     * Constructs object shape as a rectangle
     * depending on it`s features
     *
     * @param o GameObject to construct bounds for
     * @return Bounds of given GameObject
     */
    public static synchronized Shape getGameObjectShape(GameObject o) {
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
