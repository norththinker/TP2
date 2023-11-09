package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Personnage extends ObjetduJeu {

    private double x, y;
    private double w;
    private double h;

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

    private boolean estTouche = false;
    private int compteur;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setW(double w) {
        this.w = w;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public void setAy(double ay) {
        this.ay = ay;
    }

    public void setLogoView(Image logoView) {
        this.logoView = logoView;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getAx() {
        return ax;
    }

    public double getAy() {
        return ay;
    }

    public Image getLogoView() {
        return logoView;
    }

    private double vx;
    private double vy;
    private double ax;
    private double ay;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image  = "charlotte.png";
    ;
    private Image logoView = new Image(image);

    public Personnage(double x, double y, double w, double h) {


        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

    }

    public void draw(GraphicsContext context) {
        context.setFill(Color.rgb(200, 200, 200, 0));
        context.fillRect(x, y, w, h);
        context.drawImage(logoView, x, y);
        context.setStroke(Color.YELLOW);
        context.setLineWidth(2.0);
        context.strokeRect(x,y,w,h);


    }

    public void update(double deltaTemps) {
        double vitesseMax = 300;
        vx = Math.min(vitesseMax, Math.max(-vitesseMax, vx));
        vx += deltaTemps * ax;
        vy = Math.min(vitesseMax, Math.max(-vitesseMax, vy));

        vy += deltaTemps * ay;
        x += deltaTemps * vx;
        y += deltaTemps * vy;


        boolean left = Input.isKeyPressed(KeyCode.LEFT);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);

        boolean up = Input.isKeyPressed(KeyCode.UP);
        boolean down = Input.isKeyPressed(KeyCode.DOWN);

        // Mouvement horizontal
        if (left) {
            setImage("charlotte-avant.png");
            logoView = new Image(image);
            ax = -1000; // Accélération vers la gauche
        } else if (right) {
            setImage("charlotte-avant.png");
            logoView = new Image(image);
            ax = 1000; // Accélération vers la droite
        } else {
            setImage("charlotte.png");
            logoView = new Image(image);

            ax = -vx * 10;

        }

        if (up) {
            setImage("charlotte-avant.png");
            logoView = new Image(image);
            ay = -1000; // Accélération vers le haut
        } else if (down) {
            setImage("charlotte-avant.png");
            logoView = new Image(image);
            ay = 1000; // Accélération vers le bas
        } else {
            setImage("charlotte.png");
            logoView = new Image(image);
            ay = -vy * 10; // Aucune accélération verticale
        }


        if (y + h > Main.HEIGHT) {

            // Déplace les flocons vers le haut
            y = Main.HEIGHT - h;


        }
        if (x + w > Main.WIDTH) {

            // Déplace les flocons vers le haut
            x = Main.WIDTH - w;


        }
        if (x < 0) {

            x = 0;

        }
        if (y < 0) {

            y = 0;

        }
    }

    public boolean enCollisionAvec(Poissons poisson) {
        double dx = this.x - poisson.getX();
        double dy = this.y - poisson.getY();
        double dCarre = dx * dx + dy * dy;
        return dCarre < (this.w/2 + poisson.getW()/2) * (this.w/2 + poisson.getW()/2) + (this.h/2 + poisson.getH()/2) * (this.h/2 + poisson.getW()/2);


    }
    public void testCollision(Poissons poisson) {
        if (this.enCollisionAvec(poisson)) {
            System.out.println(1);
           estTouche = true;
           compteur++;
        }
    }
}