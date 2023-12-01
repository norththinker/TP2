package org.example;

import javafx.scene.image.Image;

import java.util.LinkedList;
import java.util.Random;

public class Hippocampe extends Projectile {
    private double amplitude;
    private double yInitial;
    private double period;
    private double tempEcoule;
    public Hippocampe(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        imageObjet = new Image("hippocampe.png");
        vx = 500; // Vitesse horizontale de 500px/s vers la droite
        Random r = new Random();

        amplitude = (r.nextDouble() * 30) + 30; // Amplitude aléatoire entre 30 et 60
        amplitude *= (r.nextBoolean() ? 1 : -1); // Choix aléatoire du signe

        period = 1.0 + r.nextDouble() * 2.0; // Période aléatoire entre 1.0 et 3.0
        yInitial = y;
    }

    @Override
    public void update(double deltaTemps, Camera camera) {
        tempEcoule += deltaTemps;
        x += deltaTemps * vx;

        y = yInitial + amplitude * Math.sin(2 * Math.PI * tempEcoule / period);
        gererHorsEcran(camera);
    }

    @Override
    public void testCollision(ObjetduJeu autreObjet) {
        // Implémentez la logique de collision si nécessaire
    }


    @Override
    public void calculerForcesElectriques(LinkedList<PoissonEnnemi> poissonsEnnemis, Personnage charlotte) {

    }

}
