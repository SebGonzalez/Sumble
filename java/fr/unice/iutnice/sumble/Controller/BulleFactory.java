package fr.unice.iutnice.sumble.Controller;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.ArrayList;

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
        listeBulle.add(new Bulle(c, metrics));
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

    public static int verifBulleEnDessous(int pos, Bulle b) {
        for(int i=0; i<pos; i++) {

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
}
