package org.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Personnage {

    private double x,y;
    private double w;
    private double h ;
    private double vx = 100;
    private double vy = 100;
    private double ax =100;
    Image logoView = new Image("charlotte.png");

    public Personnage(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

    }

    public void draw(GraphicsContext context) {
        context.setFill(Color.rgb(200,200,200, 0));
        context.fillRect(x, y, w, h);
        context.drawImage(logoView, 0, 0);
        context.setStroke(Color.YELLOW); // Définissez la couleur du contour en jaune
        context.setLineWidth(2.0); // Définissez l'épaisseur du contour
        context.strokeRect(0, 0,w, h);

    }

    public void update(double deltaTemps) {

        x += deltaTemps* vx;
        y += deltaTemps* vy;

        boolean left = Input.isKeyPressed(KeyCode.LEFT);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);


        // Mouvement horizontal
        if (left)
            ax = -300;
        else if (right)
            ax = 300;
        else
            vx = 0;




        boolean jump =
                Input.isKeyPressed(KeyCode.SPACE)
                        || Input.isKeyPressed(KeyCode.UP);
        // Sauter = donner une vitesse vers le haut


        if ( y+h> Main.HEIGHT){

            // Déplace les flocons vers le haut
            y =  Main.HEIGHT - h ;




        }
        if ( x+w> Main.WIDTH ){

            // Déplace les flocons vers le haut
            x=  Main.WIDTH - w ;


        }
        if ( x<0) {

            x=  0 ;

        }}


}
