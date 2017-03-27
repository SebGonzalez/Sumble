package fr.unice.iutnice.sumble.Controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Gabriel on 07/03/2017.
 * Classe permettant d'adapter le viewpager avec la liste de fragments passée en paramètre dans le constructeur
 */

public class SwipePageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    /**
     * Constructeur normal
     * @param fm : fragment manager de l'activité qui va appeler ce constructeur
     * @param fragments : liste de tous les fragments pour adapter le ViewPager
     */
    public SwipePageAdapter(FragmentManager fm, List fragments) {
        super(fm);
        this.fragments = fragments; //on set la liste
    }

    /**
      * @param position : position à laquelle le fragment se trouve dans la liste
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    /**
     *
     * @return : taille totale de la liste de fragments (ou nombre d'éléments dans la liste)
     */
    @Override
    public int getCount() {
        return this.fragments.size();
    }

    /**
     * Permet de donner un titre aux onglets de la page score
     * @param position : position de l'onglet
     * @return : le titre à donner
     */
    @Override
    public String getPageTitle(int position) {

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
