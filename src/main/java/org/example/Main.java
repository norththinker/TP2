package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
    public void start(Stage scene) {
        var root = new Pane();
        var sceneAcceuil = new Scene(root, WIDTH, HEIGHT);
        root.setBackground(Background.fill(Color.BLUE));
        Canvas canvas = new Canvas(WIDTH, HEIGHT    );
        var context = canvas.getGraphicsContext2D();


        root.getChildren().add(canvas);



        Personnage charlotte = new Personnage(3,3,102,90);
        sceneAcceuil.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
// Ferme JavaFX

            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
        sceneAcceuil.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });

var timer = new AnimationTimer() {
    long lastTime = System.nanoTime();
    @Override
    public void handle(long now) {


        double deltaTemps = (now - lastTime) * 1e-9;

        charlotte.draw(context);


        charlotte.update(deltaTemps);

        lastTime = now;
    }
};
  timer.start();






        scene.setScene(sceneAcceuil);
        scene.setResizable(false);
        scene.show();
    }
}