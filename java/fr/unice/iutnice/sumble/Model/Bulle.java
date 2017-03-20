package fr.unice.iutnice.sumble.Model;

import android.content.Context;
import android.graphics.Bitmap;
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

/**
 * Created by gonzo on 07/03/2017.
 */

public class Bulle implements Comparable {

    private int valeur;
    private int largeur;

    private int y;
    private int x;

    private BitmapDrawable img=null;
    private Context c;
    DisplayMetrics metrics;
    int couleur;

    private boolean bloque = false;

    public Bulle(Context c, DisplayMetrics m) {
        metrics = m;
        this.c=c;
        Random r = new Random();
        // int valeur = r.nextInt((max - min) + 1) + min;
        largeur = ConversionDpPixel.dpToPx(r.nextInt((80 - 50) + 1) + 50);
        x = metrics.widthPixels/2;
        y = 50;
        img = setImage(c, R.drawable.bulle);
    }

    public BitmapDrawable setImage(final Context c, final int ressource)
    {
        Drawable dr = c.getResources().getDrawable(ressource);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        return new BitmapDrawable(c.getResources(), Bitmap.createScaledBitmap(bitmap, largeur,largeur, true));
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {this.valeur = valeur;}

    public int getLargeur() {
        return largeur;
    }

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

    public void deplacementY(int valeur) throws Exception {

       // Log.v("verif", "" + verifBulleEnDessous(getPositionListeBulle()));
        if( y+largeur <= metrics.heightPixels - ConversionDpPixel.dpToPx(25) && !bloque) {
            if (verifBulleEnDessous(this) == -1 ) {
                this.y += ConversionDpPixel.dpToPx(valeur);
                //resetBloque();
            } else {
                Bulle b = this.clone();
                b.setX(b.getX() + verifBulleEnDessous(this));
                if (verifBulleEnDessous(b) == -1 && b.getX() > 0 && b.getX() + b.getLargeur() < metrics.widthPixels) {
                    this.setX(x + verifBulleEnDessous(this));
                }
                else if(y>300)
                    bloque = true;
            }
        }
        else
            bloque = true;
    }

    public Bulle clone()  {
        Bulle b = new Bulle(c, metrics);
        b.setX(x);
        b.setY(y);
        b.setLargeur(largeur);
        b.setCouleur(couleur);
        b.setValeur(valeur);
        b.setImage(c, R.drawable.bulle);

        return b;
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
        this.valeur=valeur;
    }

    public BitmapDrawable getImg() {
        return img;
    }

    @Override
    public int compareTo(Object o) {
        if(couleur > ((Bulle)o).getCouleur())
            return 1;
        else if(couleur < ((Bulle)o).getCouleur())
            return -1;
        else
            return 0;
    }

    public String toString() {
        return ""+couleur;
    }
}
