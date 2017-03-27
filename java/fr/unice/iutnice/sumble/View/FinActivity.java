package fr.unice.iutnice.sumble.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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

    public final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;

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
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                envoyerScore(getUniqueID());
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
                int permissionCheck = ActivityCompat.checkSelfPermission(FinActivity.this,Manifest.permission.READ_SMS);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(FinActivity.this, Manifest.permission.READ_SMS)) {
                        showExplanation("Permission", "Vous devez accepter pour envoyer votre score par message", Manifest.permission.READ_SMS, MY_PERMISSIONS_REQUEST_READ_SMS);
                    } else {
                        requestPermission(Manifest.permission.READ_SMS, MY_PERMISSIONS_REQUEST_READ_SMS);
                    }
                } else {
                    envoyerScoreSMS(entrerNum.getText().toString());
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(isOnline() && cptSend==0){
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                envoyerScore(getUniqueID());
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
            com.klinker.android.send_message.Settings settings = new com.klinker.android.send_message.Settings();
            settings.setUseSystemSending(true);
            Transaction transaction = new Transaction(this, settings);
            Message message = new Message("Je viens d'effectuer " + score.getValeur() + " à Sumble !", numero);
            transaction.sendNewMessage(message, 0);
            Toast.makeText(this, "SMS envoyé au : " + numero, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Veuillez entrer un numéro valide", Toast.LENGTH_SHORT).show();
        }
    }

    public void envoyerScore(String id){
        SendScore sendScore = new SendScore(this);
        sendScore.setParametre(""+score.getValeur(), ""+score.getTypeDifficulte().toString(), id, score.getMode());
        sendScore.execute();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    envoyerScoreSMS(entrerNum.getText().toString());

                } else {
                    Toast.makeText(FinActivity.this, "Vous devez accepter pour envoyer votre score par message !", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, permissionRequestCode);
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

}
