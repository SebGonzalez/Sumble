package fr.unice.iutnice.sumble.Controller;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.Fragments.GameMenu;
import fr.unice.iutnice.sumble.View.MainActivity;

/**
 * Created by Gabriel on 08/03/2017.
 */

public class ButtonChoixModeListener implements View.OnClickListener {

    private GameMenu gameMenu;


    public ButtonChoixModeListener(GameMenu gameMenu){
        this.gameMenu = gameMenu;
    }

    @Override
    public void onClick(View v) {

        if(v == gameMenu.getChallenge()){
            gameMenu.setBackgroundChallenge(R.drawable.backgroundbuttonvert);
            gameMenu.setBackgroundLimitless(R.drawable.backgroundbuttongris);
            gameMenu.setLimitlessChoisi(false);
        }else if(v == gameMenu.getLimitless()) {
            gameMenu.setBackgroundChallenge(R.drawable.backgroundbuttongris);
            gameMenu.setBackgroundLimitless(R.drawable.backgroundbuttonvert);
            gameMenu.setLimitlessChoisi(true);
        }

        //Animation fadeIn = new AlphaAnimation(0,1);
        //gameMenu.getChoixDiff().setAnimation(fadeIn);
        gameMenu.getChoixDiff().setVisibility(View.VISIBLE);

        Animation myAnim = AnimationUtils.loadAnimation(gameMenu.getContext(), R.anim.bouton_presse);
        MyInterpolator myInterpolator = new MyInterpolator(0.2, 15);
        myAnim.setInterpolator(myInterpolator);
        v.startAnimation(myAnim);


    }
}
