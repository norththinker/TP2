package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class ObjetduJeu {

    protected Image imageObjet;
    protected double x, y, w, h;
    protected double ax, ay;
    protected double vx, vy;
    protected boolean estTouche = false;

    public void draw(GraphicsContext context, Camera camera) {
        context.setFill(Color.rgb(200, 200, 200, 0));
        double xEcran = camera.calculerEcranX(x);
        double yEcran = camera.calculerEcranY(y);
        context.fillRect(xEcran, yEcran, w, h);

        context.drawImage(imageObjet, xEcran, yEcran, w, h);

        if (Main.debugMode) {
            context.setStroke(Color.YELLOW);
            context.setLineWidth(2.0);
            context.strokeRect(xEcran, yEcran, w, h);
        }
    }


    public abstract void update(double deltaTemps);

    public boolean enCollisionAvec(ObjetduJeu autreObjet) {

        return (this.x < autreObjet.x + autreObjet.w &&
                this.x + this.w > autreObjet.x &&
                this.y < autreObjet.y + autreObjet.h &&
                this.y + this.h > autreObjet.y);
    }

    public abstract void testCollision(ObjetduJeu autreObjet);
    protected abstract void gererHorsEcran();
    protected void gererEnX() {

        // x = Math.min(Main.WIDTH - w, Math.max(0, x));

        // Calculer la coordonnée maximale d'écran à droite à l'intérieur de la zone visible
        double coordonneeEcranDroiteMax = Camera.x + Main.WIDTH - w;

        // S'assurer que la coordonnée calculée ne dépasse pas la limite gauche de l'écran
        double coordonneeGaucheRestreinte = Math.max(Camera.x, x);

        // S'assurer que la coordonnée calculée ne tombe pas en dessous de la limite gauche de l'écran

        // Mettre à jour la coordonnée x de l'objet avec la valeur restreinte
        x = Math.min(coordonneeEcranDroiteMax, coordonneeGaucheRestreinte);
    }

    protected void gererEnY(){
        y = Math.min(Main.HEIGHT - h, Math.max(0, y));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public double getVx() {
        return vx;
    }

    public boolean isEstTouche() {
        return estTouche;
    }
}
