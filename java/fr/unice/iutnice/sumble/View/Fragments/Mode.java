package fr.unice.iutnice.sumble.View.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import fr.unice.iutnice.sumble.Model.Connexion.GetAllScore;
import fr.unice.iutnice.sumble.R;

/**
 * Created by Gabriel on 19/03/2017.
 * Fragment qui va afficher les scores mondiaux selon les difficultés
 */

public class Mode extends Fragment {

    private TextView firstC;
    private TextView secondC;
    private TextView thirdC;

    private TextView firstL;
    private TextView secondL;
    private TextView thirdL;

    private String difficulte;

    /**
     * Constructeur normal
     * Créé une instance du fragment mode qui sera directement ajouté dans la liste présente dans scoreMenu
     * @param diff : diffuclté (pour qu'on sache dans quelle onglet le mettre)
     * @return
     */
    public static Mode newInstance(String diff) {

        Bundle args = new Bundle();

        Mode fragment = new Mode();
        args.putString("diff", diff);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Appelée lors de la création de l'instance de Mode
     * @param inflater
     * @param container
     * @param savedInstanceState : bundle pour récupérer la difficulté passée en paramètres
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mode, container, false);

        savedInstanceState = getArguments(); //on récupère les arguments ajoutés dans newInstance()

        difficulte = savedInstanceState.getString("diff"); //on utilise le bundle pour récup la difficulté

        firstC = (TextView)view.findViewById(R.id.firstC);
        secondC = (TextView)view.findViewById(R.id.secondC);
        thirdC = (TextView)view.findViewById(R.id.thirdC);

        firstL = (TextView)view.findViewById(R.id.firstL);
        secondL = (TextView)view.findViewById(R.id.secondL);
        thirdL = (TextView)view.findViewById(R.id.thirdL);

        //on vérifie s'il est connecté avant d'effectuer la requête pour récup les scores
        if(isOnline()){
            getAllScore();
        }else{
            Toast.makeText(getContext(), "Pas de connexion", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    /**
     * Si l'utilisateur revient sur l'appli en étant connecté, on effectue la requête
     */
    @Override
    public void onResume(){
        super.onResume();
        //Log.v("onResume mode", "dedans");
        if(isOnline()){
            getAllScore();
        }
    }

    public void getAllScore(){
        new GetAllScore(this, difficulte).execute();
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

    /**
     * Vérifie la connexion de l'utilisateur
     * @return : true si en ligne & false si hors ligne
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
