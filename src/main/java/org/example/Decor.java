package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class Decor {

    private double x, y;
    public static final double w = 80;
    public static final double h = 119;
    private Image imageDecor;
    private Random random = new Random();

    public Decor(double x, double y, Image imageDecor) {
        this.x = x;
        this.y = y;
        this.imageDecor = imageDecor;
    }

    public void placerDecorSuivant(Decor decorPrecedent) {
        this.x = decorPrecedent.x + w + genererEspacement();
        this.y =  placerEnY();
    }
    public double placerEnY(){
        // Bas de l'écran - hauteur du décor + décalage supplémentaire
        return Main.HEIGHT - h + 10;
    }

    public void draw(GraphicsContext context, Camera camera) {
        context.setFill(Color.rgb(200, 200, 200, 0));
        double xEcran = camera.calculerEcranX(x);
        double yEcran = camera.calculerEcranY(y);
        context.fillRect(xEcran, yEcran, w, h);

        context.drawImage(imageDecor, xEcran, yEcran, w, h);

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


