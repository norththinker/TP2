package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * La classe abstraite représentant un objet de jeu.
 * Tous les objets de jeu étendent cette classe pour hériter des fonctionnalités communes.
 */
public abstract class ObjetduJeu {

    /**
     * Image représentant l'objet.
     */
    protected Image imageObjet;

    /**
     * Coordonnée x de l'objet.
     */
    protected double x;

    /**
     * Coordonnée y de l'objet.
     */
    protected double y;

    /**
     * Largeur de l'objet.
     */
    protected double w;

    /**
     * Hauteur de l'objet.
     */
    protected double h;

    /**
     * Accélération le long de l'axe x.
     */
    protected double ax;

    /**
     * Accélération le long de l'axe y.
     */
    protected double ay;

    /**
     * Vitesse le long de l'axe x.
     */
    protected double vx;

    /**
     * Vitesse le long de l'axe y.
     */
    protected double vy;

    /**
     * Indicateur indiquant si l'objet a été touché.
     */
    protected boolean estTouche = false;

    /**
     * Dessine l'objet à l'écran.
     *
     * @param context Le contexte graphique sur lequel dessiner l'objet.
     * @param camera  La caméra utilisée pour calculer les coordonnées à l'écran.
     */
    public void draw(GraphicsContext context, Camera camera) {
        context.setFill(Color.rgb(200, 200, 200, 0));

        // Calculer la coordonnée x à l'écran (c'est-à-dire avec respect à la caméra)
        double xEcran = camera.calculerEcranX(x);

        context.fillRect(xEcran, y, w, h);
        context.drawImage(imageObjet, xEcran, y, w, h);

        //On décide les contours jaunes si on est en mode debug.
        if (Partie.debugMode) {
            context.setStroke(Color.YELLOW);
            context.setLineWidth(2.0);
            context.strokeRect(xEcran, y, w, h);
        }

    }

    /**
     * Met à jour l'état de l'objet en fonction du temps écoulé et de la position de la caméra.
     *
     * @param deltaTemps Le temps écoulé depuis la dernière mise à jour.
     * @param camera     La caméra utilisée pour ajuster la position de l'objet.
     */
    public abstract void update(double deltaTemps, Camera camera);

    /**
     * Vérifie si l'objet est en collision avec un autre objet.
     *
     * @param autreObjet L'autre objet de jeu à vérifier pour la collision.
     * @return Vrai si une collision se produit, faux sinon.
     */
    public boolean enCollisionAvec(ObjetduJeu autreObjet) {
        return (this.getGauche() < autreObjet.getDroite() &&
                this.getDroite() > autreObjet.getGauche() &&
                this.getHaut() < autreObjet.getBas() &&
                this.getBas() > autreObjet.getHaut());
    }

    /**
     * Gère la collision avec un autre objet de jeu.
     *
     * @param autreObjet L'autre objet de jeu impliqué dans la collision.
     */
    public abstract void testCollision(ObjetduJeu autreObjet);

    /**
     * Gère les scénarios hors écran en fonction de la position de la caméra.
     *
     * @param camera La caméra utilisée pour déterminer les limites de l'écran.
     */
    protected abstract void gererHorsEcran(Camera camera);

    /**
     * Gère la coordonnée x lorsqu'elle est hors écran, la restreignant à la zone visible.
     *
     * @param camera La caméra utilisée pour déterminer les limites de l'écran.
     */
    protected void gererEnX(Camera camera) {

        // Calculer la coordonnée maximale d'écran à droite à l'intérieur de la zone visible
        double coordonneeEcranDroiteMax = camera.getX() + Main.WIDTH - w;

        // S'assurer que la coordonnée calculée ne dépasse pas la limite gauche de l'écran
        double coordonneeGaucheRestreinte = Math.max(camera.getX(), x);


        // Mettre à jour la coordonnée x de l'objet avec les deux valeurs restreintes
        x = Math.min(coordonneeEcranDroiteMax, coordonneeGaucheRestreinte);
    }

    /**
     * Gère l'aspect de la coordonnée y lorsqu'elle est hors écran, la restreignant à la zone visible.
     */
    protected void gererEnY() {
        y = Math.min(Main.HEIGHT - h, Math.max(0, y));
    }

    public double getDroite(){
        return x + w;
    }
    public double getGauche(){
        return x;
    }
    public double getHaut() {
        return y;
    }
    public double getBas() {
        return y + h;
    }
    /**
     * Obtient la coordonnée x de l'objet.
     *
     * @return La coordonnée x de l'objet.
     */
    public double getX() {
        return x;
    }

    /**
     * Définit la coordonnée x de l'objet.
     *
     * @param x La nouvelle coordonnée x de l'objet.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Obtient la coordonnée y de l'objet.
     *
     * @return La coordonnée y de l'objet.
     */
    public double getY() {
        return y;
    }

    /**
     * Obtient la vitesse le long de l'axe x de l'objet.
     *
     * @return La vitesse le long de l'axe x de l'objet.
     */
    public double getVx() {
        return vx;
    }

    /**
     * Indique si l'objet a été touché.
     *
     * @return Vrai si l'objet a été touché, faux sinon.
     */
    public boolean isEstTouche() {
        return estTouche;
    }
}

