package classes.tanks.parts;

public interface IGun {
    boolean fire();
    boolean isReady();
    double getNextCooldown();
}
