package fr.unice.iutnice.sumble.Controller;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import fr.unice.iutnice.sumble.Model.Connexion.SendScore;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.View.Fragments.GameMenu;
import fr.unice.iutnice.sumble.View.GameActivity;
import fr.unice.iutnice.sumble.View.MainActivity;

/**
 * Created by Gabriel on 09/03/2017.
 */

public class ButtonStartListener implements View.OnClickListener {

    private GameMenu gameMenu;

    public ButtonStartListener(GameMenu gameMenu){
        this.gameMenu = gameMenu;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(gameMenu.getActivity(), GameActivity.class);
        Log.v("checked diff", ""+gameMenu.getCheckedDiff());
        //intent.putExtra("diff", gameMenu.getCheckedDiff());

        //TEST - Score Parcelable : fonctionne
        Score score = new Score(22f, gameMenu.getCheckedDiff());
        intent.putExtra("score", score);
        gameMenu.startActivity(intent);
    }
}
