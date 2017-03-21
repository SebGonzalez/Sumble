package fr.unice.iutnice.sumble.View.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.unice.iutnice.sumble.Model.Connexion.GetAllScore;
import fr.unice.iutnice.sumble.R;

/**
 * Created by Gabriel on 19/03/2017.
 */

public class Mode extends Fragment {

    private TextView firstC;
    private TextView secondC;
    private TextView thirdC;

    private TextView firstL;
    private TextView secondL;
    private TextView thirdL;

    public static Mode newInstance(String diff) {

        Bundle args = new Bundle();

        Mode fragment = new Mode();
        args.putString("diff", diff);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mode, container, false);

        savedInstanceState = getArguments();
        String difficulte = savedInstanceState.getString("diff");

        firstC = (TextView)view.findViewById(R.id.firstC);
        secondC = (TextView)view.findViewById(R.id.secondC);
        thirdC = (TextView)view.findViewById(R.id.thirdC);

        firstL = (TextView)view.findViewById(R.id.firstL);
        secondL = (TextView)view.findViewById(R.id.secondL);
        thirdL = (TextView)view.findViewById(R.id.thirdL);

        return view;
    }

    public TextView getFirstC() {
        return firstC;
    }

    public TextView getSecondC() {
        return secondC;
    }

    public TextView getThirdC() {
        return thirdC;
    }

    public TextView getFirstL() {
        return firstL;
    }

    public TextView getSecondL() {
        return secondL;
    }

    public TextView getThirdL() {
        return thirdL;
    }
}
