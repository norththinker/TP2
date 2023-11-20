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

    public static void main(String[] args) {
        launch(args);
    }

    private ArrayList<Projectile> projectiles = new ArrayList<>();

    @Override
    public void start(Stage scene) {
        var root = new Pane();
        var sceneAcceuil = new Scene(root, WIDTH, HEIGHT);
        root.setBackground(Background.fill(Color.BLUE));
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        var context = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);


        Personnage charlotte = new Personnage(0, Main.HEIGHT/2, 102, 90);

        LinkedList<Poisson> poissonsEnnemis = new LinkedList<>();
        var poissonSpawnTimeline = getTimeline(poissonsEnnemis);
        poissonSpawnTimeline.play();

        sceneAcceuil.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.SPACE) {
                if (Projectile.peutLancer()) {
                    projectiles.add(new Etoile(charlotte.getX() + charlotte.w / 2 - 36 / 2,
                            charlotte.getY() + charlotte.h / 2 - 35 / 2, 36, 35));
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
                    if (projectile.isDepasse()) {
                        projectiles.remove(projectile);
                        return;
                    }
                    projectile.update(deltaTemps);
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
                    if (projectile != null) {
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
                context.setLineWidth(2.0);
                context.strokeRect(positionHorizontaleBarre, positionVerticaleBarre, largeurBarreVie, hauteurBarreVie);

                //Ajouter le projectile utilisé à côté de la barre.
                context.setFill(Color.BLUE);
                context.drawImage(Projectile.imageProjectile, positionHorizontaleBarre + largeurBarreVie+10,
                        positionVerticaleBarre, Projectile.imageProjectile.getWidth(),
                        Projectile.imageProjectile.getHeight());

                //dessiner les cœurs
                context.setFill(Color.BLUE);
                var proportionLargeurHauteur = 1872/365;
                context.drawImage(new Image(charlotte.getNombreDeVie()+"vies.png"), positionHorizontaleBarre,
                        positionVerticaleBarre + hauteurBarreVie + 20, largeurBarreVie, largeurBarreVie/proportionLargeurHauteur);

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

                    for (int i = 0; i < r.nextInt(1, 6); i++) {
                        var yInitiale = r.nextDouble((HEIGHT / 5), (4 * HEIGHT / 5));
                        var vyInitial = r.nextDouble(-100, 100);
                        var imagePoisson = new Image("poisson" + r.nextInt(1, 6) + ".png");
                        var proportionLargeurHauteur = imagePoisson.getWidth()/imagePoisson.getHeight();
                        var hauteur = r.nextInt(50, 121);
                        var largeur = hauteur*proportionLargeurHauteur;
                        poissonsEnnemis.add(new PoissonEnnemi(WIDTH, yInitiale,
                                largeur, hauteur, vyInitial, imagePoisson));
                    }
                })
        );
        poissonSpawnTimeline.setCycleCount(Timeline.INDEFINITE);
        return poissonSpawnTimeline;
    }
}