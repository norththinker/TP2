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

    public void draw(GraphicsContext context) {
        context.setFill(Color.rgb(200, 200, 200, 0));
        context.fillRect(x, y, w, h);
        context.drawImage(imageObjet, x, y);
        context.setStroke(Color.YELLOW);
        context.setLineWidth(2.0);
        context.strokeRect(x, y, w, h);
    }

    public abstract void update(double deltaTemps);

    public boolean enCollisionAvec(ObjetduJeu autreObjet) {

        return (this.x < autreObjet.x + autreObjet.w &&
                this.x + this.w > autreObjet.x &&
                this.y < autreObjet.y + autreObjet.h &&
                this.y + this.h > autreObjet.y);
    }

    public abstract void testCollision(ObjetduJeu autreObjet);

    public Image getImageObjet() {
        return imageObjet;
    }

    public void setImageObjet(Image imageObjet) {
        this.imageObjet = imageObjet;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return ay;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public boolean isEstTouche() {
        return estTouche;
    }

    public void setEstTouche(boolean estTouche) {
        this.estTouche = estTouche;
    }
}
