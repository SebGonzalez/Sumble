package fr.unice.iutnice.sumble.Controller;

import android.content.Intent;
import android.util.Log;
import android.view.View;

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
        gameMenu.startActivity(intent);
        Log.v("checked diff", ""+gameMenu.getCheckedDiff());
        intent.putExtra("diff", gameMenu.getCheckedDiff());
    }
}
