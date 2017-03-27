package fr.unice.iutnice.sumble.Model.Connexion;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.View.FinActivity;
import fr.unice.iutnice.sumble.View.Fragments.Mode;
import fr.unice.iutnice.sumble.View.Fragments.ScoreMenu;
import fr.unice.iutnice.sumble.View.GameActivity;

/**
 * Created by Gabriel on 15/03/2017.
 */

/**
 * Classe permettant de récupérer les score depuis un fichier php
 */
public class GetAllScore extends AsyncTask{

    private static final String URL_ALLSCORE = "http://sebenforce.alwaysdata.net/projet_android/get_allscore.php";

    private URL url;
    private HttpURLConnection connection;
    private boolean isOk;

    private Mode mode;
    private String difficulte;

    /**
     * Constructeur
     * @param mode
     * @param difficulte
     */
    public GetAllScore(Mode mode, String difficulte){
        this.mode = mode;
        this.difficulte = difficulte;
    }

    @Override
    protected void onPreExecute() {
    }

    /**
     * Méthode travaillant en arrière plan
     * @param params
     * @return
     */
    @Override
    protected Object doInBackground(Object[] params) {

        try {
            //on créé un objet URL de la requête php
            url = new URL(URL_ALLSCORE);
            //on établit une connexion à cette URL en get
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //on récupère le résultat de la requête php
            int responseCode = connection.getResponseCode();

            //si tout c'est bien passé
            if(responseCode == HttpURLConnection.HTTP_OK){
                isOk = true;
                String response = "";
                String line;

                //on récupère chaque caractère de la réponse
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while((line = br.readLine()) != null){
                    response += line;
                }

                //on ajoute les informations dans une liste
                ArrayList<ArrayList<String>> listeAL = new ArrayList<>();

                listeAL.add(parseJson(response, "challenge", difficulte));

                listeAL.add(parseJson(response, "limitless", difficulte));

                return listeAL;
            }else{
                isOk = false;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Méthode appelé lorsque la requête est terminé
     * @param params
     */
    @Override
    protected void onPostExecute(Object params){

        ArrayList<ArrayList<String>> listArrayList = (ArrayList<ArrayList<String>>) params;
        ArrayList<String> listeScore = new ArrayList<>();

        //si la liste des score n'est pas vide on ajoute tous les socre dans la liste
        if(listeScore != null){
            for(int i=0 ; i<listArrayList.size() ; i++){
                for(String score : listArrayList.get(i)){
                    listeScore.add(score);
                }
            }
            //on modifie la valeur des textView
            mode.getFirstC().setText(listeScore.get(0));
            mode.getSecondC().setText(listeScore.get(1));
            mode.getThirdC().setText(listeScore.get(2));

            mode.getFirstL().setText(listeScore.get(3));
            mode.getSecondL().setText(listeScore.get(4));
            mode.getThirdL().setText(listeScore.get(5));
        }else{
            //sinon on affiche une erreur
            Toast.makeText(mode.getContext(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fonction qui gère le JSON
     * @param resultat correspond à la chaine de caractère du json
     * @param mode
     * @param difficulte
     * @return la liste des score du mode et de la difficulté indiqué
     */
    public ArrayList<String> parseJson(String resultat, String mode, String difficulte){
        ArrayList<String>listeScore = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(resultat);
            JSONObject challenge = object.getJSONObject(mode);

            JSONArray score = challenge.getJSONObject(difficulte).getJSONArray("score");

            for(int i=0 ; i<score.length() ; i++){
                listeScore.add(score.getString(i));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listeScore;
    }
}
