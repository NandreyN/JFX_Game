package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.scenes.SceneLoader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneLoader.getInstance().setStage(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Tank Game");
        primaryStage.setScene(new Scene(root, 1000, 650));
        primaryStage.show();
        root.requestFocus();

        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
