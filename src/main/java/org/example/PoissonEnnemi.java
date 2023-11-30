package org.example;

import javafx.scene.image.Image;

public class PoissonEnnemi extends Poisson {

    public PoissonEnnemi(double x, double y, double w, double h, double vy, Image imagePoisson) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.vy = vy;
        this.ax = -500;
        vx = -(100 * Math.pow(Partie.numeroNiveau, 0.33) + 200);
        imageObjet = imagePoisson;
    }


    @Override
    public void update(double deltaTemps, Camera camera) {
        x += deltaTemps * vx;
        y += deltaTemps * vy;

        vx += deltaTemps * ax;

        gererHorsEcran(camera);
    }

    @Override
    protected void gererHorsEcran(Camera camera) {
        gererEnY();
    }

    @Override
    public void testCollision(ObjetduJeu autreObjet) {
        if (this.enCollisionAvec(autreObjet)){
            this.estTouche = true;
        }
    }
}
