package fr.unice.iutnice.sumble.Controller;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import fr.unice.iutnice.sumble.Model.Connexion.GetAllScore;
import fr.unice.iutnice.sumble.Model.Connexion.GetScore;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.View.Fragments.ScoreMenu;

/**
 * Created by Gabriel on 20/03/2017.
 * Scroller vers le haut pour rafraichir la page
 */

public class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener{

    private ScoreMenu scoreMenu;
    private String id;

    /**
     * Constructeur normal
     * @param scoreMenu : fragment
     * @param id : id de l'utilisateur
     */
    public SwipeRefreshListener(ScoreMenu scoreMenu, String id){
        this.scoreMenu = scoreMenu;
        this.id = id;
    }

    /**
     * Lorsque l'utilisateur rafraîchit la page (en scrollant vers le haut), on lance cette méthode
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            //thread qui va durer 1 secondes (1000ms)
            @Override public void run() {
                scoreMenu.getSwipeRefreshLayout().setRefreshing(true); //pour afficher l'icone de chargement
                scoreMenu.getSwipeRefreshLayout().canChildScrollUp();

                GetScore getScore = new GetScore(scoreMenu); //asyncTask(thread) qui execute une requete au webservice pour récupérer les scores perso
                getScore.setParametre(id);
                getScore.execute();

            }
        }, 1000);

    }
}
