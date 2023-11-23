package org.example;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;

public class Main extends Application {
    public static final double WIDTH = 900, HEIGHT = 520;
    private Camera camera = new Camera(Main.WIDTH * 8);
    private Random r = new Random();
    public static boolean debugMode = false;

    public AnimationTimer timer;

    public static void main(String[] args) {
        launch(args);
    }

    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private LinkedList<Decor> decors = new LinkedList<>();
    private MediaPlayer mediaPlayer;
    private Color couleurArrierePlan = Color.hsb(r.nextInt(190,271), 0.84, 1);
    private Timeline poissonSpawnTimeline;
    private long startTimeNano;

    @Override
    public void start(Stage stage) {


        var rootJeu = new Pane();
        var rootAccueil = new VBox();

        var sceneAccueil = new Scene(rootAccueil, WIDTH, HEIGHT);
        sceneAccueil.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        var sceneJeu = new Scene(rootJeu, WIDTH, HEIGHT);

        creerSceneAccueil(rootAccueil, stage, sceneJeu);

        rootJeu.setBackground(Background.fill(couleurArrierePlan));
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        var context = canvas.getGraphicsContext2D();

        Text timeText = new Text();  // Create a Text node for displaying time
        timeText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        timeText.setFill(Color.WHITE);
        timeText.setX(10);  // Adjust the X position as needed
        timeText.setY(HEIGHT / 2);  // Adjust the Y position as needed
        rootJeu.getChildren().add(timeText);

        rootJeu.getChildren().add(canvas);


        Personnage charlotte = new Personnage(0, Main.HEIGHT / 2, 102, 90);

        LinkedList<Poisson> poissonsEnnemis = new LinkedList<>();
        poissonSpawnTimeline = getTimeline(poissonsEnnemis);


        Decor decorActuel = new Decor(0, HEIGHT - Decor.h + 10, new Image("decor1.png"));
        decors.add(decorActuel);
        int nombreDecors = 1;

        while (decorActuel.getX() + decorActuel.getW() < Main.WIDTH * 8) {
            Image imageDecor = new Image("decor" + r.nextInt(1, 7) + ".png");
            decorActuel = new Decor(0, 0, imageDecor);
            decorActuel.placerDecorSuivant(decors.get(nombreDecors - 1));
            decors.add(decorActuel);
            nombreDecors++;
        }

        String musicFile = "C:/Users/merab/OneDrive - Collège de Bois-de-Boulogne/Prog 3/TP2/src/main/resources/Midnight-City.mp3";

        try {
            // Convert the file path to a URL
            File file = new File(musicFile);
            String mediaUrl = file.toURI().toURL().toString();

            // Create a Media object with the URL
            Media sound = new Media(mediaUrl);

            // Create a MediaPlayer with the Media object
            mediaPlayer = new MediaPlayer(sound);

            // Set the cycle count and play the media
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();

        sceneJeu.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.SPACE) {
                if (Projectile.peutLancer()) {
                    projectiles.add(new Etoile(charlotte.getX() + charlotte.w / 2 - 36 / 2,
                            charlotte.getY() + charlotte.h / 2 - 35 / 2, 36, 35));
                }
            } else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });
        sceneJeu.setOnKeyReleased((e) -> {
            Input.setKeyPressed(e.getCode(), false);
        });


        timer = new AnimationTimer() {
            long lastTime = System.nanoTime();

            @Override
            public void handle(long now) {
                double deltaTemps = (now - lastTime) * 1e-9;

                // 1. Update game entities
                charlotte.update(deltaTemps);

                camera.suivre(charlotte);
                camera.update(deltaTemps, charlotte);

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

                for (Decor decor : decors) {
                    decor.draw(context, camera);
                }

                for (Poisson poissonEnnemi : poissonsEnnemis) {
                    // Utilisez la caméra pour dessiner en prenant en compte sa position
                    poissonEnnemi.draw(context, camera);
                }

                for (Projectile projectile : projectiles) {
                    if (projectile != null) {
                        projectile.draw(context, camera);
                    }
                }
                charlotte.draw(context, camera);

                // Dessiner le rectangle blanc proportionnel au nombre de vies
                double largeurBarreVie = 150; // Ajustez la largeur de la barre de vie selon vos besoins
                double hauteurBarreVie = 30; // Ajustez la hauteur de la barre de vie selon vos besoins
                double positionHorizontaleBarre = 20; // Ajustez la position X de la barre de vie selon vos besoins
                double positionVerticaleBarre = 10; // Ajustez la position Y de la barre de vie selon vos besoins

                double pourcentageVie = (double) charlotte.getNombreDeVie() / charlotte.getNombreDeVieMax();
                double largeurRemplie = pourcentageVie * largeurBarreVie;

                context.setFill(couleurArrierePlan);
                context.fillRect(positionHorizontaleBarre, positionVerticaleBarre, largeurBarreVie, hauteurBarreVie);
                context.setFill(Color.WHITE);
                context.fillRect(positionHorizontaleBarre, positionVerticaleBarre, largeurRemplie, hauteurBarreVie);
                context.setStroke(Color.WHITE);
                context.setLineWidth(2.0);
                context.strokeRect(positionHorizontaleBarre, positionVerticaleBarre, largeurBarreVie, hauteurBarreVie);

                //Ajouter le projectile utilisé à côté de la barre.
                context.setFill(couleurArrierePlan);
                context.drawImage(Projectile.imageProjectile, positionHorizontaleBarre + largeurBarreVie + 10,
                        positionVerticaleBarre, Projectile.imageProjectile.getWidth(),
                        Projectile.imageProjectile.getHeight());

                //dessiner les cœurs
                context.setFill(couleurArrierePlan);
                var proportionLargeurHauteur = 1872 / 365;
                context.drawImage(new Image(charlotte.getNombreDeVie() + "vies.png"), positionHorizontaleBarre,
                        positionVerticaleBarre + hauteurBarreVie + 20, largeurBarreVie, largeurBarreVie / proportionLargeurHauteur);

                long elapsedNano = now - startTimeNano;
                long elapsedSeconds = elapsedNano / 1_000_000_000;
                timeText.setText("Time: " + elapsedSeconds + "s");

                lastTime = now;
            }
        };


        stage.setScene(sceneAccueil);
        stage.setResizable(false);
        stage.show();
    }

    private void creerSceneAccueil(VBox rootAccueil, Stage stage, Scene sceneJeu) {
        var logo = new ImageView(new Image("logo.png"));

        rootAccueil.setBackground(Background.fill(Color.rgb(42, 127, 255)));
        logo.setFitHeight(672/1.5);
        logo.setFitWidth(672/1.5);

        var hboxJouerInfos = new HBox();
        var boutonJouer = new Button("Jouer!");
        var boutonInfo = new Button("Infos");

        hboxJouerInfos.getChildren().addAll(boutonJouer, boutonInfo);
        hboxJouerInfos.setAlignment(Pos.CENTER);
        hboxJouerInfos.setSpacing(20);

        rootAccueil.setAlignment(Pos.CENTER);
        rootAccueil.getChildren().addAll(logo, hboxJouerInfos);

        boutonJouer.setOnAction(event -> {
            stage.setScene(sceneJeu);
            timer.start();
            startTimeNano = System.nanoTime();
            poissonSpawnTimeline.play();
        });
    }

    private Timeline getTimeline(LinkedList<Poisson> poissonsEnnemis) {
        var poissonSpawnTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1.75), event -> {
                    // Code pour créer un nouveau poisson

                    for (int i = 0; i < r.nextInt(1, 6); i++) {
                        var yInitiale = r.nextDouble((HEIGHT / 5), (4 * HEIGHT / 5));
                        var vyInitial = r.nextDouble(-100, 100);
                        var imagePoisson = new Image("poisson" + r.nextInt(1, 6) + ".png");
                        var proportionLargeurHauteur = imagePoisson.getWidth() / imagePoisson.getHeight();
                        var hauteur = r.nextInt(50, 121);
                        var largeur = hauteur * proportionLargeurHauteur;

                        poissonsEnnemis.add(new PoissonEnnemi(camera.getX() + WIDTH, yInitiale,
                                largeur, hauteur, vyInitial, imagePoisson));
                    }
                })
        );
        poissonSpawnTimeline.setCycleCount(Timeline.INDEFINITE);
        return poissonSpawnTimeline;
    }
}