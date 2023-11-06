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

import java.util.ArrayList;
import java.util.Random;


public class Main extends Application {
    public static final double WIDTH = 900, HEIGHT = 520;



    public static void main(String[] args) {
        launch(args);
    }

    public boolean isLancerProjectile() {
        return lancerProjectile;
    }

    public void setLancerProjectile(boolean lancerProjectile) {
        this.lancerProjectile = lancerProjectile;
    }
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private boolean lancerProjectile;
    private long lastProjectileTime = 0;
    private final long projectileCooldown = 500_000_000;

    @Override
    public void start(Stage scene) {
        var root = new Pane();
        var sceneAcceuil = new Scene(root, WIDTH, HEIGHT);
        root.setBackground(Background.fill(Color.BLUE));
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        var context = canvas.getGraphicsContext2D();
        var context2 = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);


        Personnage charlotte = new Personnage(3, 3, 102, 90);
        Random r =new Random();

Poissons poissons1[] = new Poissons[5];
        for (int i = 0; i < 5; i++) {
            poissons1[i] = new Poissons(WIDTH+50 , r.nextDouble(HEIGHT/4,HEIGHT/2),90,90,r.nextDouble(-100,100));
        }

        sceneAcceuil.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.SPACE) {
                long tempsActuel  = System.nanoTime();
// Ferme JavaFX
                if (tempsActuel- lastProjectileTime > projectileCooldown) {
                    setLancerProjectile(true);

                    projectiles.add(new Projectile(charlotte.getX(), charlotte.getY(), 3, 3));
                    lastProjectileTime = tempsActuel;
                }
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
                for (int i = 0; i < poissons1.length; i++) {
                    poissons1[i].update(deltaTemps);
                }


                charlotte.update(deltaTemps);
                charlotte.setX(charlotte.getX());
                charlotte.setY(charlotte.getY());

                for (Projectile projectile : projectiles) {
                    for (int i = 0; i < poissons1.length; i++) {


                    if (isLancerProjectile() && projectile != null) {
                        projectile.update(deltaTemps);
                        projectile.testCollision(poissons1[i]);
                    }
                    }
                }

// Dans la boucle de dessin

                context.clearRect(0, 0, WIDTH, HEIGHT);
                charlotte.draw(context);
                for (Projectile projectile : projectiles) {
                    if (isLancerProjectile() && projectile != null) {
                        projectile.draw(context);
                    }

                }
                for (int i = 0; i < poissons1.length; i++) {
                poissons1[i].draw(context);

                }


                 lastTime = now;}
        };
        timer.start();


        scene.setScene(sceneAcceuil);
        scene.setResizable(false);
        scene.show();
    }
}