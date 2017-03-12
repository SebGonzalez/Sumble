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

import fr.unice.iutnice.sumble.Controller.BulleFactory;
import fr.unice.iutnice.sumble.Controller.ConversionDpPixel;
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
        valeur = (int)(Math.random()*20);
        largeur = metrics.widthPixels/5;
        x = metrics.widthPixels/2;
        y = 50;
        this.c=c;
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

    public int getLargeur() {
        return largeur;
    }

    public int getY() {
        return y;
    }

    public void deplacementY(int valeur) throws Exception {

       // Log.v("verif", "" + verifBulleEnDessous(getPositionListeBulle()));
        if( y+largeur <= metrics.heightPixels - ConversionDpPixel.dpToPx(25)) {
            if (verifBulleEnDessous(getPositionListeBulle(), this) == -1) {
                this.y += valeur;
            } else {
                Bulle b = this.clone();
                b.setX(b.getX() + verifBulleEnDessous(getPositionListeBulle(), this));
                if (b.verifBulleEnDessous(getPositionListeBulle(), this) == -1 && b.getX() > 0 && b.getX() + b.getLargeur() < metrics.widthPixels) {
                    this.setX(x + verifBulleEnDessous(getPositionListeBulle(), this));
                    //Log.v("" + x, "" + BulleFactory.listeBulle.get(getPositionListeBulle()).getX());

                  //  this.y+=valeur;
                }
            }
        }
    }

    public Bulle clone()  {
        Bulle b = new Bulle(c, metrics);
        b.setX(x);
        b.setY(y);
        b.setLargeur(largeur);
        b.setImage(c, R.drawable.bulle);

        return b;
    }

    public int verifBulleEnDessous(int pos, Bulle b) {
        for(int i=0; i<pos; i++) {
           /// Log.v(""+ (y+largeur), "" + BulleFactory.listeBulle.get(i).getY());
           // Log.v(""+ (y+largeur), ""+(BulleFactory.listeBulle.get(i).getY()+BulleFactory.listeBulle.get(i).getLargeur()));
          //  Log.v(""+BulleFactory.listeBulle.get(i).getLargeur(),"oui");
            if(y+largeur > BulleFactory.listeBulle.get(i).getY() && y+largeur < BulleFactory.listeBulle.get(i).getY()+BulleFactory.listeBulle.get(i).getLargeur() && !b.equals(BulleFactory.listeBulle.get(i))) {
                if(x>=BulleFactory.listeBulle.get(i).getX() && x < BulleFactory.listeBulle.get(i).getX() + BulleFactory.listeBulle.get(i).getLargeur()) {
                     //Log.v(""+ x, "" + BulleFactory.listeBulle.get(i).getX() );
                   //  Log.v(""+ x, ""+(BulleFactory.listeBulle.get(i).getX() + BulleFactory.listeBulle.get(i).getLargeur()));
                      Log.v("","");
                    return ( (BulleFactory.listeBulle.get(i).getX()+BulleFactory.listeBulle.get(i).getLargeur()) - x);
                }
                else if(x<=BulleFactory.listeBulle.get(i).getX() && x+largeur>BulleFactory.listeBulle.get(i).getX()) {
                   // Log.v(""+ x, "" + BulleFactory.listeBulle.get(i).getX());
                   // Log.v(""+ (x+largeur), ""+(BulleFactory.listeBulle.get(i).getX()));
                    Log.v("","");
                    return -(((x+largeur)-BulleFactory.listeBulle.get(i).getX()));
                }
            }
        }
        return -1;
    }

    private int getPositionListeBulle() throws Exception {
        for(int i=0; i<BulleFactory.listeBulle.size(); i++) {
            if(BulleFactory.listeBulle.get(i).equals(this))
                return i;
        }
        throw new Exception("Bulle non enregistré");
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
}
