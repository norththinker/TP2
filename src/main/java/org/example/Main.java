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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {
    public static final double WIDTH = 900, HEIGHT = 520;
    private Partie partie = new Partie();
    private AnimationTimer timer;
    private Pane rootJeu = new Pane();
    private VBox rootAccueil = new VBox();
    private Scene sceneAccueil = new Scene(rootAccueil, WIDTH, HEIGHT);
    private int choixPoissonInfo;
    private Image imageInfo;
    private ImageView imageViewInfo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {


        sceneAccueil.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        var sceneJeu = new Scene(rootJeu, WIDTH, HEIGHT);

        creerSceneAccueil(rootAccueil, stage, sceneJeu, sceneAccueil);

        rootJeu.setBackground(Background.fill(partie.getCouleurArrierePlan()));
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        var context = canvas.getGraphicsContext2D();


        rootJeu.getChildren().addAll(canvas);

        sceneJeu.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.SPACE) {
                partie.lancerProjectile();
            } else if (e.getCode() == KeyCode.Q) {

                partie.setChoisirProjectile(0);

            } else if (e.getCode() == KeyCode.D) {

                if (Partie.debugMode)
                    Partie.debugMode = false;
                else {
                    Partie.debugMode = true;
                }


            } else if (e.getCode() == KeyCode.W && Partie.debugMode) {


                partie.setChoisirProjectile(2);


            } else if (e.getCode() == KeyCode.E && Partie.debugMode) {


                partie.setChoisirProjectile(1);


            } else if (e.getCode() == KeyCode.R && Partie.debugMode) {


                partie.Changernombredeviedecharlotte();


            } else if (e.getCode() == KeyCode.T && Partie.debugMode) {

                timer.start();
                partie.commencerNouveauJeu(timer);


            }
            else {
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


        stage.setScene(sceneAccueil);
        stage.setResizable(false);
        stage.show();
    }

    private void creerSceneAccueil(VBox rootAccueil, Stage stage, Scene sceneJeu, Scene sceneacceuil) {
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
            if (partie == null)
                partie = new Partie();

            stage.setScene(sceneJeu);
            partie.startGame(timer);

            stage.setScene(sceneJeu);
            timer.start();
        });

        var vbox = new VBox();
        var scene = new Scene(vbox, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        Text text = new Text("Charlotte la Barbotte");
        Text text1 = new Text("Par Ismail Bissoular");
        Text text2 = new Text("Et Saib Merabet");
        Text text3 = new Text("Travail remis à Nicolas Hurtubise et Georges Côté pour le cours de 420-203-RE" + "\n" +
                " -  Développement de programmes dans un environnement graphique. ");

        Random r = new Random();
        choixPoissonInfo = r.nextInt(1, 6);

        imageInfo = new Image("poisson" + choixPoissonInfo + ".png");
        Button boutonAnnuler = new Button("Annuler");
        imageViewInfo = new ImageView(imageInfo);

        vbox.getChildren().add(text);
        vbox.getChildren().add(imageViewInfo);
        vbox.getChildren().add(text1);
        vbox.getChildren().add(text2);
        vbox.getChildren().add(text3);

        text1.setFont(Font.font(30));
        text2.setFont(Font.font(30));

        boutonAnnuler.setAlignment(Pos.BOTTOM_CENTER);
        vbox.getChildren().add(boutonAnnuler);

        boutonInfo.setOnAction(event -> {
            stage.setScene(scene);

            choixPoissonInfo = r.nextInt(1, 6);
            text.setFont(Font.font(80));
            vbox.setBackground(Background.fill(Color.rgb(42, 127, 255)));
            vbox.setAlignment(Pos.TOP_CENTER);

            boutonAnnuler.setOnAction(event1 -> {
                imageInfo = new Image("poisson" + choixPoissonInfo + ".png");
                imageViewInfo.setImage(imageInfo);
                stage.setScene(sceneacceuil);
            });

            scene.setOnKeyPressed(event1 -> {
                if (event1.getCode() == KeyCode.SPACE) {
                    stage.setScene(sceneacceuil);
                }
            });
        });
    }

    private void changementPartie(Stage stage) {
        if (partie.isChangerEcranAcceuil()) {
            stage.setScene(sceneAccueil);
            timer.stop();
            partie = new Partie();
        }
    }
}