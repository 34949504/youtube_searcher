package org.example.demo;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javafx.scene.robot.Robot;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;


public class HelloController implements Observer{
    @FXML private TextField textfield;
    @FXML private Button search_button;

    private String link_prefix = "https://www.youtube.com/results?search_query=";
    private String openGoogleURL  = "openGoogleURL.bat";
    private Stage stage;

    public void initStuff()
    {
       stage.setOnShown(new EventHandler<WindowEvent>() {
           @Override
           public void handle(WindowEvent windowEvent) {
               stage.requestFocus();
               textfield.requestFocus();

               System.out.println("Textfield has focus? "+textfield.isFocused());
           }
       });
    }

    @FXML
    public void initialize(){

        ImageView imageView = readImage("youtubeLogo.png");
        imageView.setFitHeight(60);
        imageView.setPreserveRatio(true);
        search_button.setGraphic(imageView);




       search_button.setOnMouseEntered(e -> {
    ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), imageView);
    scaleUp.setToX(1.1);
    scaleUp.setToY(1.1);
    scaleUp.play();

    RotateTransition rotate = new RotateTransition(Duration.millis(200), imageView);
    rotate.setByAngle(5);
    rotate.play();
});

// Animation on exit
search_button.setOnMouseExited(e -> {
    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), imageView);
    scaleDown.setToX(1.0);
    scaleDown.setToY(1.0);
    scaleDown.play();

    RotateTransition rotateBack = new RotateTransition(Duration.millis(200), imageView);
    rotateBack.setToAngle(0);
    rotateBack.play();
});

    }

    @FXML
    private void search()
    {
        String contents = getTextFieldContent(textfield);
        if (contents == null)
        {
            return;
        }

        String url = getCompleteURL(contents);
        execute_batFile(url);
        textfield.setText("");
        stage.hide();

    }


    private String getTextFieldContent(TextField textfield)
    {
        String content = textfield.getText();

        if (content.isEmpty())
            return null;

        return content;

    }

    private String getCompleteURL(String content)
    {
        String[] words = content.split(" ");
        StringBuilder complete = new StringBuilder(link_prefix);

        int size = words.length;
        int i = 0;
        for(String word:words)
        {
            complete.append(word);

            if (i != size-1)
            {
                complete.append("+");
            }
            i++;
        }
        return complete.toString();
    }


    private void execute_batFile(String url) {
        try {
            // 1. Extract the .bat file from resources to a temp file
            InputStream inputStream = getClass().getResourceAsStream(openGoogleURL);
            if (inputStream == null) {
                System.err.println("Could not find bat file in resources!");
                return;
            }

            File tempBat = File.createTempFile("openGoogleURL", ".bat");
            tempBat.deleteOnExit();

            Files.copy(inputStream, tempBat.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 2. Build and run the process
            ArrayList<String> script = new ArrayList<>();
            script.add(tempBat.getAbsolutePath());  // use extracted file path
            System.out.println(tempBat.getAbsolutePath());
            script.add(url);

            System.out.println("Script: " + script);

            ProcessBuilder pb = new ProcessBuilder(script);
            pb.start();

        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void openApp() {
        System.out.printf("Stage is showing %b\n",stage.isShowing());

        if (stage.isIconified())
        {
            stage.setIconified(false);
        }
        if (!stage.isShowing())
        {
            stage.setIconified(false);
            stage.setAlwaysOnTop(true);
            stage.toFront();
            stage.show();

            Scene scene = stage.getScene();
            if (scene == null) return;

            Bounds bounds = textfield.localToScreen(textfield.getBoundsInLocal());

            Robot robot = new Robot();

            // Save current mouse position
            double originalX = robot.getMouseX();
            double originalY = robot.getMouseY();

            // Move to the center of the textfield
            double clickX = bounds.getMinX() + bounds.getWidth() / 2;
            double clickY = bounds.getMinY() + bounds.getHeight() / 2;

            // Perform the click
            robot.mouseMove(clickX, clickY);
            robot.mouseClick(MouseButton.PRIMARY);

            // Restore the original mouse position
            robot.mouseMove(originalX, originalY);


//            stage.setAlwaysOnTop(false);
        }
        else {
            stage.hide();
        }
    }
    @Override
    public void closeApp() {
        stage.hide();
    }


    public ImageView readImage(String name)
    {
        InputStream inputStream =  getClass().getResourceAsStream(name);
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        return imageView;
    }


}