package fr.unice.iutnice.sumble.View;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.View.SurfaceView.SurfaceViewDebutant;
import fr.unice.iutnice.sumble.View.SurfaceView.SurfaceViewIExpert;
import fr.unice.iutnice.sumble.View.SurfaceView.SurfaceViewIntermediaire;


public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.game_layout);

        String mode = getIntent().getStringExtra("mode");
        TypeDifficulte difficulte = getIntent().getParcelableExtra("difficulte");
        Log.v("difficulte GA", ""+difficulte);

        String id = getIntent().getStringExtra("id");

        if(difficulte.equals(TypeDifficulte.Facile)) {
            SurfaceViewDebutant surface = new SurfaceViewDebutant(this, mode, difficulte, id);
            setContentView(surface);
        }
        else if(difficulte.equals(TypeDifficulte.Moyen)) {
            SurfaceViewIntermediaire surface = new SurfaceViewIntermediaire(this, mode, difficulte, id);
            setContentView(surface);
        }
        else {
            SurfaceViewIExpert surface = new SurfaceViewIExpert(this, mode, difficulte, id);
            setContentView(surface);
        }


        /*Score testScore = getIntent().getExtras().getParcelable("score"); // à supprimer

        Log.v("score parcel", ""+testScore);
        Log.v("type diff parcel", ""+testScore.getTypeDifficulte().toString());*/

        //TEST - SendScore base de donnees
        /*SendScore sendScore = new SendScore(this);
        sendScore.setParametre(""+testScore.getValeur(), ""+testScore.getTypeDifficulte().toString(), getId(), "le mode");
        Log.v("score exec", testScore.toString());
        sendScore.execute();*/

    }

}
