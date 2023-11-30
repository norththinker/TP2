package org.example;

import javafx.scene.image.Image;

import java.util.LinkedList;

public class Etoile extends Projectile{
    final static Image imageProjectile = new Image("etoile.png");
    public Etoile(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        imageObjet = imageProjectile;
        vx = 800;
    }
    @Override
    public void update(double deltaTemps, Camera camera) {
        x += deltaTemps * vx;

        gererHorsEcran(camera);
    }
    @Override
    public void testCollision(ObjetduJeu autreObjet) {
        if (this.enCollisionAvec(autreObjet)) {
            this.estTouche = true;
        }
    }

    @Override
    public void calculerForcesElectriques(LinkedList<Poisson> poissonsEnnemis, Personnage charlotte) {

    }
}
