package fr.unice.iutnice.sumble.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.ContactsContract;
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

/**
 * Gère la fin du jeu
 */
public class FinActivity extends AppCompatActivity {

    private Button retourMenu;
    private TextView scoreFin;
    private Score score;
    private boolean isSend;
    private int cptSend=0;
    private Button envoyer;


    public final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    private final int PICK_CONTACT_REQUEST = 1;

    /**
     * Creation de la vue
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        score = getIntent().getExtras().getParcelable("score"); //on recup le score qui est Parcelable

        retourMenu = (Button)findViewById(R.id.retourMenu);
        //au click, retourne au menu
        retourMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        scoreFin = (TextView)findViewById(R.id.scoreFin);
        scoreFin.setText(score.getValeur()+"");

        //verification de la connexion
        if(isOnline()){
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
                envoyerScore(getUniqueID());
            cptSend ++;
            isSend = true;
        }else{
            Toast.makeText(this, "Vous devez être connecté à internet pour envoyer votre score", Toast.LENGTH_SHORT).show();
        }


        envoyer = (Button)findViewById(R.id.envoyer);
        //on peut envoyer son score au numéro entré dans l'EditText
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ActivityCompat.checkSelfPermission(FinActivity.this,Manifest.permission.READ_SMS);

                //on vérifie les permissions encore et toujours...
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(FinActivity.this, Manifest.permission.READ_SMS)) {
                        showExplanation("Permission", "Vous devez accepter pour envoyer votre score par message", Manifest.permission.READ_SMS, MY_PERMISSIONS_REQUEST_READ_SMS);
                    } else {
                        requestPermission(Manifest.permission.READ_SMS, MY_PERMISSIONS_REQUEST_READ_SMS);
                    }
                } else {
                    //si elles sont acceptées, on peut envoyer le score par message
                    pickContact();
                }
            }
        });
    }

    /**
     * Au retour sur l'application
     */
    @Override
    public void onResume(){
        //on renvoie le score à la base de donnée s'il n'a pas pu le faire avant
        //s'il a déjà fait, on ne renvoie rien évidemment
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

    /**
     * Fonction pour envoyer un sms
     * @param numero : numero auquel envoyer le sms
     */
    public void envoyerScoreSMS(String numero){
        //pour envoyer un SMS, nous avons utilisé une libraire appelée klinker send_message

        //expression régulière pour vérifier qu'il s'agit bien d'un numéro de téléphone

        com.klinker.android.send_message.Settings settings = new com.klinker.android.send_message.Settings();
        settings.setUseSystemSending(true);
        Transaction transaction = new Transaction(this, settings);
        Message message = new Message("Je viens d'effectuer " + score.getValeur() + " à Sumble !", numero);
        transaction.sendNewMessage(message, 0);
        Toast.makeText(this, "SMS envoyé au : " + numero, Toast.LENGTH_SHORT).show();

    }

    /**
     * Envoyer score au serveur
     * @param id
     */
    public void envoyerScore(String id){
        SendScore sendScore = new SendScore(this);
        sendScore.setParametre(""+score.getValeur(), ""+score.getTypeDifficulte().toString(), id, score.getMode());
        sendScore.execute();
    }

    /**
     * Est en ligne ?
     * @return
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Après la demande de permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickContact();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);

                envoyerScoreSMS(number);
            }
        }
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

}
