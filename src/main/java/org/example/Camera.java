package org.example;


public class Camera {
    public static double x, y;
    private final double limiteDroite;  // Nouvelle variable pour la limite à droite
    private boolean doitSuivre = false;  // Nouvelle variable pour indiquer si la caméra doit suivre
    double vitesseDefilementX;

    public Camera(double limiteDroite) {
        this.limiteDroite = limiteDroite;
    }

    public double calculerEcranX(double xMonde) {
        return xMonde - x;
    }

    public double calculerEcranY(double yMonde) {
        return yMonde - y;
    }

    /**
     * Fait avancer la caméra vers la droite automatiquement
     */
    public void update(double deltaTemps, Personnage charlotte) {

        if (doitSuivre) {
            vitesseDefilementX = charlotte.getVx();
            // La caméra bouge vers la droite automatiquement
            x += deltaTemps * vitesseDefilementX;
        }
    }

    public void suivre(Personnage charlotte) {
        double limiteGauche = x;

        if (charlotte.getX() < limiteGauche) {
            // Ajuster la caméra si le personnage est trop à gauche
            x = charlotte.getX() - limiteGauche;
        }
        if (cameraArrete()) {
            // Si oui, arrêter la caméra à la limite
            x = limiteDroite - Main.WIDTH;
        }

        // Activer le suivi lorsque Charlotte atteint 1/5 de la caméra
        doitSuivre = ((charlotte.getX() > (limiteGauche + 0.2 * Main.WIDTH)) && charlotte.getVx() > 0 && x != limiteDroite - Main.WIDTH);
    }

    public double getX() {
        return x;
    }
    private boolean cameraArrete(){
        return x > limiteDroite - Main.WIDTH;
    }
}
