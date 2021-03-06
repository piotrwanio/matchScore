package com.example.piotr_wanio.matchscore;

import android.app.FragmentTransaction;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class LeagueFragment extends Fragment {


    private Fragment fragment;

    private TextView textView;
    private Button scoresButton, scheduleButton, standingsButton;
    private ImageView leagueLogo;

    public LeagueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_league, container, false);

        leagueLogo = (ImageView)view.findViewById(R.id.leagueLogo);
        textView = (TextView)view.findViewById(R.id.textView);
        standingsButton = (Button)view.findViewById(R.id.button);
        scoresButton = (Button) view.findViewById(R.id.button2);
        scheduleButton = (Button) view.findViewById(R.id.button3);

        Bundle bundle = getArguments();
        int leagueId = bundle.getInt("leagueId", 0);
        String league = "";
        switch (leagueId)
        {
            case 2021:
                league = "Premier League";
                break;
            case 2002:
                league = "Bundesliga";
                break;
            case 2014:
                league = "La Liga";
        }

        Context context = leagueLogo.getContext();
        int id = context.getResources().getIdentifier("drawable/" + league.replaceAll(" ", "_").toLowerCase(),
                null, context.getPackageName());
        leagueLogo.setImageResource(id);
        textView.setText(league);
        standingsButton.setOnClickListener(v->{
            Bundle args = new Bundle();
            fragment = new TableFragment();
            fragment.setArguments(args);
            args.putInt("leagueId", leagueId);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        });

        scoresButton.setOnClickListener(v->{
            Bundle args = new Bundle();
            fragment = new ResultsFragment();
            fragment.setArguments(args);
            args.putInt("leagueId", leagueId);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        });


        scheduleButton.setOnClickListener(v->{
            Bundle args = new Bundle();
            fragment = new ScheduleFragment();
            fragment.setArguments(args);
            args.putInt("leagueId", leagueId);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        });

        return view;
    }



}
