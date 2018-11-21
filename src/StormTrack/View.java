package StormTrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class View extends Application {

    public View() {}

    public static void main(String[] args) throws Exception {

        launch(args);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane page = (BorderPane) FXMLLoader.load(getClass()
                    .getClassLoader().getResource("Window.fxml"));

            Scene scene = new Scene(page);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Storm Track v0.9");

            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
