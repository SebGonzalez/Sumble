package fr.unice.iutnice.sumble.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import fr.unice.iutnice.sumble.Controller.SwipePageAdapter;
import fr.unice.iutnice.sumble.Model.Connexion.GetScore;
import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.Fragments.GameMenu;
import fr.unice.iutnice.sumble.View.Fragments.ScoreMenu;
import fr.unice.iutnice.sumble.View.Fragments.SettingsMenu;

import static android.R.id.list;

public class MainActivity extends FragmentActivity{

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    List<Fragment> fragments;

    public final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments = new ArrayList<Fragment>();

        int permissionCheck = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.READ_PHONE_STATE, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                requestPermission(Manifest.permission.READ_PHONE_STATE, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } else {
            Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }*/

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
        }

        List<Fragment> fragments = getFragments();
        if(permissionCheck == PackageManager.PERMISSION_GRANTED)
            fragments.add(ScoreMenu.newInstance(getUniqueID()));
        pagerAdapter = new SwipePageAdapter(super.getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) super.findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

    }

    public List<Fragment> getFragments() {
        List<Fragment> list = new ArrayList<Fragment>();

        list.add(SettingsMenu.newInstance());
        list.add(GameMenu.newInstance());
        list.add(ScoreMenu.newInstance(getUniqueID()));
        Log.v("id", getUniqueID());
        return list;
    }

    public List<Fragment> getFragments2() {
        List<Fragment> list = new ArrayList<Fragment>();

        list.add(SettingsMenu.newInstance());
        list.add(GameMenu.newInstance());
        //list.add(ScoreMenu.newInstance(getUniqueID()));
        Log.v("id", getUniqueID());
        return list;
    }

    public String getId(){

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(this, "Besoin de l'accord", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    fragments = getFragments();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    fragments = getFragments2();
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
}
