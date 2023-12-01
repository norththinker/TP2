package org.example;

import javafx.scene.image.Image;
import java.util.Random;

public class Baril extends ObjetduJeu {
    private double hauteurDuJeu;

    private double hauteurDuBaril;
    private double tempsTotal;

    private boolean isTouche ;
    private double periode = 3;

    public void setDejaTouche(boolean dejaTouche) {
        this.dejaTouche = dejaTouche;
    }

    private boolean dejaTouche;



    private int choix ;


    public Baril() {

        imageObjet = new Image("baril.png");
        hauteurDuJeu = Main.HEIGHT;
        Random r = new Random();
        x = r.nextDouble(Partie.longeurNiveau/5, 4*Partie.longeurNiveau/5);
        w = 70;
        h = 83;
        hauteurDuBaril = h;
    }

    @Override


    public void update(double deltaTemps, Camera camera) {
        tempsTotal += deltaTemps;
        y = (hauteurDuJeu - hauteurDuBaril) / 2 * Math.sin(2 * Math.PI * tempsTotal / periode) + (hauteurDuJeu - hauteurDuBaril) / 2;

    }

    public boolean isDejaTouche() {
        return dejaTouche;
    }

    @Override
    public void testCollision(ObjetduJeu autreObjet) {
        if (this.enCollisionAvec(autreObjet)) {
            if (!isTouche) {


                Random r = new Random();

                choix = r.nextInt(0, 3);


                imageObjet = new Image("baril-ouvert.png");
                setTouche(true);
                dejaTouche = false;
            }
        }
    }

    @Override
    protected void gererHorsEcran(Camera camera) {

    }
    public void setTouche(boolean touche) {
        isTouche = touche;
    }

    public boolean isTouche() {
        return isTouche;
    }
    public int getChoix() {
        return choix;
    }

    public void setChoix(int choix) {
        this.choix = choix;
    }
}
