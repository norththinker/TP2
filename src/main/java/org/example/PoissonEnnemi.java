package org.example;

import javafx.scene.image.Image;

public class PoissonEnnemi extends Poisson {

    public PoissonEnnemi(double x, double y, double w, double h, double vy) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.vy = vy;
        vx = -(100 * Math.pow(1, 0.33) + 200);
        imageObjet = new Image("poisson1.png");
    }


    @Override
    public void update(double deltaTemps) {
        x += deltaTemps * vx;
        y += deltaTemps * vy;

        if (y + h > Main.HEIGHT) {
            y = Main.HEIGHT - h;
        }
    }

    @Override
    public void testCollision(ObjetduJeu autreObjet) {
        if (this.enCollisionAvec(autreObjet)){
            this.estTouche = true;
        }
    }
}
