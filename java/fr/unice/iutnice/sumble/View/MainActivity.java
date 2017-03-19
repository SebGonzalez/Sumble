package fr.unice.iutnice.sumble.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

import java.util.ArrayList;
import java.util.List;

import fr.unice.iutnice.sumble.Controller.SwipePageAdapter;
import fr.unice.iutnice.sumble.Model.Connexion.GetScore;
import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.Fragments.GameMenu;
import fr.unice.iutnice.sumble.View.Fragments.ScoreMenu;
import fr.unice.iutnice.sumble.View.Fragments.SettingsMenu;

public class MainActivity extends FragmentActivity{

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        pagerAdapter = new SwipePageAdapter(super.getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) super.findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

    }

    public List<Fragment> getFragments() {
        List<Fragment> list = new ArrayList<Fragment>();

        list.add(SettingsMenu.newInstance());
        list.add(GameMenu.newInstance());
        list.add(ScoreMenu.newInstance(getId()));
        Log.v("id", getId());
        return list;
    }

    public String getId(){

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
