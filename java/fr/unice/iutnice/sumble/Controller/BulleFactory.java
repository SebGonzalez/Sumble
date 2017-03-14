package fr.unice.iutnice.sumble.Controller;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.ArrayList;

import fr.unice.iutnice.sumble.Model.Bulle;

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
}
