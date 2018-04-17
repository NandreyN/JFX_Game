package view.infoPanel;

/**
 * Interface for UI InfoPanel, which know nothing about the View
 */
public interface ITankStateUI {
    void cooldown(double duration);

    boolean decreaseHP(double byValue);
}
