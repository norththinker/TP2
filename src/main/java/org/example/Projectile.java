package org.example;

import javafx.scene.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Projectile extends ObjetduJeu {
    protected static long tempsDernierProjectile = 0;
    protected static final long delaiEntreProjectiles = 500_000_000;
    protected final static Image imageProjectile = new Image("etoile.png");
    protected boolean depasse = false;



    @Override
    protected void gererHorsEcran() {
        if (x > Main.HEIGHT){
            depasse = false;
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
