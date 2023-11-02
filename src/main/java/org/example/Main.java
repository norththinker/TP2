package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static final double WIDTH = 900, HEIGHT = 520;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var root = new Pane();
        var sceneAcceuil = new Scene(root, WIDTH, HEIGHT);
        root.setBackground(Background.fill(Color.BLUE));

        var logoView = new ImageView(new Image("logo.png"));

        root.getChildren().add(logoView);

        primaryStage.setScene(sceneAcceuil);
        primaryStage.setResizable(false);
    }
}