package fr.unice.iutnice.sumble.View.Fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
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

import org.xmlpull.v1.XmlPullParser;

import fr.unice.iutnice.sumble.Controller.ButtonChoixModeListener;
import fr.unice.iutnice.sumble.Controller.RadioButtonListener;
import fr.unice.iutnice.sumble.R;

/**
 * Created by Gabriel on 07/03/2017.
 */

public class GameMenu extends Fragment{

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


    public static GameMenu newInstance() {

        Bundle args = new Bundle(1);
        GameMenu fragment = new GameMenu();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

}
