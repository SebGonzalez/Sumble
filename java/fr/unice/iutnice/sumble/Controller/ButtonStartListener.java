package fr.unice.iutnice.sumble.Controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.View;

import fr.unice.iutnice.sumble.Model.Connexion.SendScore;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.Fragments.GameMenu;
import fr.unice.iutnice.sumble.View.GameActivity;
import fr.unice.iutnice.sumble.View.MainActivity;

import static android.R.attr.id;

/**
 * Created by Gabriel on 09/03/2017.
 */

public class ButtonStartListener implements View.OnClickListener {

    private GameMenu gameMenu;
    private TypeDifficulte typeDifficulte;

    public ButtonStartListener(GameMenu gameMenu, TypeDifficulte typeDifficulte){
        this.gameMenu = gameMenu;
        this.typeDifficulte = typeDifficulte;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(gameMenu.getActivity(), GameActivity.class);
        Log.v("checked diff", ""+gameMenu.getCheckedDiff());

        if(gameMenu.getLimitlessChoisi())
            intent.putExtra("mode", "Limitless");
        else
            intent.putExtra("mode", "Challenge");

        Log.v("difficulte onClick", ""+typeDifficulte.toString());
        intent.putExtra("difficulte", (Parcelable) typeDifficulte);
        intent.putExtra("id", id);

        gameMenu.startActivity(intent);
        gameMenu.getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);

    }
}
