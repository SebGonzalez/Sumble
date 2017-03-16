package fr.unice.iutnice.sumble.Controller;

import android.content.Intent;
import android.os.Parcelable;
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
    private TypeDifficulte typeDifficulte;

    public ButtonStartListener(GameMenu gameMenu, TypeDifficulte typeDifficulte){
        this.gameMenu = gameMenu;
        this.typeDifficulte = typeDifficulte;
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(gameMenu.getActivity(), GameActivity.class);
        Log.v("checked diff", ""+gameMenu.getCheckedDiff());

<<<<<<< HEAD
        intent.putExtra("difficulte", (Parcelable) gameMenu.getCheckedDiff());
=======
        intent.putExtra("difficulte", (Parcelable)gameMenu.getCheckedDiff());
>>>>>>> 6683c137c052e5ccd794a706df1650198282a56a
        if(gameMenu.getLimitlessChoisi())
            intent.putExtra("mode", "Limitless");
        else
            intent.putExtra("mode", "Challenge");
<<<<<<< HEAD
        intent.putExtra("diff", (Parcelable) typeDifficulte);

=======
        gameMenu.startActivity(intent);
>>>>>>> 6683c137c052e5ccd794a706df1650198282a56a

        //TEST - Score Parcelable : fonctionne
        /*Score score = new Score(22f, gameMenu.getCheckedDiff());
        intent.putExtra("score", score);*/
        gameMenu.startActivity(intent);

    }
}
