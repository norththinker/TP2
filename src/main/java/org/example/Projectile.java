package org.example;


import java.util.LinkedList;

public abstract class Projectile extends ObjetduJeu {

    protected boolean depasse = false;



    @Override
    protected void gererHorsEcran(Camera camera) {
        if (x > camera.getX() + Main.WIDTH){
            depasse = true;
        }
    }


    public boolean isDepasse() {
        return depasse;
    }

    public abstract void calculerForcesElectriques(LinkedList<PoissonEnnemi> poissonsEnnemis, Personnage charlotte);
}
