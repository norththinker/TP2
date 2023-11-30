package org.example;

import javafx.scene.image.Image;

import java.util.LinkedList;

public abstract class Projectile extends ObjetduJeu {
    protected static Image imageProjectile = new Image("etoile.png");
    protected boolean depasse = false;



    @Override
    protected void gererHorsEcran(Camera camera) {
        if (x > camera.getX() + Main.WIDTH){
            depasse = true;
        }
    }


    public boolean isDepasse() {
        return depasse;
    }

    public abstract void calculerForcesElectriques(LinkedList<Poisson> poissonsEnnemis, Personnage charlotte);
}
