package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Personnage extends ObjetduJeu {

    private double x, y, w, h;
    private double vx, vy, ax, ay;
    private boolean estTouche = false;
    private int compteur;
    private Image charlotteImageActuelle = new Image("charlotte.png");
    private final Image charlotteNormalImage = new Image("charlotte.png");
    private final Image charlotteAvantImage = new Image("charlotte-avant.png");

    public Personnage(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void draw(GraphicsContext context) {
        context.setFill(Color.rgb(200, 200, 200, 0));
        context.fillRect(x, y, w, h);
        context.drawImage(charlotteImageActuelle, x, y);
        context.setStroke(Color.YELLOW);
        context.setLineWidth(2.0);
        context.strokeRect(x, y, w, h);
    }

    public void update(double deltaTemps) {
        double vitesseMax = 300;
        vx = limiter(vx + deltaTemps * ax, -vitesseMax, vitesseMax);
        vy = limiter(vy + deltaTemps * ay, -vitesseMax, vitesseMax);
        x += deltaTemps * vx;
        y += deltaTemps * vy;

        gererCommandes();
        gererHorsEcran();
    }

    private void gererCommandes() {
        boolean left = Input.isKeyPressed(KeyCode.LEFT);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);
        boolean up = Input.isKeyPressed(KeyCode.UP);
        boolean down = Input.isKeyPressed(KeyCode.DOWN);

        boolean charlotteBouge = left || right || up || down;

        toCharlotteAvant(charlotteBouge);

        ax = left ? -1000 : right ? 1000 : -vx * 10;
        ay = up ? -1000 : down ? 1000 : -vy * 10;
    }

    private void toCharlotteAvant(boolean charlotteBouge) {
        if (charlotteBouge)
            charlotteImageActuelle = charlotteAvantImage;
        else
            charlotteImageActuelle = charlotteNormalImage;
    }

    private void gererHorsEcran() {
        y = Math.min(Main.HEIGHT - h, Math.max(0, y));
        x = Math.min(Main.WIDTH - w, Math.max(0, x));
    }

    private double limiter(double valeur, double min, double max) {
        return Math.min(max, Math.max(min, valeur));
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public double getW() {
        return w;
    }

    @Override
    public void setW(double w) {
        this.w = w;
    }

    @Override
    public double getH() {
        return h;
    }

    @Override
    public void setH(double h) {
        this.h = h;
    }

    @Override
    public double getVx() {
        return vx;
    }

    @Override
    public void setVx(double vx) {
        this.vx = vx;
    }

    @Override
    public double getVy() {
        return vy;
    }
    @Override
    public void setVy(double vy) {
        this.vy = vy;
    }
    @Override
    public boolean isEstTouche() {
        return estTouche;
    }
    @Override
    public void setEstTouche(boolean estTouche) {
        this.estTouche = estTouche;
    }
    public int getCompteur() {
        return compteur;
    }
    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }
}
