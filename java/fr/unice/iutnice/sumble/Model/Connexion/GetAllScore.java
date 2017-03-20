package fr.unice.iutnice.sumble.Model.Connexion;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class GetAllScore extends AsyncTask{

    private static final String URL_ALLSCORE = "http://sebenforce.alwaysdata.net/projet_android/get_allscore.php";

    private String parametre;

    private ProgressDialog chargement;

    private URL url;
    private HttpURLConnection connection;

    private Mode mode;
    private String difficulte;

    public GetAllScore(Mode mode, String difficulte){
        this.mode = mode;
        this.difficulte = difficulte;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            url = new URL(URL_ALLSCORE);
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

                ArrayList<ArrayList<String>> listeAL = new ArrayList<>();

                listeAL.add(parseJson(response, "challenge", difficulte));

                listeAL.add(parseJson(response, "limitless", difficulte));

                return listeAL;
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

        ArrayList<ArrayList<String>> listArrayList = (ArrayList<ArrayList<String>>) params;
        ArrayList<String> listeScore = new ArrayList<>();

        for(int i=0 ; i<listArrayList.size() ; i++){
            for(String score : listArrayList.get(i)){
                listeScore.add(score);
            }
        }

        mode.getFirstC().setText("1er-"+listeScore.get(0));
        mode.getSecondC().setText("2ème-"+listeScore.get(1));
        mode.getThirdC().setText("3ème-"+listeScore.get(2));

        mode.getFirstL().setText("1er-"+listeScore.get(3));
        mode.getSecondL().setText("2ème-"+listeScore.get(4));
        mode.getThirdL().setText("3ème-"+listeScore.get(5));
    }

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
