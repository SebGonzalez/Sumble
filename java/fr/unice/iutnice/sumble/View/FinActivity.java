package fr.unice.iutnice.sumble.View;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import fr.unice.iutnice.sumble.Model.Connexion.SendScore;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.R;

public class FinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        Score score = getIntent().getExtras().getParcelable("score");

        SendScore sendScore = new SendScore(this);
        Log.v("sendScore diff", ""+score.getTypeDifficulte().toString());
        sendScore.setParametre(""+score.getValeur(), ""+score.getTypeDifficulte().toString(), getUniqueID(), score.getMode());
        Log.v("score exec", score.toString());
        sendScore.execute();
    }

    public String getUniqueID(){
        String myAndroidDeviceId = "";
        TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null){
            myAndroidDeviceId = mTelephony.getDeviceId();
        }else{
            myAndroidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return myAndroidDeviceId;
    }

    public String getId(){

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Log.v("oui", ""+ telephonyManager.getDeviceId());
        return telephonyManager.getDeviceId();


    }
}
