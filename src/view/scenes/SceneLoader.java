package view.scenes;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Configures game scene and returns it
 * for displaying
 */
public class SceneLoader {
    private static SceneLoader sceneLoader;
    private Stage mainStage;

    public static SceneLoader getInstance() {
        if (sceneLoader == null)
            sceneLoader = new SceneLoader();
        return sceneLoader;
    }

    public void setStage(Stage stage) {
        assert stage != null;
        this.mainStage = stage;
    }

    public Scene getGameCompletedScene(double width, double height) {
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(new TextField("Completed"));
        return new Scene(pane, width, height);
    }

    public Scene getGameKilledScene(double width, double height) {
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(new TextField("Потрачено"));
        return new Scene(pane, width, height);
    }

    public Scene getGameLevelCompletedScene(double width, double height) {
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(new TextField("Level completed"));
        return new Scene(pane, width, height);
    }

    public void removeStage(Scene scene) {
    }
}
