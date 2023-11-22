package org.example;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Personnage extends Poisson {

    private final Image charlotteNormalImage = new Image("charlotte.png");
    private final Image charlotteAvantImage = new Image("charlotte-avant.png");
    private final Image charlotteOutchImage = new Image("charlotte-outch.png");
    private int nombreDeVieMax = 4;
    private int nombreDeVie = nombreDeVieMax;

    private boolean clignote = false;
    private long tempsClignotageCommence;

    public Personnage(double x, double y, double w, double h) {
        imageObjet = new Image("charlotte.png");
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void update(double deltaTemps) {
        if (clignote) {
            continueClignotage();
        }

        double vitesseMax = 300;
        vx = limiter(vx + deltaTemps * ax, -vitesseMax, vitesseMax);
        vy = limiter(vy + deltaTemps * ay, -vitesseMax, vitesseMax);
        x += deltaTemps * vx;
        y += deltaTemps * vy;

        gererCommandes();
        gererHorsEcran();
    }

    private void gererCommandes() {
        boolean left = Input.isKeyPressed(KeyCode.LEFT);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);
        boolean up = Input.isKeyPressed(KeyCode.UP);
        boolean down = Input.isKeyPressed(KeyCode.DOWN);

        boolean charlotteBouge = left || right || up || down;

        toCharlotteAvant(charlotteBouge);

        ax = left ? -1000 : right ? 1000 : -vx*5;
        ay = up ? -1000 : down ? 1000 : -vy*5;
    }

    private void toCharlotteAvant(boolean charlotteBouge) {
        if (charlotteBouge && !clignote)
            imageObjet = charlotteAvantImage;

        else if (!charlotteBouge && !clignote) {
            imageObjet = charlotteNormalImage;
        }
    }

    @Override
    protected void gererHorsEcran() {
        gererEnX();
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

    public int getNombreDeVieMax() {
        return nombreDeVieMax;
    }

    public int getNombreDeVie() {
        return nombreDeVie;
    }
}