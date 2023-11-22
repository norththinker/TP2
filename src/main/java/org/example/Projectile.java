package org.example;

import javafx.scene.image.Image;

public abstract class Projectile extends ObjetduJeu {
    protected static long tempsDernierProjectile = 0;
    protected static final long delaiEntreProjectiles = 500_000_000;
    protected final static Image imageProjectile = new Image("etoile.png");
    protected boolean depasse = false;



    @Override
    protected void gererHorsEcran() {
        if (x > Camera.x + Main.WIDTH){
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

}
