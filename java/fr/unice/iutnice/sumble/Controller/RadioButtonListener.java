package fr.unice.iutnice.sumble.Controller;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.Fragments.GameMenu;

/**
 * Created by Gabriel on 08/03/2017.
 */

public class RadioButtonListener implements RadioGroup.OnCheckedChangeListener{

    private GameMenu gameMenu;

    public RadioButtonListener(GameMenu gameMenu){
        this.gameMenu = gameMenu;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        gameMenu.getLancerPartie().setVisibility(View.VISIBLE);

        if(group.getCheckedRadioButtonId() == gameMenu.getFacile().getId()){
            gameMenu.setCheckedDiff(0);
        }else if(group.getCheckedRadioButtonId() == gameMenu.getMoyen().getId()){
            gameMenu.setCheckedDiff(1);
        }else if(group.getCheckedRadioButtonId() == gameMenu.getDifficile().getId()){
            gameMenu.setCheckedDiff(2);
        }
    }
}
