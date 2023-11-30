package org.example;

import javafx.animation.AnimationTimer;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    public static final double WIDTH = 900, HEIGHT = 520;
    private Partie partie = new Partie();
    private AnimationTimer timer;
    Pane rootJeu = new Pane();
    VBox rootAccueil = new VBox();
    Scene sceneAccueil = new Scene(rootAccueil, WIDTH, HEIGHT);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {


        sceneAccueil.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        var sceneJeu = new Scene(rootJeu, WIDTH, HEIGHT);

        creerSceneAccueil(rootAccueil, stage, sceneJeu);

        rootJeu.setBackground(Background.fill(partie.getCouleurArrierePlan()));
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        var context = canvas.getGraphicsContext2D();


        rootJeu.getChildren().addAll(canvas);

        sceneJeu.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.SPACE) {
                partie.lancerProjectile();
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


                partie.update(deltaTemps);
                partie.draw(context);

                changementPartie(stage);

                if (partie.charlotteArriveFin() && !partie.charlotteMorte()) {
                    // Recommencer le jeu
                    partie.commencerNouveauJeu(timer);
                    rootJeu.setBackground(Background.fill(partie.getCouleurArrierePlan()));
                }
                changementPartie(stage);

                lastTime = now;
            }
        };

        partie = new Partie();
        stage.setScene(sceneAccueil);
        stage.setResizable(false);
        stage.show();
    }

    private void creerSceneAccueil(VBox rootAccueil, Stage stage, Scene sceneJeu) {
        var logo = new ImageView(new Image("logo.png"));

        rootAccueil.setBackground(Background.fill(Color.rgb(42, 127, 255)));
        logo.setFitHeight(672 / 1.5);
        logo.setFitWidth(672 / 1.5);

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
            partie.startGame(timer);
        });
    }

    private void changementPartie(Stage stage) {
        if (partie.isChangerEcranAcceuil()) {
            stage.setScene(sceneAccueil);
            timer.stop();
        }
    }
}