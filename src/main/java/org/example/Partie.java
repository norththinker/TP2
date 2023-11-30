package org.example;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Partie {
    private boolean dejatirer = false;
    private Camera camera = new Camera(Main.WIDTH * 8);
    private Personnage charlotte = new Personnage(0, Main.HEIGHT / 2, 102, 90);
    private Random r = new Random();
    public static boolean debugMode = true;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private LinkedList<Decor> decors = new LinkedList<>();
    private MediaPlayer mediaPlayer;
    private Color couleurArrierePlan = Color.hsb(r.nextInt(190, 271), 0.84, 1);
    private LinkedList<Poisson> poissonsEnnemis = new LinkedList<>();
    private Timeline poissonSpawnTimeline;
    private long tempsDepuisDebut;
    private long startTimeNano;
    public static int numeroNiveau = 1;
    private Baril baril = new Baril();
    private int choisirProjectile = 0;
    private int dernierTypeProjectile = 0;
    private Image choisirImage;
    private boolean partieFini = false;
    private long tempsDepuisPartieFini;
    boolean finDePartieAffiche = false;

    public Partie() {
        initialiserDecors();
        initializeMedia();
        poissonSpawnTimeline = getTimeline(poissonsEnnemis);
    }

    private void initialiserDecors() {

        Decor decorActuel = new Decor(0, Main.HEIGHT - Decor.h + 10, new Image("decor1.png"));
        decors.add(decorActuel);
        int nombreDecors = 1;

        while (decorActuel.getX() + decorActuel.getW() < Main.WIDTH * 8) {
            Image imageDecor = new Image("decor" + r.nextInt(1, 7) + ".png");
            decorActuel = new Decor(0, 0, imageDecor);
            decorActuel.placerDecorSuivant(decors.get(nombreDecors - 1));
            decors.add(decorActuel);
            nombreDecors++;
        }
    }

    private void initializeMedia() {
        String musicFile = "src/main/resources/Midnight-City.mp3";
        try {
            File file = new File(musicFile);
            String mediaUrl = file.toURI().toURL().toString();
            Media sound = new Media(mediaUrl);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mediaPlayer.setVolume(0);
    }

    public void update(double deltaTemps) {
        charlotte.update(deltaTemps, camera);
        camera.update(deltaTemps, charlotte);
        camera.suivre(charlotte);
        camera.update(deltaTemps, charlotte);


        for (Poisson ennemi : poissonsEnnemis) {
            ennemi.update(deltaTemps, camera);
        }

        for (Projectile projectile : projectiles) {
            if (projectile.isDepasse()) {
                projectiles.remove(projectile);
                return;
            }
            projectile.update(deltaTemps, camera);
        }
        updateTempsEcouleDepuisDebut();
        // 2. Check for collisions
        for (Poisson ennemi : poissonsEnnemis) {
            if (!charlotte.isEstTouche()) {
                charlotte.testCollision(ennemi);
            }

            for (Projectile projectile : projectiles) {
                ennemi.testCollision(projectile);
            }
        }

        baril.update(deltaTemps, camera);
        baril.testCollision(charlotte);
        Changerimage();

        if (baril.isTouche() && !dejatirer) {

            if (dernierTypeProjectile == baril.getChoix()) {
                while (dernierTypeProjectile == baril.getChoix())
                    baril.setChoix(r.nextInt(0, 3));
            }

            choisirProjectile = baril.getChoix();
            dejatirer = true;

        }

        if (choisirProjectile == 1) {
            for (Projectile projectile : projectiles) {
                projectile.calculerForcesElectriques(poissonsEnnemis, charlotte);
            }
        }

        for (Poisson poissonEnnemi : poissonsEnnemis) {
            charlotte.testCollision(poissonEnnemi);
        }

        // 3. Remove entities marked as "touched"
        poissonsEnnemis.removeIf(Poisson::isEstTouche);

    }

    public void draw(GraphicsContext context) {

        context.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);


        // 5. Draw entities on the canvas

        for (Decor decor : decors) {
            decor.draw(context, camera);
        }

        baril.draw(context, camera);

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

        dessinerBarreVie(context);

        afficherStatusJeu(context);
        afficherTemppsEcoule(context);
    }

    public void startGame(AnimationTimer timer) {
        // Start the game
        startTimeNano = System.nanoTime();
        timer.start();
        mediaPlayer.play();
        startPoissonSpawnTimeline();
    }

    public void commencerNouveauJeu(AnimationTimer timer) {
        timer.stop();
        startTimeNano = System.nanoTime();
        charlotte.setX(0);
        camera.setX(0);
        projectiles.clear();
        decors.clear();
        poissonsEnnemis.clear();
        baril = new Baril();

        numeroNiveau += 1;
        initialiserDecors();
        poissonSpawnTimeline.stop();
        poissonSpawnTimeline = getTimeline(poissonsEnnemis);
        startPoissonSpawnTimeline();
        couleurArrierePlan = Color.hsb(r.nextInt(190, 271), 0.84, 1);
        timer.start();
    }

    public void startPoissonSpawnTimeline() {
        // Start timeline for spawning fish
        poissonSpawnTimeline.play();
    }

    private void dessinerBarreVie(GraphicsContext context) {
        // Dessiner le rectangle blanc proportionnel au nombre de vies
        double largeurBarreVie = 150; // Ajustez la largeur de la barre de vie selon vos besoins
        double hauteurBarreVie = 30; // Ajustez la hauteur de la barre de vie selon vos besoins
        double positionHorizontaleBarre = 20; // Ajustez la position X de la barre de vie selon vos besoins
        double positionVerticaleBarre = 10; // Ajustez la position Y de la barre de vie selon vos besoins

        double pourcentageVie = (double) charlotte.getNombreDeVie() / charlotte.getNombreDeVieMax();
        double largeurRemplie = pourcentageVie * largeurBarreVie;

        context.fillRect(positionHorizontaleBarre, positionVerticaleBarre, largeurBarreVie, hauteurBarreVie);
        context.setFill(Color.WHITE);
        context.fillRect(positionHorizontaleBarre, positionVerticaleBarre, largeurRemplie, hauteurBarreVie);
        context.setStroke(Color.WHITE);
        context.setLineWidth(2.0);
        context.strokeRect(positionHorizontaleBarre, positionVerticaleBarre, largeurBarreVie, hauteurBarreVie);

        //Ajouter le projectile utilisé à côté de la barre.
        context.drawImage(Projectile.imageProjectile, positionHorizontaleBarre + largeurBarreVie + 10,
                positionVerticaleBarre, Projectile.imageProjectile.getWidth(),
                Projectile.imageProjectile.getHeight());

        //dessiner les cœurs
        var proportionLargeurHauteur = 1872 / 365;
        context.drawImage(new Image(charlotte.getNombreDeVie() + "vies.png"), positionHorizontaleBarre,
                positionVerticaleBarre + hauteurBarreVie + 20, largeurBarreVie, largeurBarreVie / proportionLargeurHauteur);
    }

    public Color getCouleurArrierePlan() {
        return couleurArrierePlan;
    }

    public void lancerProjectile() {
        if (Projectile.peutLancer()) {

            if (choisirProjectile == 0) {
                dernierTypeProjectile = choisirProjectile;

                projectiles.add(new Etoile(charlotte.getX() + charlotte.w / 2 - 36 / 2,
                        charlotte.getY() + charlotte.h / 2 - 35 / 2, 36, 35));
                choisirImage = new Image("etoile.png");
                Projectile.imageProjectile = choisirImage;
            } else if (choisirProjectile == 1) {
                dernierTypeProjectile = choisirProjectile;
                projectiles.add(new BoiteDeSardine(charlotte.getX() + charlotte.w / 2 - 36 / 2,
                        charlotte.getY() + charlotte.h / 2 - 35 / 2, 36, 35));

                choisirImage = new Image("sardines.png");
                Projectile.imageProjectile = choisirImage;
            } else if (choisirProjectile == 2) {
                dernierTypeProjectile = choisirProjectile;
                projectiles.add(new Hippocampe(charlotte.getX() + charlotte.w / 2 - 36 / 2,
                        charlotte.getY() + charlotte.h / 2 - 35 / 2, 20, 36));
                projectiles.add(new Hippocampe(charlotte.getX() + charlotte.w / 2 - 36 / 2,
                        charlotte.getY() + charlotte.h / 2 - 35 / 2, 20, 36));
                projectiles.add(new Hippocampe(charlotte.getX() + charlotte.w / 2 - 36 / 2,
                        charlotte.getY() + charlotte.h / 2 - 35 / 2, 20, 36));


                choisirImage = new Image("hippocampe.png");
                Projectile.imageProjectile = choisirImage;
            }
        }
    }

    private Timeline getTimeline(LinkedList<Poisson> poissonsEnnemis) {
        var poissonSpawnTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1.75), event -> {
                    // Code pour créer un nouveau poisson

                    for (int i = 0; i < r.nextInt(1, 6); i++) {
                        var yInitiale = r.nextDouble((Main.HEIGHT / 5), (4 * Main.HEIGHT / 5));
                        var vyInitial = r.nextDouble(-100, 100);
                        var imagePoisson = new Image("poisson" + r.nextInt(1, 6) + ".png");
                        var proportionLargeurHauteur = imagePoisson.getWidth() / imagePoisson.getHeight();
                        var hauteur = r.nextInt(50, 121);
                        var largeur = hauteur * proportionLargeurHauteur;

                        poissonsEnnemis.add(new PoissonEnnemi(camera.getX() + Main.WIDTH, yInitiale,
                                largeur, hauteur, vyInitial, imagePoisson));
                    }
                })
        );
        poissonSpawnTimeline.setCycleCount(Timeline.INDEFINITE);
        return poissonSpawnTimeline;
    }

    private void updateTempsEcouleDepuisDebut() {
        long elapsedNano = System.nanoTime() - startTimeNano;
        tempsDepuisDebut = elapsedNano / 1_000_000_000;
    }


    private void Changerimage() {
        if (choisirProjectile == 0) {


            choisirImage = new Image("etoile.png");
            Projectile.imageProjectile = choisirImage;
        } else if (choisirProjectile == 1) {


            choisirImage = new Image("sardines.png");
            Projectile.imageProjectile = choisirImage;
        } else if (choisirProjectile == 2) {


            choisirImage = new Image("Hippocampe.png");
            Projectile.imageProjectile = choisirImage;
        }
    }

    public void afficherStatusJeu(GraphicsContext context) {
        context.setFont(Font.font("Jokerman", FontWeight.BOLD, 100));
        context.setTextAlign(TextAlignment.CENTER);
        // Montrer pour 4 secondes
        if (tempsDepuisDebut < 4) {
            context.setFill(Color.WHITE);
            context.fillText("Niveau" + numeroNiveau, Main.WIDTH / 2, Main.HEIGHT / 2);
        }

        // Afficher "FIN DE PARTIE" si Charlotte est morte
        if (charlotteMorte() && !finDePartieAffiche) {
            tempsDepuisPartieFini = System.nanoTime();
            context.setFill(Color.RED);
            context.fillText("FIN DE PARTIE", Main.WIDTH / 2, Main.HEIGHT / 2);
            finDePartieAffiche = true;
        }

        // Afficher "FIN DE PARTIE" pendant 3 secondes après la mort de Charlotte
        if (charlotteMorte() && finDePartieAffiche && (System.nanoTime() - tempsDepuisPartieFini) / 1_000_000_000 < 3) {
            context.setFill(Color.RED);
            context.fillText("FIN DE PARTIE", Main.WIDTH / 2, Main.HEIGHT / 2);
            if ((System.nanoTime() - tempsDepuisPartieFini) / 1_000_000_000 > 3)
                partieFini = true;
        }
    }

    private void afficherTemppsEcoule(GraphicsContext context) {
        context.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        context.setFill(Color.WHITE);
        context.fillText("Temps: " + tempsDepuisDebut + "s", 50, Main.HEIGHT / 2);

    }

    public boolean charlotteArriveFin() {
        return charlotte.getX() + charlotte.w >= Main.WIDTH * 8;
    }

    public boolean charlotteMorte() {
        return charlotte.estMorte();
    }

    public boolean isPartieFini() {
        return partieFini;
    }

    public void setPartieFini(boolean partieFini) {
        this.partieFini = partieFini;
    }

    public long getStartTimeNano() {
        return startTimeNano;
    }

    public long getTempsDepuisPartieFini() {
        return tempsDepuisPartieFini;
    }

    public void setTempsDepuisPartieFini(long tempsDepuisPartieFini) {
        this.tempsDepuisPartieFini = tempsDepuisPartieFini;
    }
}

