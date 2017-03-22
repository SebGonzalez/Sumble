package fr.unice.iutnice.sumble.Controller;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import fr.unice.iutnice.sumble.Model.Bulle;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by gonzo on 07/03/2017.
 */

public class BulleFactory {

    public static ArrayList<Bulle> listeBulle;

    public BulleFactory(Context c, DisplayMetrics metrics) {
        listeBulle = new ArrayList<>();
    }

    public ArrayList<Bulle> getListeBulle() {
        return listeBulle;
    }

    public static int getPositionListeBulle(Bulle b) throws Exception {
        for(int i=0; i<listeBulle.size(); i++) {
            if(listeBulle.get(i).equals(b))
                return i;
        }
        throw new Exception("Bulle non enregistré");
    }

    public static int verifBulleEnDessous(Bulle b) {
        for(int i=0; i<listeBulle.size(); i++) {

            if(b.getY()+b.getLargeur() > listeBulle.get(i).getY() && b.getY()+b.getLargeur() < listeBulle.get(i).getY()+listeBulle.get(i).getLargeur() && !b.equals(listeBulle.get(i))) {
                if(b.getX()>=listeBulle.get(i).getX() && b.getX() < listeBulle.get(i).getX() + listeBulle.get(i).getLargeur()) {

                    return ( (listeBulle.get(i).getX()+listeBulle.get(i).getLargeur()) - b.getX());
                }
                else if(b.getX()<=listeBulle.get(i).getX() && b.getX()+b.getLargeur()>listeBulle.get(i).getX()) {

                    return -(((b.getX()+b.getLargeur())-listeBulle.get(i).getX()));
                }
            }
        }
        return -1;
    }

    public void resetBloque() {
        for(int i=0; i<listeBulle.size(); i++) {
            if(listeBulle.get(i).isBloque()) {
                listeBulle.get(i).setBloque(false);
            }
        }
    }

    public boolean verifPossibiliteGen(int x, DisplayMetrics metrics) {
        int compteur = 0;
        if(listeBulle.size() != 0) {
            for (int i = 0; i < listeBulle.size(); i++) {
                if (listeBulle.get(i).getY() < ConversionDpPixel.dpToPx(80)) {
                    for (int y = 0; y < listeBulle.size(); y++) {
                        if (i != y && listeBulle.get(y).getY() < ConversionDpPixel.dpToPx(80)) {
                            if (listeBulle.get(i).getX() - x < listeBulle.get(y).getX() + listeBulle.get(y).getLargeur() && listeBulle.get(i).getX() + listeBulle.get(i).getLargeur() + x > listeBulle.get(y).getX())
                                compteur++;
                            else if (listeBulle.get(i).getX() - x <= 0)
                                compteur++;
                            else if (listeBulle.get(i).getX() + listeBulle.get(i).getLargeur() + x >= metrics.widthPixels)
                                compteur++;
                        }
                    }
                }
            }
            if (compteur >= compteurBulleEnHaut() && compteurBulleEnHaut()>4)
                return false;
        }
        return true;

    }

    public int compteurBulleEnHaut() {
        int compteur = 0;
        int y=0;
        for(int i=0; i<listeBulle.size(); i++) {
            if(listeBulle.get(i).getY() < ConversionDpPixel.dpToPx(80)) {
                while(y<listeBulle.size()) {
                    if(i!= y && listeBulle.get(i).getY()+listeBulle.get(i).getLargeur() > listeBulle.get(y).getY()) {
                        compteur++;
                        y = listeBulle.size();
                    }
                    y++;
                }
            }
            y=0;
        }
        return compteur;
    }

    public boolean verifPossibiliteGen(DisplayMetrics metrics) {
        if(listeBulle.size() != 0) {
            Log.v("x", ""+metrics.widthPixels);
            Random r = new Random();
            int depart = r.nextInt(ConversionDpPixel.dpToPx(40))+ConversionDpPixel.dpToPx(20);
            for (int i = ConversionDpPixel.dpToPx(50); i < metrics.widthPixels-ConversionDpPixel.dpToPx(50); i += ConversionDpPixel.dpToPx(50)) {
                if (verifLigne(i))
                    return true;
            }
            return false;
        }
        return true;
    }

    public boolean verifLigne(int x) {
        for(int y=0; y<listeBulle.size(); y++) {

            if (ConversionDpPixel.dpToPx(60) + ConversionDpPixel.dpToPx(20) > listeBulle.get(y).getY() && x> listeBulle.get(y).getX() && x < listeBulle.get(y).getX()+listeBulle.get(y).getLargeur())
                return false;
        }
        Log.v("x", ""+x);
        return true;
    }
}
