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
<<<<<<< HEAD
import fr.unice.iutnice.sumble.R;
=======
>>>>>>> 6683c137c052e5ccd794a706df1650198282a56a

public class GameActivity extends Activity {

    private static boolean isFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.game_layout);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        String mode = getIntent().getStringExtra("mode");
        TypeDifficulte difficulte = getIntent().getExtras().getParcelable("difficulte");

        SurfaceViewBulle surface = new SurfaceViewBulle(this.getApplicationContext(), metrics, mode, difficulte);
        setContentView(surface);

<<<<<<< HEAD
        /*Score testScore = getIntent().getExtras().getParcelable("score"); // à supprimer

        Log.v("score parcel", ""+testScore);
        Log.v("type diff parcel", ""+testScore.getTypeDifficulte().toString());*/

        //TEST - SendScore base de donnees
        /*SendScore sendScore = new SendScore(this);
        sendScore.setParametre(""+testScore.getValeur(), ""+testScore.getTypeDifficulte().toString(), getId(), "le mode");
        Log.v("score exec", testScore.toString());
        sendScore.execute();*/

        if(estFinie()){
            Intent intent = new Intent(this, FinActivity.class);
            intent.putExtra("score", score(45f));
            startActivity(intent);
        }
    }

    public Score score(float value){
        TypeDifficulte typeDiff = getIntent().getExtras().getParcelable("diff");
        String mode = getIntent().getExtras().getString("mode");

        return new Score(value, typeDiff, mode);
=======
        //Score testScore = getIntent().getExtras().getParcelable("score"); // à supprimer

       // Log.v("score parcel", ""+testScore);
       // Log.v("type diff parcel", ""+testScore.getTypeDifficulte().toString());

        //TEST - SendScore base de donnees
        /*SendScore sendScore = new SendScore(this);
        sendScore.setParametre(""+testScore.getValeur(), ""+testScore.getTypeDifficulte().toString(), getId());
        Log.v("score exec", testScore.toString());
        sendScore.execute();*/
>>>>>>> 6683c137c052e5ccd794a706df1650198282a56a
    }

    public static boolean estFinie(){
        return isFinished;
    }

    public static void setIsFinished(boolean valeur){
        isFinished = valeur;
    }
}
