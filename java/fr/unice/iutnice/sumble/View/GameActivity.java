package fr.unice.iutnice.sumble.View;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.game_layout);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        SurfaceViewBulle surface = new SurfaceViewBulle(this.getApplicationContext(), metrics);
        setContentView(surface);
    }
}