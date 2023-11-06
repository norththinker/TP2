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
    private double ax = 100;
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
        context.drawImage(logoView, x, y);
        context.setStroke(Color.YELLOW); // Définissez la couleur du contour en jaune
        context.setLineWidth(2.0); // Définissez l'épaisseur du contour
        context.strokeRect(x, y,w, h);
    }

    public void update(double deltaTemps) {

        //vy += deltaTemps * ay;
        x += deltaTemps * vx;
        y += deltaTemps * vy;

        boolean left = Input.isKeyPressed(KeyCode.LEFT);
        boolean right = Input.isKeyPressed(KeyCode.RIGHT);
        boolean up = Input.isKeyPressed(KeyCode.UP);
        boolean down = Input.isKeyPressed(KeyCode.DOWN);

        // Mouvement horizontal
        if (left)
            vx = -150;
        else if (right)
            vx = 150;
        else
            vx = 0; // No arrow key pressed

        // Mouvement vertical
        if (up)
            vy = -150;
        else if (down)
            vy = 150;
        else
            vy = 0; // No arrow key pressed


        if (y + h > Main.HEIGHT) {
            y = Main.HEIGHT - h;
        }
        else if (y < 0) {
            y = 0;
            vy = 0;
        }
        if (x + w > Main.WIDTH){
            x = Main.WIDTH - w;
        }
        else if (x < 0) {
            x = 0;
        }
    }


}
