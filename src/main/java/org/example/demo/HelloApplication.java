package org.example.demo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        InitializeStuff initializeStuff = new InitializeStuff();
        initializeStuff.init();

        Platform.setImplicitExit(false);

//        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load(); // IMPORTANT: must call load() before getController()

        HelloController controller = fxmlLoader.getController();
        KeyListener keyListener = new KeyListener();

        keyListener.addObserver(controller);
        controller.setStage(stage);
        controller.initStuff();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                windowEvent.consume();
                stage.hide();
            }
        });





        Scene scene = new Scene(root);

        scene.setFill(Color.TRANSPARENT); // IMPORTANT: make the scene fully transparent

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Hello!");
        stage.setX(-100);
        stage.setY(-100);
        stage.show();
        stage.hide();
        centerStage(stage);

        stage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {

            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

    public void centerStage(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2 + screenBounds.getMinX());
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2 + screenBounds.getMinY());
    }


}