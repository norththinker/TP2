package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Poisson extends ObjetduJeu{
    protected double limiter(double valeur, double min, double max) {
        return Math.min(max, Math.max(min, valeur));
    }
    protected void gererHorsEcran() {
        y = Math.min(Main.HEIGHT - h, Math.max(0, y));
        x = Math.min(Main.WIDTH - w, Math.max(0, x));
    }

    @Override
    public abstract void testCollision(ObjetduJeu autreObjet);
}
