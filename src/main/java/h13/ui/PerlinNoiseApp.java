package h13.ui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PerlinNoiseApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Perlin Noise");
        primaryStage.setResizable(false);

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        int width = (int) screen.getWidth() / 2;
        int height = (int) screen.getHeight() / 2;
        AlgorithmPane root = new AlgorithmPane();
        Scene scene = new Scene(root, width, height);

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
