package fr.unice.iutnice.sumble.View.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.MainActivity;

/**
 * Created by Gabriel on 07/03/2017.
 */

public class SettingsMenu extends Fragment{

    private Spinner languages;
    private Button rafraichirLangue;

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
        ArrayList<String> listeLangues = new ArrayList<>();
        listeLangues.add("Français");
        listeLangues.add("English");
        listeLangues.add("Italiano");

        languages = (Spinner)view.findViewById(R.id.spinner);
        languages.setPrompt(""+R.string.selectionLangue);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, listeLangues);
        languages.setAdapter(adapter);

        languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("dans itemSelected", "ok");
                Resources res = getContext().getResources();
                Configuration config = res.getConfiguration();
                Log.v("pos", position+"");
                switch (position) {
                    case 0:
                        config.setLocale(Locale.FRENCH);
                        break;
                    case 1:
                        config.setLocale(Locale.ENGLISH);
                        break;
                    case 2:
                        config.setLocale(Locale.ITALIAN);
                        break;
                    default:
                        config.setLocale(Locale.FRENCH);
                        break;
                }
                res.updateConfiguration(config, res.getDisplayMetrics());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("dans onNothing", "ok");
                //getActivity().recreate(); //se fait en boucle si dans onItemSelected
            }
        });

        rafraichirLangue = (Button)view.findViewById(R.id.rafraichirLangue);
        rafraichirLangue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Traduction");
                progressDialog.setMessage("Traduction en cours...");
                progressDialog.show();
                getActivity().recreate();
            }
        });


        return view;
    }

}
