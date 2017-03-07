package fr.unice.iutnice.sumble.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.util.Log;

import fr.unice.iutnice.sumble.R;

/**
 * Created by gonzo on 07/03/2017.
 */

public class Bulle {

    private int valeur;
    private int largeur;

    private int y;
    private int x;

    private BitmapDrawable img=null;
    private Context c;
    DisplayMetrics metrics;

    public Bulle(Context c, DisplayMetrics m) {
        metrics = m;
        Log.v("Bule : ", "" +  metrics.widthPixels);
        valeur = (int)Math.random()*20;
        largeur = metrics.widthPixels/5;
        x = metrics.widthPixels/2;
        y = metrics.heightPixels/2;
        this.c=c;
        img = setImage(c, R.drawable.bulle);
    }

    public BitmapDrawable setImage(final Context c, final int ressource)
    {
        Drawable dr = c.getResources().getDrawable(ressource);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        return new BitmapDrawable(c.getResources(), Bitmap.createScaledBitmap(bitmap, largeur,largeur, true));
    }

    public void draw(Canvas canvas)
    {
        if(img==null) {return;}

        canvas.drawBitmap(img.getBitmap(), x, y, null);
    }

    public int getValeur() {
        return valeur;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getY() {
        return y;
    }

    public void deplacementY(int valeur) {
        this.y += valeur;
    }

    public int getX() {
        return x;
    }

    public BitmapDrawable getImg() {
        return img;
    }
}
