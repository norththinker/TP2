package org.example;

import javafx.scene.image.Image;

import java.util.LinkedList;

public abstract class Projectile extends ObjetduJeu {
    protected static long tempsDernierProjectile = 0;
    protected static final long delaiEntreProjectiles = 500_000_000;
    protected static Image imageProjectile = new Image("etoile.png");
    protected boolean depasse = false;



    @Override
    protected void gererHorsEcran(Camera camera) {
        if (x > camera.getX() + Main.WIDTH){
            depasse = true;
        }
    }
    public static boolean peutLancer() {
        long tempActuel = System.nanoTime();
        if (tempActuel - tempsDernierProjectile > delaiEntreProjectiles) {
            tempsDernierProjectile = tempActuel;
            return true;
        }
        return false;
    }

    public boolean isDepasse() {
        return depasse;
    }

    public abstract void calculerForcesElectriques(LinkedList<Poisson> poissonsEnnemis, Personnage charlotte);
}
