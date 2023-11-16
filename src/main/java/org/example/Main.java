package org.example;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Main extends Application {
    public static final double WIDTH = 900, HEIGHT = 520;
    Random r = new Random();
    private Image poissonEnnemi = new Image("poisson1.png");

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

        root.getChildren().add(canvas);


        Personnage charlotte = new Personnage(3, 3, 102, 90);

        LinkedList<Poisson> poissonsEnnemis= new LinkedList<>();
        var poissonSpawnTimeline = getTimeline(poissonsEnnemis);
        poissonSpawnTimeline.play();

        sceneAcceuil.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.SPACE) {
                long tempsActuel = System.nanoTime();
                if (tempsActuel - lastProjectileTime > projectileCooldown) {
                    setLancerProjectile(true);
                    projectiles.add(new Projectile(charlotte.getX() + charlotte.w/2 - 36/2, charlotte.getY() + charlotte.h/2 - 35/2, 36, 35));
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

                // 1. Update game entities
                charlotte.update(deltaTemps);

                for (Poisson ennemi : poissonsEnnemis) {
                    ennemi.update(deltaTemps);
                }

                for (Projectile projectile : projectiles) {
                    if (isLancerProjectile() && projectile != null) {
                        projectile.update(deltaTemps);
                    }
                }

                // 2. Check for collisions
                for (Poisson ennemi : poissonsEnnemis) {
                    if (!charlotte.isEstTouche()) {
                        charlotte.testCollision(ennemi);
                    }

                    for (Projectile projectile : projectiles) {
                        ennemi.testCollision(projectile);
                    }
                }

                for (Poisson poissonEnnemi : poissonsEnnemis) {
                    charlotte.testCollision(poissonEnnemi);
                }

                // 3. Remove entities marked as "touched"
                poissonsEnnemis.removeIf(Poisson::isEstTouche);

                // 4. Clear the canvas
                context.clearRect(0, 0, WIDTH, HEIGHT);

                // 5. Draw entities on the canvas
                for (Poisson poissonEnnemi : poissonsEnnemis) {
                    poissonEnnemi.draw(context);
                }

                for (Projectile projectile : projectiles) {
                    if (isLancerProjectile() && projectile != null) {
                        projectile.draw(context);
                    }
                }
                charlotte.draw(context);

                // Dessiner le rectangle blanc proportionnel au nombre de vies
                double largeurBarreVie = 150; // Ajustez la largeur de la barre de vie selon vos besoins
                double hauteurBarreVie = 30; // Ajustez la hauteur de la barre de vie selon vos besoins
                double positionHorizontaleBarre = 20; // Ajustez la position X de la barre de vie selon vos besoins
                double positionVerticaleBarre = 10; // Ajustez la position Y de la barre de vie selon vos besoins

                double pourcentageVie = (double) charlotte.getNombreDeVie() / charlotte.getNombreDeVieMax();
                double largeurRemplie = pourcentageVie * largeurBarreVie;

                context.setFill(Color.BLUE);
                context.fillRect(positionHorizontaleBarre, positionVerticaleBarre, largeurBarreVie, hauteurBarreVie);
                context.setFill(Color.WHITE);
                context.fillRect(positionHorizontaleBarre, positionVerticaleBarre, largeurRemplie, hauteurBarreVie);
                context.setStroke(Color.WHITE);
                context.setLineWidth(1.0);
                context.strokeRect(positionHorizontaleBarre, positionVerticaleBarre, largeurBarreVie, hauteurBarreVie);
                lastTime = now;
            }
        };
        timer.start();


        scene.setScene(sceneAcceuil);
        scene.setResizable(false);
        scene.show();
    }

    private Timeline getTimeline(LinkedList<Poisson> poissonsEnnemis) {
        var poissonSpawnTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1.75), event -> {
                    // Code pour créer un nouveau poisson




                    for (int i = 0; i < r.nextDouble(1,5); i++) {
                        poissonsEnnemis.add( new PoissonEnnemi(WIDTH + 50, r.nextDouble(HEIGHT / 4, HEIGHT / 2),
                                poissonEnnemi.getWidth(), poissonEnnemi.getHeight(), r.nextDouble(-100, 100)));
                    }
                })
        );
        poissonSpawnTimeline.setCycleCount(Timeline.INDEFINITE);
        return poissonSpawnTimeline;
    }
}