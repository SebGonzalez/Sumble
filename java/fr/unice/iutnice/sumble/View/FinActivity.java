package fr.unice.iutnice.sumble.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.unice.iutnice.sumble.Model.Connexion.SendScore;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.R;

public class FinActivity extends AppCompatActivity {

    private Button retourMenu;
    private TextView scoreFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        Score score = getIntent().getExtras().getParcelable("score");

        retourMenu = (Button)findViewById(R.id.retourMenu);
        retourMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        scoreFin = (TextView)findViewById(R.id.scoreFin);
        scoreFin.setText(score.getValeur()+"");

        SendScore sendScore = new SendScore(this);
        String id = getIntent().getStringExtra("id");
        Log.v("sendScore diff", ""+score.getTypeDifficulte().toString());
        sendScore.setParametre(""+score.getValeur(), ""+score.getTypeDifficulte().toString(), id, score.getMode());
        Log.v("score exec", score.toString());
        sendScore.execute();
    }

}
