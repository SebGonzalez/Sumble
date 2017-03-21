package fr.unice.iutnice.sumble.Controller;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import fr.unice.iutnice.sumble.Model.Connexion.GetAllScore;
import fr.unice.iutnice.sumble.Model.Connexion.GetScore;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.View.Fragments.ScoreMenu;

/**
 * Created by Gabriel on 20/03/2017.
 */

public class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener{

    private ScoreMenu scoreMenu;
    private String id;

    public SwipeRefreshListener(ScoreMenu scoreMenu, String id){
        this.scoreMenu = scoreMenu;
        this.id = id;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                scoreMenu.getSwipeRefreshLayout().setRefreshing(true);
                scoreMenu.getSwipeRefreshLayout().canChildScrollUp();

                GetScore getScore = new GetScore(scoreMenu);
                getScore.setParametre(id);
                getScore.execute();

                /*GetAllScore getAllScore = new GetAllScore(scoreMenu);
                getAllScore.execute();*/
            }
        }, 1000);

    }
}
