package fr.unice.iutnice.sumble.View;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.unice.iutnice.sumble.Controller.SwipePageAdapter;
import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.Fragments.GameMenu;
import fr.unice.iutnice.sumble.View.Fragments.ScoreMenu;
import fr.unice.iutnice.sumble.View.Fragments.SettingsMenu;

/**
 * Activité de départ
 */
public class MainActivity extends FragmentActivity{

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    List<Fragment> fragments;

    //utilisé pour demandé la permission
    public final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    /**
     * Création de l'activité de démarrage
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //on assigne le layout
        setContentView(R.layout.activity_main);

        //on récupère la liste des fragment de la page
        fragments = new ArrayList<Fragment>();

        //on déclare la permission à vérifier (si l'utilisateur l'a autorisé
        int permissionCheck = ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);

        //si elle n'est pas encore autorisé
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_PHONE_STATE, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            fragments = getFragments();
            pagerAdapter = new SwipePageAdapter(super.getSupportFragmentManager(), fragments);
            viewPager = (ViewPager) super.findViewById(R.id.viewpager);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(1);
        }

        if(!isOnline()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connexion internet");
            builder.setMessage("Pour bénéficier d'une meilleure exéperience de jeu, activez une connexion à internet pour recevoir et envoyer vos scores");
            builder.setPositiveButton("Ouvrir les paramètres", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            });
            builder.show();
        }else{
            Log.v("oui" , ""+isOnline());
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.v("onResume", "dedans");

    }

    /**
     * La liste des fragments s'il accepte les permissions
     * @return
     */
    public List<Fragment> getFragments() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(SettingsMenu.newInstance());
        list.add(new GameMenu());
        list.add(ScoreMenu.newInstance(getUniqueID()));
        Log.v("id", getUniqueID());
        return list;
    }

    /**
     * La liste des fragments s'il n'accepte pas les permissions
     * @return
     */
    public List<Fragment> getFragmentsSansId() {
        List<Fragment> list = new ArrayList<Fragment>();
        list.add(SettingsMenu.newInstance());
        list.add(new GameMenu());
        list.add(new ScoreMenu());
        return list;
    }

    /**
     * Récupérer l'imei de l'utilisateur
     * @return
     */
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

    /**
     * Est-il connecté ?
     * @return : true si oui, false sinon
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Après la demande des permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fragments = getFragments();
                    pagerAdapter = new SwipePageAdapter(super.getSupportFragmentManager(), fragments);
                    viewPager = (ViewPager) super.findViewById(R.id.viewpager);
                    viewPager.setAdapter(pagerAdapter);
                    viewPager.setCurrentItem(1);
                } else {
                    Toast.makeText(MainActivity.this, "La consultation et l'envoie des score ne sera pas possible si vous refusez !", Toast.LENGTH_SHORT).show();
                    fragments = getFragmentsSansId();
                    pagerAdapter = new SwipePageAdapter(super.getSupportFragmentManager(), fragments);
                    viewPager = (ViewPager) super.findViewById(R.id.viewpager);
                    viewPager.setAdapter(pagerAdapter);
                    viewPager.setCurrentItem(1);
                }
        }
    }

    /**
     * Demande de permissions
     * @param permissionName
     * @param permissionRequestCode
     */
    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, permissionRequestCode);
    }

}
