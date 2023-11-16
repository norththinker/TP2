package org.example;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Personnage extends Poisson {

    private Image charlotteImageActuelle = new Image("charlotte.png");
    private final Image charlotteNormalImage = new Image("charlotte.png");
    private final Image charlotteAvantImage = new Image("charlotte-avant.png");
    private final Image charlotteOutchImage = new Image("charlotte-outch.png");

    private boolean clignote = false;
    private long flashingStartTime;

    public Personnage(double x, double y, double w, double h) {
        imageObjet = charlotteImageActuelle;
        this.x = Main.WIDTH / 2;
        this.y = Main.HEIGHT / 2;
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

        ax = left ? -1000 : right ? 1000 : -vx * 10;
        ay = up ? -1000 : down ? 1000 : -vy * 10;
    }

    private void toCharlotteAvant(boolean charlotteBouge) {
        if (charlotteBouge && !clignote)
            imageObjet = charlotteAvantImage;

        else if (!charlotteBouge && !clignote) {
            imageObjet = charlotteNormalImage;
        }
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
            flashingStartTime = System.nanoTime();
            imageObjet = charlotteOutchImage;
        }
    }

    public void continueClignotage() {
        long tempsEcoule = System.nanoTime() - flashingStartTime;
        long dureeClignotage = 2000000000; // 2 secondes

        if (tempsEcoule < dureeClignotage) {
            long intervalClignotage = 250000000; // 0.25 seconde
            long flashCycleTime = tempsEcoule % (2 * intervalClignotage);

            if (flashCycleTime < intervalClignotage) {
                // Montrer charlotteOutchImage pour lse premiers 0.25 seconde
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
}