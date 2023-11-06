package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
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
        var scene = new Scene(root, WIDTH, HEIGHT);
        var canvas = new Canvas(WIDTH, HEIGHT);

        root.getChildren().add(canvas);
        var contextCharlotte = canvas.getGraphicsContext2D();


        var charlotte = new Personnage(0,0, 100,100);

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
// Ferme JavaFX
                Platform.exit();
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
        scene.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });


        var timer = new AnimationTimer() {
            long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {

                double deltaTemps = (now - lastTime) * 1e-9;


                /* -- Update --
                for (var flocon : flocons)
                    flocon.update(deltaTemps);
                */

                contextCharlotte.clearRect(0,0, WIDTH, HEIGHT);

                // -- Dessin --
                // Arrière-plan
               contextCharlotte.setFill(Color.BLUE);
               contextCharlotte.fillRect(0, 0, WIDTH, HEIGHT);


                charlotte.update(deltaTemps);

                charlotte.draw(contextCharlotte);

                /*
                for (var flocon : flocons)
                    flocon.draw(context);
                */

                lastTime = now;
            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Animations!");
        primaryStage.show();
    }
}