package fr.unice.iutnice.sumble.Model.Connexion;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import fr.unice.iutnice.sumble.View.Fragments.ScoreMenu;
import fr.unice.iutnice.sumble.View.GameActivity;

import static fr.unice.iutnice.sumble.R.id.score;

/**
 * Created by Gabriel on 15/03/2017.
 * Récupère le contenu du json à l'adresse indiquée
 * Permet de récupérer les scores persos de l'utilisateur
 */

public class GetScore extends AsyncTask{

    private static final String URL_SCORE = "http://sebenforce.alwaysdata.net/projet_android/get_score.php?";

    private String parametre;

    private ProgressDialog chargement;

    private URL url;
    private HttpURLConnection connection;

    private ScoreMenu scoreMenu;

    /**
     * Constructeur normal
     * @param scoreMenu : fragment dans lequel cette méthode est appelée
     */
    public GetScore(ScoreMenu scoreMenu) {
        this.scoreMenu = scoreMenu;
    }

    /**
     * S'execute en tâche de fond
     * @param params : paramètres de l'url
     * @return : reponse de la requête
     */
    @Override
    protected Object doInBackground(Object[] params) {

        try {
            // protocole http : connexion au serveur
            url = new URL(URL_SCORE+""+parametre);
            Log.v("url", ""+url.toString());
            connection = (HttpURLConnection) url.openConnection(); //ouvre la connexion
            connection.setRequestMethod("GET"); //type de méthode de récupération dans le php
            connection.connect(); //connexion au serveur

            int responseCode = connection.getResponseCode(); //doit valoir 200 pour être ok

            if(responseCode == HttpURLConnection.HTTP_OK){
                Log.v("SendScore", "reponse ok");
                String response = "";
                String line;

                //va lire la réponse du serveur pour la mettre dans la variable "response"
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while((line = br.readLine()) != null){
                    response += line;
                }
                return parseJson(response); //on décode le json grâce à la méthode parseJson
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * S'execute après doInBackground
     * @param params : reponse donnée par "doInBackground"
     */
    @Override
    protected void onPostExecute(Object params){

        //si la réponse du serveur est null, on ne change pas les valeurs
        if(params == null){
            scoreMenu.getFacileValue().setText("-");
            scoreMenu.getMoyenValue().setText("-");
            scoreMenu.getDifficileValue().setText("-");

            scoreMenu.getFacileValueL().setText("-");
            scoreMenu.getMoyenValueL().setText("-");
            scoreMenu.getDifficileValueL().setText("-");
            Toast.makeText(scoreMenu.getContext(), "Pas de connexion internet", Toast.LENGTH_SHORT).show();
        }else {
            ArrayList<String> liste = (ArrayList<String>) params;
            if(!liste.get(0).equals("null"))
                scoreMenu.getFacileValue().setText(liste.get(0));
            else
                scoreMenu.getFacileValue().setText("-");

            if(liste.get(1).equals("null")) {
                scoreMenu.getMoyenValue().setText("-");
            }
            else
                scoreMenu.getMoyenValue().setText(liste.get(1));

            if(!liste.get(2).equals("null"))
                scoreMenu.getDifficileValue().setText(liste.get(2));
            else
                scoreMenu.getDifficileValue().setText("-");


            if(!liste.get(3).equals("null"))
                scoreMenu.getFacileValueL().setText(liste.get(3));
            else
                scoreMenu.getFacileValueL().setText("-");

            if(!liste.get(4).equals("null"))
                scoreMenu.getMoyenValueL().setText(liste.get(4));
            else
                scoreMenu.getMoyenValueL().setText("-");

            if(!liste.get(5).equals("null"))
                scoreMenu.getDifficileValueL().setText(liste.get(5));
            else
                scoreMenu.getDifficileValueL().setText("-");
        }

        scoreMenu.getSwipeRefreshLayout().setRefreshing(false);

        //chargement.hide();
    }

    public ArrayList<String> parseJson(String resultat){
        ArrayList<String>listeScore = new ArrayList<>();
        try {

            //on crée l'objet json
            JSONObject object = new JSONObject(resultat);

            //challenge à partir du premier objet ("object") construit qui permet d'être parcouru
            JSONObject challenge = object.getJSONObject("challenge");
            JSONObject limitless = object.getJSONObject("limitless");

            //et ainsi de suite...

            //challenge
            String cScoreF = challenge.getJSONObject("facile").getString("score");
            String cScoreM = challenge.getJSONObject("moyen").getString("score");
            String cScoreD = challenge.getJSONObject("difficile").getString("score");

            //limitless
            String lScoreF = limitless.getJSONObject("facile").getString("score");
            String lScoreM = limitless.getJSONObject("moyen").getString("score");
            String lScoreD = limitless.getJSONObject("difficile").getString("score");

            //on ajoute tout dans la liste...
            listeScore.add(cScoreF);
            listeScore.add(cScoreM);
            listeScore.add(cScoreD);

            listeScore.add(lScoreF);
            listeScore.add(lScoreM);
            listeScore.add(lScoreD);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listeScore; //...qu'on retourne pour que onPostExecute l'ait
    }

    public void setParametre(String id){
        Log.v("imei", id);
        this.parametre = "id="+id;
    }
}
