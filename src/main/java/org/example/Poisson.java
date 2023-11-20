package org.example;

public abstract class Poisson extends ObjetduJeu{
    protected double limiter(double valeur, double min, double max) {
        return Math.min(max, Math.max(min, valeur));
    }

}
