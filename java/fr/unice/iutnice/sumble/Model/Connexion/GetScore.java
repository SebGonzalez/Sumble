package fr.unice.iutnice.sumble.Model.Connexion;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Gabriel on 15/03/2017.
 */

public class GetScore extends AsyncTask{

    private static final String URL_SCORE = "http://sebenforce.alwaysdata.net/projet_android/get_score.php?";

    private String parametre;

    private ProgressDialog chargement;

    private URL url;
    private HttpURLConnection connection;

    private ScoreMenu scoreMenu;

    public GetScore(ScoreMenu scoreMenu){
        this.scoreMenu = scoreMenu;
    }

    @Override
    protected void onPreExecute() {

        chargement = new ProgressDialog(scoreMenu.getContext());
        chargement.setTitle("Chargement des données");
        chargement.setMessage("Veuillez patienter");
        chargement.show();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            url = new URL(URL_SCORE+""+parametre);
            Log.v("url", ""+url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                Log.v("SendScore", "reponse ok");
                String response = "";
                String line;

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while((line = br.readLine()) != null){
                    response += line;
                }
                return parseJson(response);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object params){

        if(params == null){
            scoreMenu.getFacileValue().setText("-");
            scoreMenu.getMoyenValue().setText("-");
            scoreMenu.getDifficileValue().setText("-");

            scoreMenu.getFacileValueL().setText("-");
            scoreMenu.getMoyenValueL().setText("-");
            scoreMenu.getDifficileValueL().setText("-");
        }else {
            ArrayList<String> liste = (ArrayList<String>) params;
            scoreMenu.getFacileValue().setText(liste.get(0));
            scoreMenu.getMoyenValue().setText(liste.get(1));
            scoreMenu.getDifficileValue().setText(liste.get(2));

            scoreMenu.getFacileValueL().setText(liste.get(3));
            scoreMenu.getMoyenValueL().setText(liste.get(4));
            scoreMenu.getDifficileValueL().setText(liste.get(5));
        }

        chargement.hide();
    }

    public ArrayList<String> parseJson(String resultat){
        ArrayList<String>listeScore = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(resultat);
            JSONObject challenge = object.getJSONObject("challenge");
            JSONObject limitless = object.getJSONObject("limitless");

            //challenge
            String cScoreF = challenge.getJSONObject("facile").getString("score");
            String cScoreM = challenge.getJSONObject("moyen").getString("score");
            String cScoreD = challenge.getJSONObject("difficile").getString("score");

            //limitless
            String lScoreF = limitless.getJSONObject("facile").getString("score");
            String lScoreM = limitless.getJSONObject("moyen").getString("score");
            String lScoreD = limitless.getJSONObject("difficile").getString("score");

            listeScore.add(cScoreF);
            listeScore.add(cScoreM);
            listeScore.add(cScoreD);

            listeScore.add(lScoreF);
            listeScore.add(lScoreM);
            listeScore.add(lScoreD);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listeScore;
    }

    public void setParametre(String id){
        Log.v("imei", id);
        this.parametre = "id="+id;
    }
}
