package org.example;

import javafx.scene.image.Image;

import java.util.LinkedList;

public class  BoiteDeSardine extends Projectile {

    private  double forceEnX  ;
    private  double forceEnY  ;



    public BoiteDeSardine(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        imageObjet = new Image("sardines.png");
        vx = 300;
    }

    @Override
    public void testCollision(ObjetduJeu autreObjet) {

    }

    /**
     * Met à jour la position de l'objet en fonction du temps, en tenant compte de l'accélération.
     * Limite la vitesse maximale verticale entre -500px/s et +500px/s.
     * Limite la vitesse horizontale entre 300px/s et 500px/s.
     * Gère la position verticale et horizontale par rapport à l'écran.
     *
     * @param deltaTemps Temps écoulé depuis la dernière mise à jour.
     * @param camera     Caméra utilisée pour le suivi.
     */
    public void update(double deltaTemps, Camera camera) {

        vx += deltaTemps * ax;
        vy += deltaTemps * ay;

        // Limiter la vitesse finale horizontale entre 300px/s et 500px/s
        vx = Math.max(300, Math.min(500, vx));

        // Limiter la vitesse finale verticale entre -500px/s et +500px/s
        vy = Math.max(-500, Math.min(500, vy));


        x += deltaTemps * vx;
        y += deltaTemps * vy;

        // Gérer la position verticale par rapport à l'écran
        gererEnY();

        // Gérer la position horizontale par rapport à l'écran
        gererHorsEcran(camera);
    }

    /**
     * Calcule les forces électriques entre l'objet et les poissons ennemis.
     * Applique les forces résultantes à l'accélération de l'objet.
     *
     * @param poissonsEnnemis Liste des poissons ennemis à considérer pour les forces électriques.
     * @param charlotte       Personnage utilisé pour déterminer la direction des forces électriques.
     */
    public void calculerForcesElectriques(LinkedList<PoissonEnnemi> poissonsEnnemis, Personnage charlotte) {
        // Réinitialiser les forces
        forceEnX = 0;
        forceEnY = 0;

        // Calculer les forces électriques
        for (PoissonEnnemi poisson : poissonsEnnemis) {
            if (poisson.getX() > x && poisson.getX() > charlotte.x) {
                double deltaX = x - poisson.getX();
                double deltaY = y - poisson.getY();
                double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

                // Éviter une division par zéro
                if (distance < 0.01) {
                    distance = 0.01;
                }

                // Loi de Coulomb
                double forceElectrique = (1000 * -100 * 200) / Math.pow(distance, 2);

                // Calculer les proportions du vecteur force
                double proportionX = deltaX / distance;
                double proportionY = deltaY / distance;

                // Appliquer les forces en x et y et leur somme
                forceEnX += forceElectrique * proportionX;
                forceEnY += forceElectrique * proportionY;
            }
        }

        // Appliquer les forces résultantes à l'accélération
        ax = forceEnX;
        ay = forceEnY;
    }

    protected void gererEnY() {
        if (y + (h / 2) > Main.HEIGHT || y - (h / 2) < 0) {
            // Rebondir vers le bas si la boîte de sardines touche le haut de l'écran
            vy *= -0.9;
            ay = -ay;
        }

        // Limiter la position en y
        y = Math.min(Main.HEIGHT - h / 2, Math.max(h / 2, y));
    }


}
