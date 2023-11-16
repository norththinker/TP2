package org.example;

import javafx.scene.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Projectile extends ObjetduJeu {

    public Projectile(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        imageObjet = new Image("etoile.png");
        vx = 300;
    }

    @Override
    public void update(double deltaTemps) {
        x += deltaTemps * vx;

        if (y + h > Main.HEIGHT) {
            y = Main.HEIGHT - h;
        }
    }

    @Override
    public void testCollision(ObjetduJeu autreObjet) {
        if (this.enCollisionAvec(autreObjet)) {
            this.estTouche = true;
        }
    }
}
