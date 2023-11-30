package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class Decor extends ObjetduJeu{

    private Random random = new Random();

    public Decor(double x, double y, Image imageDecor) {
        this.x = x;
        this.y = y;
        this.w = 80;
        this.h = 119;
        imageObjet = imageDecor;
    }

    public void placerDecorSuivant(Decor decorPrecedent) {
        this.x = decorPrecedent.x + w + genererEspacement();
        this.y =  placerEnY();
    }
    public double placerEnY(){
        // Bas de l'écran - hauteur du décor + décalage supplémentaire
        return Main.HEIGHT - h + 10;
    }
    @Override
    public void update(double deltaTemps, Camera camera) {

    }

    @Override
    public void testCollision(ObjetduJeu autreObjet) {

    }

    @Override
    protected void gererHorsEcran(Camera camera) {

    }

    private double genererEspacement() {
        return random.nextInt(50,101);
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


    public double getH() {
        return h;
    }
}


