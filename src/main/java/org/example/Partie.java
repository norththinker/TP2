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
    private Image imageProjectile;
    private TypeProjectile projectileActuel;
    public static double longeurNiveau = Main.WIDTH*8;
    private Camera camera = new Camera(longeurNiveau);
    private double tempsPoisson;
    private Personnage charlotte = new Personnage(0, Main.HEIGHT / 2, 102, 90);
    private Random r = new Random();
    public static boolean debugMode = false;
    private ArrayList<Projectile> projectiles;



    private LinkedList<Decor> decors = new LinkedList<>();
    private MediaPlayer mediaPlayer;
    private Color couleurArrierePlan;
    private LinkedList<PoissonEnnemi> poissonsEnnemis;
    private Timeline poissonSpawnTimeline;
    private long tempsDepuisDebut;
    private long startTimeNano;
    public static int numeroNiveau;
    private Baril baril;
    private boolean partieFini;
    private long tempsDepuisPartieFini;
    private boolean changerEcranAcceuil;

    public Partie() {
        imageProjectile = new Image("etoile.png");
        numeroNiveau = 1;
        couleurArrierePlan = Color.hsb(r.nextInt(190, 271), 0.84, 1);
        poissonsEnnemis = new LinkedList<>();
        projectileActuel = TypeProjectile.ETOILE;
        changerEcranAcceuil = false;
        partieFini = false;
        baril = new Baril();
        projectiles = new ArrayList<>();
        initialiserDecors();
        initialiserMedia();

        poissonSpawnTimeline = getTimeline(poissonsEnnemis);
    }

    private void initialiserDecors() {

        Decor decorActuel = new Decor(0, Main.HEIGHT - 119 + 10, new Image("decor1.png"));
        decors.add(decorActuel);
        int nombreDecors = 1;

        while (decorActuel.getX() + decorActuel.getW() < Partie.longeurNiveau) {
            Image imageDecor = new Image("decor" + r.nextInt(1, 7) + ".png");
            decorActuel = new Decor(0, 0, imageDecor);
            decorActuel.placerDecorSuivant(decors.get(nombreDecors - 1));
            decors.add(decorActuel);
            nombreDecors++;
        }
    }

    private void initialiserMedia() {
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
        // Check pour collisions
        for (PoissonEnnemi ennemi : poissonsEnnemis) {


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

        if (baril.isTouche() && !baril.isDejaTouche()) {

            TypeProjectile projectileChoisi = TypeProjectile.values()[baril.getChoix()];

            while (projectileActuel == projectileChoisi) {
                projectileChoisi = TypeProjectile.values()[r.nextInt(0, 3)];
                
            }

            projectileActuel = projectileChoisi;
            baril.setDejaTouche(true);


        }

        if (projectileActuel == TypeProjectile.SARDINES) {
            for (Projectile projectile : projectiles) {
                projectile.calculerForcesElectriques(poissonsEnnemis, charlotte);
            }
        }

        for (Poisson poissonEnnemi : poissonsEnnemis) {
            charlotte.testCollision(poissonEnnemi);
        }

        // Enlever les poissons ennemis tués
        poissonsEnnemis.removeIf(PoissonEnnemi::isEstTouche);
        poissonsEnnemis.removeIf(PoissonEnnemi::isDepasse);
    }

    public void draw(GraphicsContext context) {

        context.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);


        // Draw entities on the canvas


        baril.draw(context, camera,poissonsEnnemis.size(),projectiles.size(),charlotte.x/longeurNiveau*100);
        for (Decor decor : decors) {
            decor.draw(context, camera,poissonsEnnemis.size(),projectiles.size(),charlotte.x/longeurNiveau*100);
        }
        for (Poisson poissonEnnemi : poissonsEnnemis) {
            // Utilisez la caméra pour dessiner en prenant en compte sa position
            poissonEnnemi.draw(context, camera,poissonsEnnemis.size(),projectiles.size(),charlotte.x/longeurNiveau*100);
        }

        for (Projectile projectile : projectiles) {
            if (projectile != null) {
                projectile.draw(context, camera,poissonsEnnemis.size(),projectiles.size(),charlotte.x/longeurNiveau*100);
            }
        }
        charlotte.draw(context, camera,poissonsEnnemis.size(),projectiles.size(),charlotte.x/longeurNiveau*100);

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
        changerEcranAcceuil = false;


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
        context.drawImage(imageProjectile, positionHorizontaleBarre + largeurBarreVie + 10,
                positionVerticaleBarre, imageProjectile.getWidth(),
                imageProjectile.getHeight());

        //dessiner les cœurs
        var proportionLargeurHauteur = 1872 / 365;
        context.drawImage(new Image(charlotte.getNombreDeVie() + "vies.png"), positionHorizontaleBarre,
                positionVerticaleBarre + hauteurBarreVie + 20, largeurBarreVie, largeurBarreVie / proportionLargeurHauteur);
    }

    public Color getCouleurArrierePlan() {
        return couleurArrierePlan;
    }

    public void lancerProjectile() {
        // Vérifie si le personnage peut lancer un projectile
        if (charlotte.peutLancer()) {
            // Calcule la position initiale du projectile par rapport à Charlotte
            double positionX = charlotte.getX() + charlotte.w / 2 - 36 / 2;
            double positionY = charlotte.getY() + charlotte.h / 2 - 35 / 2;

            // Crée et ajoute le projectile en fonction du type actuel
            switch (projectileActuel) {
                case ETOILE:
                    projectiles.add(new Etoile(positionX, positionY, 36, 35)); // Crée une étoile
                    break;
                case SARDINES:
                    projectiles.add(new BoiteDeSardine(positionX, positionY, 35, 29)); // Crée une boîte de sardines
                    break;
                case HIPPOCAMPE:
                    for (int i = 0; i < 3; i++) {
                        projectiles.add(new Hippocampe(positionX, positionY, 20, 36)); // Crée trois hippocampes
                    }
                    break;
            }
        }
    }


    private Timeline getTimeline(LinkedList<PoissonEnnemi> poissonsEnnemis) {
        tempsPoisson = 0.75 + (1 / Math.sqrt(numeroNiveau));
        var poissonSpawnTimeline = new Timeline(

                new KeyFrame(Duration.seconds(tempsPoisson), event -> { // Cette methode permet de crée un poissons différent
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
        if (imageProjectile != null) {
            imageProjectile= new Image(projectileActuel.getImage());
        }
    }


    public void afficherStatusJeu(GraphicsContext context) {
        context.setFont(Font.font("Jokerman", FontWeight.BOLD, 100));
        context.setTextAlign(TextAlignment.CENTER);
        // Montrer pour 4 secondes
        if (tempsDepuisDebut < 4 && !charlotteMorte()) {
            context.setFill(Color.WHITE);
            context.fillText("Niveau" + numeroNiveau, Main.WIDTH / 2, Main.HEIGHT / 2);
        }
        if (!charlotteMorte())
            return;

        // Afficher "FIN DE PARTIE" si Charlotte est morte
        if (charlotteMorte() && !partieFini) {
            tempsDepuisPartieFini = System.nanoTime();
            context.setFill(Color.RED);
            context.fillText("FIN DE PARTIE", Main.WIDTH / 2, Main.HEIGHT / 2);
            partieFini = true;
        }

        // Afficher "FIN DE PARTIE" pendant 3 secondes après la mort de Charlotte
        if (partieFini && (System.nanoTime() - tempsDepuisPartieFini) / 1_000_000_000 < 3) {
            context.setFill(Color.RED);
            context.fillText("FIN DE PARTIE", Main.WIDTH / 2, Main.HEIGHT / 2);
        } else
            changerEcranAcceuil = true;
    }

    private void afficherTemppsEcoule(GraphicsContext context) {
        context.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        context.setFill(Color.WHITE);
        context.fillText("Temps: " + tempsDepuisDebut + "s", 50, Main.HEIGHT / 2);

    }

    public void Changernombredeviedecharlotte() {
        partieFini = false;


        charlotte.setNombreDeVie(4);


    }

    public boolean charlotteArriveFin() {
        return charlotte.getX() + charlotte.w >= Main.WIDTH * 8;
    }

    public boolean charlotteMorte() {
        return charlotte.estMorte();
    }

    public void setProjectileActuel(TypeProjectile projectileActuel) {
        this.projectileActuel = projectileActuel;
    }

    public boolean isChangerEcranAcceuil() {
        return changerEcranAcceuil;
    }

}

