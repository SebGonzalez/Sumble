package fr.unice.iutnice.sumble.Controller;

import android.content.res.Resources;

/**
 * Created by gonzo on 07/03/2017.
 */

/**
 * Classe de conversion d'unité (px et dp)
 */
public class ConversionDpPixel {

    /**
     * Méthode permettant de convertir des dp en pixel
     * @param dp
     * @return
     */
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Méthode permettant de convertir des pixels en dp
     * @param px
     * @return
     */
    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
