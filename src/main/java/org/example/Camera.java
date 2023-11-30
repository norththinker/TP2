package org.example;


public class Camera {
    private double x;
    private final double limiteDroite;  // Nouvelle variable pour la limite à droite
    double vitesseDefilementX;

    public Camera(double limiteDroite) {
        this.limiteDroite = limiteDroite;
    }

    public double calculerEcranX(double xMonde) {
        return xMonde - x;
    }


    /**
     * Fait avancer la caméra vers la droite automatiquement
     */
    public void update(double deltaTemps, Personnage charlotte) {
        boolean doitSuivre = (charlotte.getX() > (x + 0.2 * Main.WIDTH)) && charlotte.getVx() > 0 && x != limiteDroite - Main.WIDTH;
        
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
    }

    public double getX() {
        return x;
    }
    private boolean cameraArrete(){
        return x > limiteDroite - Main.WIDTH;
    }

    public void setX(double x) {
        this.x = x;
    }
}
