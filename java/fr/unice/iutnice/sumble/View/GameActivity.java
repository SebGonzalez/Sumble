package fr.unice.iutnice.sumble.View;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;

import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.View.SurfaceView.SurfaceViewDebutant;
import fr.unice.iutnice.sumble.View.SurfaceView.SurfaceViewIExpert;
import fr.unice.iutnice.sumble.View.SurfaceView.SurfaceViewIntermediaire;

/**
 * Activité dans laquelle le jeu se lance
 */

public class GameActivity extends Activity {

    SurfaceViewDebutant surfaceDebutant;
    SurfaceViewIntermediaire surfaceIntermediaire;
    SurfaceViewIExpert surfaceExpert;
    TypeDifficulte difficulte;

    /**
     * Création de l'activité
     * @param savedInstanceState : bundle pour récupérer les put des Intents
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.game_layout);

        String mode = getIntent().getStringExtra("mode");
        difficulte = getIntent().getParcelableExtra("difficulte");

        //lancement du mode en fonction des modes choisis
        if(difficulte.equals(TypeDifficulte.Facile)) {
            surfaceDebutant = new SurfaceViewDebutant(this, mode);
            setContentView(surfaceDebutant);
        }
        else if(difficulte.equals(TypeDifficulte.Moyen)) {
            surfaceIntermediaire = new SurfaceViewIntermediaire(this, mode);
            setContentView(surfaceIntermediaire);
        }
        else {
            surfaceExpert = new SurfaceViewIExpert(this, mode);
            setContentView(surfaceExpert);
        }

        Log.v("test", "test" + getResources().getConfiguration().orientation);
        if(getResources().getConfiguration().orientation == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }

    /**
     * Si le jeu est mis "en pause", on coupe le son
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(difficulte.equals(TypeDifficulte.Facile)) {
            surfaceDebutant.stopSound();
        }
        else if(difficulte.equals(TypeDifficulte.Moyen)) {
            surfaceIntermediaire.stopSound();
        }
        else {
            surfaceExpert.stopSound();
        }
    }

}
