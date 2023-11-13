package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Poissons extends ObjetduJeu {
    private double x, y;
    private double w;
    private double h;
    private double ax;
    private double ay;
    private boolean mort = false;



    public boolean isMort() {
        return mort;
    }

    public void setMort(boolean mort) {
        this.mort = mort;
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

    public double getAx() {
        return ax;
    }

    public void setAx(double ax) {
        this.ax = ax;
    }

    public double getAy() {
        return ay;
    }

    public void setAy(double ay) {
        this.ay = ay;
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

    public Image getLogoView() {
        return logoView;
    }

    public void setLogoView(Image logoView) {
        this.logoView = logoView;
    }

    public boolean isEstTouche() {
        return estTouche;
    }

    public void setEstTouche(boolean estTouche) {
        this.estTouche = estTouche;
    }

    private boolean estTouche =false;
    private double vx = -300 ;
    private double vy = 300 ;
    private Image logoView = new Image("poisson1.png");

    public Poissons(double x, double y, double w, double h,double vy) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.vy = vy;
    }
    public void draw(GraphicsContext context) {
        context.setFill(Color.rgb(200, 200, 200, 0));
        context.fillRect(x, y, w, h);
        context.drawImage(logoView, x, y);
        context.setStroke(Color.YELLOW);
        context.setLineWidth(2.0);
        context.strokeRect(x,y,w,h);
        if ( estTouche) {
            context.clearRect(x,y,w,h);

        }

    }

    public void update(double deltaTemps) {


        x += deltaTemps * vx;
        y += deltaTemps * vy;




        if (y + h > Main.HEIGHT) {

            // Déplace les flocons vers le haut
            y = Main.HEIGHT - h;


        }


    }
    public boolean enCollisionAvec(Personnage poisson) {
        double dx = this.x - poisson.getX();
        double dy = this.y - poisson.getY();
        double dCarre = dx * dx + dy * dy;
        return dCarre < (this.w/2 + poisson.getW()/2) * (this.w/2 + poisson.getW()/2) + (this.h/2 + poisson.getH()/2) * (this.h/2 + poisson.getW()/2);


    }
    public void testCollision(Personnage poisson) {
        if (this.enCollisionAvec(poisson)) {
            if (!poisson.isEstTouche()) {
            System.out.println(1);
            poisson.setEstTouche(true); }
         poisson.setCompteur(poisson.getCompteur()+1);
        }


}}
