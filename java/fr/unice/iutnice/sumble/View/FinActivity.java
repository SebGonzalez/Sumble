package fr.unice.iutnice.sumble.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Transaction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.unice.iutnice.sumble.Model.Connexion.SendScore;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.R;

public class FinActivity extends AppCompatActivity {

    private Button retourMenu;
    private TextView scoreFin;
    private Score score;
    private boolean isSend;
    private int cptSend=0;
    private Button envoyer;
    private EditText entrerNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        score = getIntent().getExtras().getParcelable("score");

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

        if(isOnline()){
            envoyerScore();
            cptSend ++;
            isSend = true;
        }else{
            Toast.makeText(this, "Vous devez être connecté à internet pour envoyer votre score", Toast.LENGTH_SHORT).show();
        }
        entrerNum = (EditText)findViewById(R.id.entrerNum);

        envoyer = (Button)findViewById(R.id.envoyer);
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyerScoreSMS(entrerNum.getText().toString());
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(isOnline() && cptSend==0){
            envoyerScore();
            Toast.makeText(this, "Le score a été envoyé", Toast.LENGTH_SHORT).show();
            cptSend ++;
            isSend = true;
        }else{
            if(isSend){
                Toast.makeText(this, "Votre score a déjà été envoyé", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Score non envoyé", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void envoyerScoreSMS(String numero){
        Pattern pattern = Pattern.compile("(\\+[0-9]{3}( [0-9][0-9])+)|([0-9]+)");
        Matcher matcher = pattern.matcher(numero);
        boolean numOk = matcher.matches();
        if(numOk) {
            Toast.makeText(this, "sms en cours d'envoi !", Toast.LENGTH_SHORT).show();
            com.klinker.android.send_message.Settings settings = new com.klinker.android.send_message.Settings();
            settings.setUseSystemSending(true);
            Transaction transaction = new Transaction(this, settings);
            Message message = new Message("test", numero);
            transaction.sendNewMessage(message, 0);
            Toast.makeText(this, "SMS envoyé au : " + numero, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Veuillez entrer un numéro valide", Toast.LENGTH_SHORT).show();
        }
    }

    public void envoyerScore(){
        SendScore sendScore = new SendScore(this);
        String id = getIntent().getStringExtra("id");
        Log.v("sendScore diff", ""+score.getTypeDifficulte().toString());
        sendScore.setParametre(""+score.getValeur(), ""+score.getTypeDifficulte().toString(), id, score.getMode());
        Log.v("score exec", score.toString());
        sendScore.execute();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
