package fr.unice.iutnice.sumble.Controller;

import android.content.res.Resources;

/**
 * Created by gonzo on 07/03/2017.
 */

public class ConversionDpPixel {

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
