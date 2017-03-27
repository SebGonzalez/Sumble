package fr.unice.iutnice.sumble.Model.Connexion;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fr.unice.iutnice.sumble.View.FinActivity;
import fr.unice.iutnice.sumble.View.GameActivity;

/**
 * Created by Gabriel on 15/03/2017.
 */

/**
 * Classe qui gère l'envoie de score
 */
public class SendScore extends AsyncTask{

    private static final String URL_SCORE = "http://sebenforce.alwaysdata.net/projet_android/ajouter_score.php?";

    private String parametre;

    private ProgressDialog chargement;

    private URL url;
    private HttpURLConnection connection;

    private FinActivity finActivity;

    /**
     * Constructeur
     * @param finActivity
     */
    public SendScore(FinActivity finActivity){
        this.finActivity = finActivity;
    }

    /**
     * Fonction qui est appelée avant que la tâche soit effectuée
     */
    @Override
    protected void onPreExecute() {
        //on affiche un chargement avant le début de l'envoie
        chargement = new ProgressDialog(finActivity);
        chargement.setTitle("Envoi du score");
        chargement.setMessage("Veuillez patienter");
        chargement.show();
    }

    /**
     * Méthode travaillant en arrière plan
     * @param params
     * @return
     */
    @Override
    protected Object doInBackground(Object[] params) {

        try {
            //on appelle le fichier php et on passe en paramètre le score
            url = new URL(URL_SCORE+""+parametre);
            Log.v("url", ""+url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                Log.v("SendScore", "reponse ok");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * On cache le chargement
     * @param params
     */
    @Override
    protected void onPostExecute(Object params){
        chargement.hide();
    }

    public void setParametre(String parametreScore, String parametreDiff, String id, String mode){
        Log.v("imei", id);
        this.parametre = "score="+parametreScore+"&diff="+parametreDiff+"&id="+id+"&mode="+mode;
    }
}
