package org.example;

public abstract class Poisson extends ObjetduJeu{
    protected double limiter(double valeur, double min, double max) {
        var restraindreAuMinimum = Math.max(min, valeur);
        return Math.min(max, restraindreAuMinimum);
    }

}
