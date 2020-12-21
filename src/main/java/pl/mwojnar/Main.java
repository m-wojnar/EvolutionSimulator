package pl.mwojnar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        var scene = new Scene(FXMLLoader.load(getClass().getResource("configuration.fxml")));
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("icon.png").toString()));
        stage.setTitle("Evolution simulator");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
