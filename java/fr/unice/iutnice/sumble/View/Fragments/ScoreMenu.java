package fr.unice.iutnice.sumble.View.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fr.unice.iutnice.sumble.R;

/**
 * Created by Gabriel on 07/03/2017.
 */

public class ScoreMenu extends Fragment {


    private ImageView chevronLeft;

    public static ScoreMenu newInstance() {

        Bundle args = new Bundle(1);
        ScoreMenu fragment = new ScoreMenu();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_score, container, false);

        return view;
    }
}