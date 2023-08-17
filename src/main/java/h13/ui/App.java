package h13.ui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);

        // Default size
        Rectangle2D size = Screen.getPrimary().getBounds();
        double width = size.getWidth() / 2;
        double height = size.getHeight() / 2;
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.centerOnScreen();

        AlgorithmView view = new AlgorithmView();
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            view.getCanvas().setWidth(newValue.doubleValue());
        });

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            view.getCanvas().setHeight(newValue.doubleValue());
        });


        Scene scene = new Scene(view, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
