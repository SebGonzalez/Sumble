package fr.unice.iutnice.sumble.Controller;

import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RadioGroup;

import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.Fragments.GameMenu;

/**
 * Created by Gabriel on 08/03/2017.
 */

public class RadioButtonListener implements RadioGroup.OnCheckedChangeListener {

    private GameMenu gameMenu;

    public RadioButtonListener(GameMenu gameMenu){
        this.gameMenu = gameMenu;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        gameMenu.getLancerPartie().setVisibility(View.VISIBLE);

    }
}
