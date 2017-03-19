package fr.unice.iutnice.sumble.View.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fr.unice.iutnice.sumble.Model.Connexion.GetScore;
import fr.unice.iutnice.sumble.Model.Connexion.SendScore;
import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.MainActivity;

/**
 * Created by Gabriel on 07/03/2017.
 */

public class ScoreMenu extends Fragment {

    private TextView facileValue;
    private TextView moyenValue;
    private TextView difficileValue;

    private TextView facileValueL;
    private TextView moyenValueL;
    private TextView difficileValueL;

    private int cpt=0;

    private ImageView chevronLeft;

    public static ScoreMenu newInstance(String id) {

        Bundle args = new Bundle();
        ScoreMenu fragment = new ScoreMenu();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_score, container, false);

        savedInstanceState = getArguments();
        String imei = savedInstanceState.getString("id");

        facileValue = (TextView)view.findViewById(R.id.facileValue);
        facileValue.setText("-");

        moyenValue = (TextView)view.findViewById(R.id.moyenValue);
        moyenValue.setText("-");

        difficileValue = (TextView)view.findViewById(R.id.difficileValue);
        difficileValue.setText("-");

        facileValueL = (TextView)view.findViewById(R.id.facileValueL);
        facileValueL.setText("-");

        moyenValueL = (TextView)view.findViewById(R.id.moyenValueL);
        moyenValueL.setText("-");

        difficileValueL = (TextView)view.findViewById(R.id.difficileValueL);
        difficileValueL.setText("-");

        GetScore getScore = new GetScore(this);
        getScore.setParametre(imei);
        getScore.execute();

        return view;
    }

    public TextView getDifficileValue() {
        return difficileValue;
    }

    public TextView getMoyenValue() {
        return moyenValue;
    }

    public TextView getFacileValue() {
        return facileValue;
    }

    public TextView getFacileValueL() {
        return facileValueL;
    }

    public TextView getMoyenValueL() {
        return moyenValueL;
    }

    public TextView getDifficileValueL() {
        return difficileValueL;
    }
}
