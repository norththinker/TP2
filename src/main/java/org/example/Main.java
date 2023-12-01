package org.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
            gererTouches(e, stage, timer, partie, rootJeu);
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

                if (partie.isChangerEcranAcceuil()) {
                    nouvellePartie(stage);
                }
                if (partie.charlotteArriveFin() && !partie.charlotteMorte()) {
                    // Recommencer le jeu
                    partie.commencerNouveauJeu(timer);
                    rootJeu.setBackground(Background.fill(partie.getCouleurArrierePlan()));

                }
                lastTime = now;
            }
        };


        stage.setScene(sceneAccueil);
        stage.setResizable(false);
        stage.show();
    }

    private void creerSceneAccueil(VBox rootAccueil, Stage stage, Scene sceneJeu, Scene sceneacceuil) {
        // Création de l'image du logo et configuration de ses dimensions
        var logoImageView = new ImageView(new Image("logo.png"));
        rootAccueil.setBackground(Background.fill(Color.rgb(42, 127, 255))); // Définition de la couleur de fond

// Configuration des dimensions du logo
        logoImageView.setFitHeight(672 / 1.5);
        logoImageView.setFitWidth(672 / 1.5);

// Création des boutons "Jouer!" et "Infos"
        var boutonJouer = new Button("Jouer!");
        var boutonInfo = new Button("Infos");

// Création du conteneur HBox pour les boutons
        var hboxBoutons = new HBox();
        hboxBoutons.getChildren().addAll(boutonJouer, boutonInfo);
        hboxBoutons.setAlignment(Pos.CENTER);
        hboxBoutons.setSpacing(20);

// Configuration du conteneur principal (root) pour la scène d'accueil
        rootAccueil.setAlignment(Pos.CENTER);
        rootAccueil.getChildren().addAll(logoImageView, hboxBoutons);

// Configuration de l'événement de touche pour quitter l'application lorsque la touche Escape est enfoncée
        sceneacceuil.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

// Gestionnaire d'événement pour le bouton "Jouer!"
        boutonJouer.setOnAction(event -> {
            if (partie == null)
                partie = new Partie();

            // Démarrage du jeu
            stage.setScene(sceneJeu);
            partie.startGame(timer);
            timer.start();
        });

// Configuration de la scène d'informations avec un style CSS externe
        var vboxInfo = new VBox();
        var sceneInfo = new Scene(vboxInfo, WIDTH, HEIGHT);
        sceneInfo.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

// Ajout des éléments textuels à la scène d'informations
        Text titreText = new Text("Charlotte la Barbotte");
        Text auteursText = new Text("Par Ismail Bissoular et Saib Merabet");
        Text coursText = new Text("Travail remis à Nicolas Hurtubise et Georges Côté pour le cours de 420-203-RE\n" +
                " -  Développement de programmes dans un environnement graphique. ");

// Génération aléatoire d'un indice de poisson pour les informations
        Random random = new Random();
        choixPoissonInfo = random.nextInt(1, 6);
         imageInfo = new Image("poisson" + choixPoissonInfo + ".png");
        Button boutonAnnuler = new Button("Annuler");
        ImageView imageViewInfo = new ImageView(imageInfo);

// Ajout des éléments à la VBox
        vboxInfo.getChildren().addAll(titreText, imageViewInfo, auteursText, coursText);

// Configuration des polices pour les textes
        auteursText.setFont(Font.font(30));

// Configuration du bouton "Annuler"
        boutonAnnuler.setAlignment(Pos.BOTTOM_CENTER);
        vboxInfo.getChildren().add(boutonAnnuler);

// Gestionnaire d'événement pour le bouton "Infos"
        boutonInfo.setOnAction(event -> {
            // Passage à la scène d'informations
            stage.setScene(sceneInfo);

            // Génération aléatoire d'un nouvel indice de poisson
            choixPoissonInfo = random.nextInt(1, 6);
            titreText.setFont(Font.font(80));
            vboxInfo.setBackground(Background.fill(Color.rgb(42, 127, 255)));
            vboxInfo.setAlignment(Pos.TOP_CENTER);

            // Gestionnaire d'événement pour le bouton "Annuler" dans la scène d'informations
            boutonAnnuler.setOnAction(event1 -> {
                // Réinitialisation de l'image du poisson et retour à la scène d'accueil
                imageInfo = new Image("poisson" + choixPoissonInfo + ".png");
                imageViewInfo.setImage(imageInfo);
                stage.setScene(sceneacceuil);
            });

            // Gestionnaire d'événement pour la touche Escape dans la scène d'informations
            sceneInfo.setOnKeyPressed(event1 -> {
                if (event1.getCode() == KeyCode.ESCAPE) {
                    // Retour à la scène d'accueil
                    stage.setScene(sceneacceuil);
                }
            });
        });
    }
    public void gererTouches(KeyEvent e, Stage stage, AnimationTimer timer, Partie partie, Pane rootJeu) {
        switch (e.getCode()) {
            case SPACE -> partie.lancerProjectile();
            case D -> {
                if (Partie.debugMode) {
                    Partie.debugMode = false;
                } else {
                    Partie.debugMode = true;
                }
            }
            case Q -> {
                if (Partie.debugMode) {
                    partie.setProjectileActuel(TypeProjectile.ETOILE);
                }
            }
            case W -> {
                if (Partie.debugMode) {
                    partie.setProjectileActuel(TypeProjectile.HIPPOCAMPE);
                }
            }
            case E -> {
                if (Partie.debugMode) {
                    partie.setProjectileActuel(TypeProjectile.SARDINES);
                }
            }
            case R -> {
                if (Partie.debugMode) {
                    partie.Changernombredeviedecharlotte();
                }
            }
            case T -> {
                if (Partie.debugMode) {
                    partie.commencerNouveauJeu(timer);
                    rootJeu.setBackground(Background.fill(partie.getCouleurArrierePlan()));
                }
            }
            case ESCAPE -> {
                stage.setScene(sceneAccueil);
                nouvellePartie(stage);
            }
            default -> Input.setKeyPressed(e.getCode(), true);
        }
    }

    private void nouvellePartie(Stage stage) {
        stage.setScene(sceneAccueil);
        timer.stop();
        partie = new Partie();
        rootJeu.setBackground(Background.fill(partie.getCouleurArrierePlan()));
    }
}