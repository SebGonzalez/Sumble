package fr.unice.iutnice.sumble.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import fr.unice.iutnice.sumble.Model.Connexion.SendScore;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.R;


public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.game_layout);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        String mode = getIntent().getStringExtra("mode");
        TypeDifficulte difficulte = getIntent().getParcelableExtra("difficulte");
        Log.v("difficulte GA", ""+difficulte);

        String id = getIntent().getStringExtra("id");

        SurfaceViewBulle surface = new SurfaceViewBulle(this, metrics, mode, difficulte, id);
        setContentView(surface);


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
