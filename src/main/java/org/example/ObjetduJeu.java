package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ObjetduJeu {






    private double x, y;
    private double w;
    private double h;
    private double vx  ;
    private double vy  ;
    public boolean isEstTouche() {
        return estTouche;
    }

    public void setEstTouche(boolean estTouche) {
        this.estTouche = estTouche;
    }

    private boolean estTouche =false;
    public ObjetduJeu() {

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

    public ObjetduJeu(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

    }

    public void draw(GraphicsContext context) {



    }

    public void update(double deltaTemps) {


        x += deltaTemps * vx;
        y += deltaTemps * vy;




        if (y + h > Main.HEIGHT) {

            // Déplace les flocons vers le haut
            y = Main.HEIGHT - h;


        }

    }











}