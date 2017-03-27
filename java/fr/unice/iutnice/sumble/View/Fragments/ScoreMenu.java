package fr.unice.iutnice.sumble.View.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.unice.iutnice.sumble.Controller.SwipePageAdapter;
import fr.unice.iutnice.sumble.Controller.SwipeRefreshListener;
import fr.unice.iutnice.sumble.R;

/**
 * Created by Gabriel on 07/03/2017.
 */

public class ScoreMenu extends Fragment {

    private TextView facileValue;
    private TextView moyenValue;
    private TextView difficileValue;

    private TextView facileValueL;
    private TextView moyenValueL;
    private TextView difficileValueL;

    private ViewPager pager;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView chevronLeft;

    public static ScoreMenu newInstance(String id) {

        Bundle args = new Bundle();
        ScoreMenu fragment = new ScoreMenu();
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_score, container, false);

        savedInstanceState = getArguments();
        String imei = savedInstanceState.getString("id");

        facileValue = (TextView)view.findViewById(R.id.facileValue);
        facileValue.setText("-");

        moyenValue = (TextView)view.findViewById(R.id.moyenValue);
        moyenValue.setText("-");

        difficileValue = (TextView)view.findViewById(R.id.difficileValue);
        difficileValue.setText("-");

        facileValueL = (TextView)view.findViewById(R.id.facileValueL);
        facileValueL.setText("-");

        moyenValueL = (TextView)view.findViewById(R.id.moyenValueL);
        moyenValueL.setText("-");

        difficileValueL = (TextView)view.findViewById(R.id.difficileValueL);
        difficileValueL.setText("-");

        pager = (ViewPager)view.findViewById(R.id.modes);
        pager.setAdapter(new SwipePageAdapter(getChildFragmentManager(), getFragments()));


        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshListener(this, imei));

        return view;
    }

    public TextView getDifficileValue() {
        return difficileValue;
    }

    public TextView getMoyenValue() {
        return moyenValue;
    }

    public TextView getFacileValue() {
        return facileValue;
    }

    public TextView getFacileValueL() {
        return facileValueL;
    }

    public TextView getMoyenValueL() {
        return moyenValueL;
    }

    public TextView getDifficileValueL() {
        return difficileValueL;
    }

    public List<Fragment> getFragments(){
        List<Fragment> list = new ArrayList<Fragment>();

        list.add(Mode.newInstance("facile"));
        list.add(Mode.newInstance("moyen"));
        list.add(Mode.newInstance("difficile"));
        return list;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout(){
        return swipeRefreshLayout;
    }
}
