package fr.unice.iutnice.sumble.View.Fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.util.Set;

import fr.unice.iutnice.sumble.Controller.ButtonChoixModeListener;
import fr.unice.iutnice.sumble.Controller.ButtonStartListener;
import fr.unice.iutnice.sumble.Controller.RadioButtonListener;
import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.MainActivity;

import static android.app.Activity.RESULT_OK;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Gabriel on 07/03/2017.
 */

public class GameMenu extends Fragment {

    private TextView parametres;
    private TextView score;

    //boutons
    private Button limitless;
    private Button challenge;
    private Button lancerPartie;

    private ImageView chevronRight;
    private ImageView chevronLeft;
    private ImageView infosDiff;

    private RelativeLayout choixDiff;

    private RadioGroup radioGroup;

    private RadioButton facile;
    private RadioButton moyen;
    private RadioButton difficile;

    private boolean limitlessChoisi;
    private TypeDifficulte checkedDiff;
    private String id;

    private MediaPlayer mPlayer = null;

    public final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    public static GameMenu newInstance(String id) {

        Bundle args = new Bundle();
        GameMenu fragment = new GameMenu();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_jouer, container, false);

        ImageView titre = (ImageView)view.findViewById(R.id.logo);
        titre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(R.raw.sumble);
            }
        });


        limitless = (Button)view.findViewById(R.id.limitless);
        challenge = (Button)view.findViewById(R.id.challenge);

        ButtonChoixModeListener buttonChoixModeListener = new ButtonChoixModeListener(this);
        limitless.setOnClickListener(buttonChoixModeListener);
        challenge.setOnClickListener(buttonChoixModeListener);

        choixDiff = (RelativeLayout)view.findViewById(R.id.choixDiff);
        choixDiff.setVisibility(View.INVISIBLE);

        parametres = (TextView)view.findViewById(R.id.param);
        score = (TextView)view.findViewById(R.id.score);
        chevronRight = (ImageView)view.findViewById(R.id.chevronright);
        chevronLeft = (ImageView)view.findViewById(R.id.chevronleft);

        infosDiff = (ImageView)view.findViewById(R.id.infosDiff);
        infosDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder infoWindow = new AlertDialog.Builder(getContext());
                infoWindow.setTitle(R.string.infos);
                infoWindow.setMessage("Débutant : Deux chiffres sont affichés dans la bulle, le premier correspond à la valeur de la bulle et le deuxième correspond à la valeur à atteindre avec ce chiffre. Les couleurs de chaque bulle permettent d’identifier les bulles à associer pour former le chiffre voulu.\n\n" +
                        "Intermédiaire : Le deuxième chiffre n’est plus présent.\n\n" +
                        "Expert : Le deuxième chiffre n’est plus présent et il n’y a plus les couleurs");
                infoWindow.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                infoWindow.show();
            }
        });

        radioGroup = (RadioGroup)view.findViewById(R.id.radiogroup);
        RadioButtonListener radioButtonListener = new RadioButtonListener(this);
        radioGroup.setOnCheckedChangeListener(radioButtonListener);

        facile = (RadioButton)view.findViewById(R.id.facile);
        moyen = (RadioButton)view.findViewById(R.id.moyen);
        difficile = (RadioButton)view.findViewById(R.id.difficile);

        lancerPartie = (Button)view.findViewById(R.id.lancerPartie);
        lancerPartie.setVisibility(View.INVISIBLE);

        if(savedInstanceState != null) {
            if(savedInstanceState.getInt("saveVisibilityDiff") == View.VISIBLE)
                choixDiff.setVisibility(View.VISIBLE);
            else if(savedInstanceState.getInt("saveVisibilityDiff") == View.INVISIBLE)
                choixDiff.setVisibility(View.INVISIBLE);

            if(savedInstanceState.getBoolean("saveButton") == false) {
                setBackgroundChallenge(R.drawable.backgroundbuttonvert);
                setBackgroundLimitless(R.drawable.backgroundbuttongris);
            }
            else {
                setBackgroundLimitless(R.drawable.backgroundbuttonvert);
                setBackgroundChallenge(R.drawable.backgroundbuttongris);
            }

            if(savedInstanceState.getParcelable("saveDifficulte") != null) {
                if (savedInstanceState.getParcelable("saveDifficulte").equals(TypeDifficulte.Facile)) {
                    setCheckedDiff(TypeDifficulte.Facile);
                } else if (savedInstanceState.getParcelable("saveDifficulte").equals(TypeDifficulte.Moyen)) {
                    setCheckedDiff(TypeDifficulte.Moyen);
                } else {
                    setCheckedDiff(TypeDifficulte.Difficile);
                }
            }
        }

        savedInstanceState = getArguments();
        id = savedInstanceState.getString("id");

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("saveVisibilityDiff", choixDiff.getVisibility());
        outState.putBoolean("saveButton", limitlessChoisi);
        outState.putParcelable("saveDifficulte", getCheckedDiff());
    }

    public Button getLimitless(){
        return limitless;
    }

    public Button getChallenge(){
        return challenge;
    }

    public RelativeLayout getChoixDiff() {
        return choixDiff;
    }

    public TextView getScore() {
        return score;
    }

    public ImageView getChevronRight() {
        return chevronRight;
    }

    public ImageView getChevronLeft() {
        return chevronLeft;
    }

    public TextView getParametres() {
        return parametres;
    }

    public void setBackgroundLimitless(int couleur){
        limitless.setBackgroundResource(couleur);
    }

    public void setBackgroundChallenge(int couleur){

        challenge.setBackgroundResource(couleur);
    }

    public Button getLancerPartie() {
        return lancerPartie;
    }

    public void setCheckedDiff(TypeDifficulte choix){
        Log.v("set..", choix.toString());
        checkedDiff = choix;
        Log.v("set..", checkedDiff.toString());
    }

    public TypeDifficulte getCheckedDiff(){
        return checkedDiff;
    }

    public RadioButton getFacile() {
        return facile;
    }

    public RadioButton getMoyen() {
        return moyen;
    }

    public RadioButton getDifficile() {
        return difficile;
    }

    public void setLimitlessChoisi(boolean valeur){
        limitlessChoisi = valeur;
    }

    public boolean getLimitlessChoisi(){
        return limitlessChoisi;
    }

    public void setButtonStartListener(){
        ButtonStartListener buttonStartListener = new ButtonStartListener(this, checkedDiff, id);
        lancerPartie.setOnClickListener(buttonStartListener);
    }

    private void playSound(int resId) {
        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(this.getContext(), resId);
        mPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
    }

    public ImageView getInfosDiff(){
        return infosDiff;
    }
}
