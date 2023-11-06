package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Projectile extends ObjetduJeu{
    private double x, y;
    private double w;
    private double h;
    private double vx = 300 ;

    private String image ;
    private Image logoView = new Image("etoile.png");
    public Projectile(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

    }

    public void draw(GraphicsContext context) {
        context.setFill(Color.rgb(200, 200, 200, 0));
        context.fillRect(x, y, w, h);
        context.drawImage(logoView, x, y);


    }

    public void update(double deltaTemps) {


        x += deltaTemps * vx;





        if (y + h > Main.HEIGHT) {

            // Déplace les flocons vers le haut
            y = Main.HEIGHT - h;


        }

    }


    public boolean enCollisionAvec(ObjetduJeu objetduJeu) {
        double dx = this.x - objetduJeu.getX();
        double dy = this.y - objetduJeu.getY();
        double dCarre = dx * dx + dy * dy;
        return dCarre < (this.w/2 + objetduJeu.getW()/2) * (this.w/2 + objetduJeu.getW()/2) + (this.h/2 + objetduJeu.getH()/2) * (this.h/2 + objetduJeu.getW()/2);



    }
    public void testCollision(ObjetduJeu autre) {
        if (this.enCollisionAvec(autre)) {

         autre.setEstTouche(true);
        }
    }




}