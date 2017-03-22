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

public class GetAllScore extends AsyncTask{

    private static final String URL_ALLSCORE = "http://sebenforce.alwaysdata.net/projet_android/get_allscore.php";

    private String parametre;

    private ProgressDialog chargement;

    private URL url;
    private HttpURLConnection connection;
    private boolean isOk;

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
                isOk = true;
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

    @Override
    protected void onPostExecute(Object params){

        ArrayList<ArrayList<String>> listArrayList = (ArrayList<ArrayList<String>>) params;
        ArrayList<String> listeScore = new ArrayList<>();

        if(listeScore != null){
            for(int i=0 ; i<listArrayList.size() ; i++){
                for(String score : listArrayList.get(i)){
                    listeScore.add(score);
                }
            }
            mode.getFirstC().setText(listeScore.get(0));
            mode.getSecondC().setText(listeScore.get(1));
            mode.getThirdC().setText(listeScore.get(2));

            mode.getFirstL().setText(listeScore.get(3));
            mode.getSecondL().setText(listeScore.get(4));
            mode.getThirdL().setText(listeScore.get(5));
        }else{
            Toast.makeText(mode.getContext(), "Une erreur est survenue", Toast.LENGTH_SHORT).show();
        }



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
