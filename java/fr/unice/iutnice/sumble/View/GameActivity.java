package fr.unice.iutnice.sumble.View;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import fr.unice.iutnice.sumble.Model.Connexion.SendScore;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.Model.TypeDifficulte;

public class GameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.game_layout);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        String mode = getIntent().getStringExtra("mode");
        TypeDifficulte difficulte = getIntent().getExtras().getParcelable("difficulte");

        SurfaceViewBulle surface = new SurfaceViewBulle(this.getApplicationContext(), metrics, mode, difficulte);
        setContentView(surface);

        //Score testScore = getIntent().getExtras().getParcelable("score"); // à supprimer

       // Log.v("score parcel", ""+testScore);
       // Log.v("type diff parcel", ""+testScore.getTypeDifficulte().toString());

        //TEST - SendScore base de donnees
        /*SendScore sendScore = new SendScore(this);
        sendScore.setParametre(""+testScore.getValeur(), ""+testScore.getTypeDifficulte().toString(), getId());
        Log.v("score exec", testScore.toString());
        sendScore.execute();*/
    }

    public String getId(){

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}
