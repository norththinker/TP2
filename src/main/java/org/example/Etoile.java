package org.example;

import javafx.scene.image.Image;

public class Etoile extends Projectile{
    final static Image imageProjectile = new Image("etoile.png");
    public Etoile(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        imageObjet = imageProjectile;
        vx = 300;
    }
    @Override
    public void update(double deltaTemps) {
        x += deltaTemps * vx;

        gererHorsEcran();
    }
    @Override
    public void testCollision(ObjetduJeu autreObjet) {
        if (this.enCollisionAvec(autreObjet)) {
            this.estTouche = true;
        }
    }
}
