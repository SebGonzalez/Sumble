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

public class SettingsMenu extends Fragment{

    private ImageView chevronRight;

    public static SettingsMenu newInstance() {

        Bundle args = new Bundle(1);
        SettingsMenu fragment = new SettingsMenu();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_parametres, container, false);

        return view;
    }
}
