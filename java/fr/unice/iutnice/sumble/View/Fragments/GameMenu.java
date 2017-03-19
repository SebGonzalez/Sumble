package fr.unice.iutnice.sumble.View.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import fr.unice.iutnice.sumble.Controller.ButtonChoixModeListener;
import fr.unice.iutnice.sumble.Controller.ButtonStartListener;
import fr.unice.iutnice.sumble.Controller.RadioButtonListener;
import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.MainActivity;

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

    private RelativeLayout choixDiff;

    private RadioGroup radioGroup;

    private RadioButton facile;
    private RadioButton moyen;
    private RadioButton difficile;

    private boolean limitlessChoisi;
    private TypeDifficulte checkedDiff;

    public final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    public static GameMenu newInstance() {

        Bundle args = new Bundle(1);
        GameMenu fragment = new GameMenu();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
        Log.v("bite2", "bite");
        View view = inflater.inflate(R.layout.page_jouer, container, false);

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

        radioGroup = (RadioGroup)view.findViewById(R.id.radiogroup);
        RadioButtonListener radioButtonListener = new RadioButtonListener(this);
        radioGroup.setOnCheckedChangeListener(radioButtonListener);

        facile = (RadioButton)view.findViewById(R.id.facile);
        moyen = (RadioButton)view.findViewById(R.id.moyen);
        difficile = (RadioButton)view.findViewById(R.id.difficile);

        lancerPartie = (Button)view.findViewById(R.id.lancerPartie);
        lancerPartie.setVisibility(View.INVISIBLE);

        return view;
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
        ButtonStartListener buttonStartListener = new ButtonStartListener(this, checkedDiff);
        lancerPartie.setOnClickListener(buttonStartListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    Log.v("bite", "bite");
                }
                return;
            }

        }
    }
}
