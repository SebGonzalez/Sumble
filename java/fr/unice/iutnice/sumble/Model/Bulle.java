package fr.unice.iutnice.sumble.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Random;

import fr.unice.iutnice.sumble.Controller.BulleFactory;
import fr.unice.iutnice.sumble.Controller.ConversionDpPixel;
import fr.unice.iutnice.sumble.R;

import static android.R.attr.max;
import static fr.unice.iutnice.sumble.Controller.BulleFactory.verifBulleEnDessous;
import static fr.unice.iutnice.sumble.R.drawable.bulle;

/**
 * Created by gonzo on 07/03/2017.
 */

public class Bulle implements Comparable {

    private int valeur;
    private int largeur;

    private int y;
    private int x;

    private Bitmap img;
    private Context c;
    DisplayMetrics metrics;
    int couleur;

    private boolean bloque = false;

    //constructeur utile pour le clone (évite de cloner des images qui sont des objets lourds en mémoire)
    public Bulle(DisplayMetrics m) {
        metrics = m;
    }

    /**
     * constructeur
     */
    public Bulle(Context c, DisplayMetrics m, int largeur) {
        metrics = m;
        this.c=c;
        this.largeur = largeur;
        x = metrics.widthPixels/2;
        y = ConversionDpPixel.dpToPx(60);

        //assignation de l'image
        final Bitmap bitmap =  BitmapFactory.decodeResource(c.getResources(), R.drawable.bulle);
        img = Bitmap.createScaledBitmap(bitmap,largeur,largeur, false);

    }

    //getter et setter
    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {this.valeur = valeur;}

    public int getY() {
        return y;
    }

    public boolean isBloque() {
        return bloque;
    }

    public void setBloque(boolean bloque) {
        this.bloque = bloque;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    public int getX() {
        return x;
    }

    public void setX(int valeur) {
        this.x = valeur;
    }
    public void setY(int valeur) {
        this.y = valeur;
    }

    public void setLargeur(int valeur) {
        this.largeur=valeur;
    }
    public int getLargeur() {
        return largeur;
    }
    public Bitmap getImg() {
        return img;
    }

    /**
     * Fonction qui gère le déplacement des bulles
     * @param valeur du déplacement
     */
    public void deplacementY(int valeur) {

        //si la bulle n'est pas tout en bas de l'écran et si elle n'est pas bloqué
        if( y+largeur <= metrics.heightPixels - ConversionDpPixel.dpToPx(25) && !bloque) {
            //si il n'y a pas de bulle en dessous
            if (verifBulleEnDessous(this) == -1 ) {
                //on incrémente y
                this.y += ConversionDpPixel.dpToPx(valeur);
            } else {
                //sinon on test sur une nouvelle bulle
                Bulle b = this.clone();
                //on applique l'écart entre les deux bulles
                b.setX(b.getX() + verifBulleEnDessous(this));
                //et si sur cette nouvelle bulle il n'y a pas de bulles en dessous ou qu'elle ne sort pas de l'écran on modifie la position de la bulle initiale
                if (verifBulleEnDessous(b) == -1 && b.getX() > 0 && b.getX() + b.getLargeur() < metrics.widthPixels) {
                    this.setX(x + verifBulleEnDessous(this));
                }
                /*else if(b.getX() < 0){
                    this.setX(0);
                    //this.setX(x + verifBulleEnDessous(this)/2);
                    bloque = true;
                }
                else if(b.getX()+largeur > metrics.widthPixels) {
                    this.setX(metrics.widthPixels-largeur);
                    bloque = true;
                }*/
                //sinon on bloque la bulle
                else {
                    bloque = true;
                }

            }
        }
        else
            bloque = true;
    }

    /**
     * Méthode standarde clone
     * @return la bulle cloné
     */
    public Bulle clone()  {
        Bulle b = new Bulle(metrics);
        b.setX(x);
        b.setY(y);
        b.setCouleur(couleur);
        b.setValeur(valeur);
        b.setLargeur(largeur);
        return b;
    }

    /**
     * Méthode de comparaison de bulle par rapport à l'attribut couleur
     * @return 1 si la couleur est supérieur, -1 dans le cas contraire et 0 si elles sont égales
     */
    @Override
    public int compareTo(Object o) {
        if(couleur > ((Bulle)o).getCouleur())
            return 1;
        else if(couleur < ((Bulle)o).getCouleur())
            return -1;
        else
            return 0;
    }

}
