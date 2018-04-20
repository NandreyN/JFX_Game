package view.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

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

    public Scene getGameCompletedScene(double width, double height) throws IOException {
        Parent parent = loadFXML("gameCompleted");
        return new Scene(parent, width, height);
    }

    public Scene getGameKilledScene(double width, double height) throws IOException {
        Parent parent = loadFXML("killed");
        return new Scene(parent, width, height);
    }

    public Scene getGameLevelCompletedScene(double width, double height) throws IOException {
        Parent parent = loadFXML("levelCompleted");
        return new Scene(parent, width, height);
    }

    private Parent loadFXML(String name) throws IOException {
        return FXMLLoader.load(getClass().getResource(name + ".fxml"));
    }
}
