package fr.unice.iutnice.sumble.Controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Gabriel on 07/03/2017.
 */

public class SwipePageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public SwipePageAdapter(FragmentManager fm, List fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0){
            return "FACILE";
        }else if(position == 1){
            return "INTERMEDIAIRE";
        }else if(position == 2){
            return "DIFFICILE";
        }
        return "";
    }
}
