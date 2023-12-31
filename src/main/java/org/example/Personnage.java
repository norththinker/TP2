package org.example;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Personnage extends Poisson {

    private final Image charlotteNormalImage = new Image("charlotte.png");
    private final Image charlotteAvantImage = new Image("charlotte-avant.png");
    private final Image charlotteOutchImage = new Image("charlotte-outch.png");

    public void setNombreDeVie(int nombreDeVie) {
        this.nombreDeVie = nombreDeVie;
    }

    private int nombreDeVieMax = 4;
    private int nombreDeVie = nombreDeVieMax;

    private boolean clignote = false;
    private long tempsClignotageCommence;
    protected static long tempsDernierProjectile = 0;
    protected static final long delaiEntreProjectiles = 500_000_000;
    public Personnage(double x, double y, double w, double h) {
        imageObjet = new Image("charlotte.png");
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void update(double deltaTemps, Camera camera) {
        if (clignote) {
            continueClignotage();
        }
        double vitesseMax = 300;
        vx = limiter(vx + deltaTemps * ax, -vitesseMax, vitesseMax);
        vy = limiter(vy + deltaTemps * ay, -vitesseMax, vitesseMax);
        x += deltaTemps * vx;
        y += deltaTemps * vy;

        gererCommandes();
        gererHorsEcran(camera);
    }

    private void gererCommandes() {
        boolean left = Input.isKeyPressed(KeyCode.LEFT);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);
        boolean up = Input.isKeyPressed(KeyCode.UP);
        boolean down = Input.isKeyPressed(KeyCode.DOWN);

        boolean charlotteBouge = left || right || up || down;

        toCharlotteAvant(charlotteBouge);


        ax = calculerAx(left, right, vx);
        ay = calculerAy(up, down, vy);
    }


    private void toCharlotteAvant(boolean charlotteBouge) {
        if (charlotteBouge && !clignote)
            imageObjet = charlotteAvantImage;

        else if (!charlotteBouge && !clignote) {
            imageObjet = charlotteNormalImage;
        }
    }
    private double calculerAcceleration(boolean positif, boolean negatif, double vitesse, double accelerationPourArreter) {
        if (positif) {
            return negatif ? 0 : 1000;
        } else {
            return negatif ? -1000 : -vitesse * accelerationPourArreter;
        }
    }

    private double calculerAx(boolean left, boolean right, double vx) {
        return calculerAcceleration(right, left, vx, 5);
    }

    private double calculerAy(boolean up, boolean down, double vy) {
        return calculerAcceleration(down, up, vy, 5);
    }

    @Override
    protected void gererHorsEcran(Camera camera) {
        gererEnX(camera);
        gererEnY();
    }

    @Override
    public void testCollision(ObjetduJeu autreObjet) {
        if (this.enCollisionAvec(autreObjet)) {
            this.estTouche = true;
            commencerClignotage();
        }
    }

    public void commencerClignotage() {
        if (!clignote) {
            clignote = true;

            if (nombreDeVie > 0)
                nombreDeVie--;

            tempsClignotageCommence = System.nanoTime();
            imageObjet = charlotteOutchImage;
        }
    }

    public void continueClignotage() {
        long tempsEcoule = System.nanoTime() - tempsClignotageCommence;
        long dureeClignotage = 2000000000; // 2 secondes

        if (tempsEcoule < dureeClignotage) {
            long intervalClignotage = 250000000; // 0.25 seconde
            long flashCycleTime = tempsEcoule % (2 * intervalClignotage);

            if (flashCycleTime < intervalClignotage) {
                // Montrer charlotteOutchImage pour les premiers 0.25 seconde
                imageObjet = charlotteOutchImage;
            } else {
                // Montrer aucune image pour les autres 0.25 seconde
                imageObjet = null;
            }
        } else {
            // Après 2 secondes, le clignotage finit et on change l'image à charlotteNormalImage
            imageObjet = charlotteNormalImage;
            clignote = false;
        }
    }
    public boolean peutLancer() {
        long tempActuel = System.nanoTime();
        if (tempActuel - tempsDernierProjectile > delaiEntreProjectiles) {
            tempsDernierProjectile = tempActuel;
            return true;
        }
        return false;
    }
    public boolean estMorte(){
        return nombreDeVie == 0;
    }
    public int getNombreDeVieMax() {
        return nombreDeVieMax;
    }

    public int getNombreDeVie() {
        return nombreDeVie;
    }

}