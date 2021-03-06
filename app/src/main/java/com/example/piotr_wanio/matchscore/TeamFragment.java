package com.example.piotr_wanio.matchscore;


import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment {

    private Cursor cursor;
    private SQLiteDatabase db;
    SQLiteOpenHelper matchScoreDatabaseHelper;



    public TeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        ImageView teamsLogo = (ImageView)view.findViewById(R.id.teamsLogo);
        TextView teamName = (TextView)view.findViewById(R.id.teamsName);
        Button playersButton = (Button)view.findViewById(R.id.playersButton);
        Button scoresButton = (Button)view.findViewById(R.id.scoresButton);
        Button scheduleButton = (Button)view.findViewById(R.id.scheduleButton);

        Bundle bundle = getArguments();
        int teamId = bundle.getInt("teamId", 0);
        int leagueId = bundle.getInt("leagueId", 0);;


        try {
            if (db == null) {
                matchScoreDatabaseHelper = MatchScoreDatabaseHelper.getInstance(getActivity());
            }
            db = matchScoreDatabaseHelper.getReadableDatabase();

            SimpleService simpleService = new SimpleService(getActivity());
            try {
                simpleService.getTeamsResponse(teamId, leagueId, teamsLogo, teamName);
            }
            catch (Exception e){

            }

            String whereClause = "ID = ?";
            String[] whereArgs = new String[] {
                    String.valueOf(teamId)
            };

            cursor = db.query("Team", new String[]{"_id", "ID", "IMAGE_RESOURCE", "NAME", "ADDRESS", "WEBSITE", "LEAGUE_ID"}, whereClause, whereArgs, null, null, null);

            cursor.moveToPosition(0);
            Context context = teamsLogo.getContext();

            if(cursor.getCount() > 0) {

                String team = cursor.getString(3);
                teamName.setText(cursor.getString(3));
                String teamLogoUrl = cursor.getString(2);
                if (teamLogoUrl.contains(".cvg")) {
                    Uri uri = Uri.parse(teamLogoUrl);

                    RequestBuilder<PictureDrawable> requestBuilder;
                    requestBuilder = GlideApp.with(context)
                            .as(PictureDrawable.class)
                            .placeholder(R.drawable.arsenal_fc)
                            .error(R.drawable.chelsea_fc)
                            .listener(new SvgSoftwareLayerSetter());


                    requestBuilder.load(uri).into(teamsLogo);
                } else {
                    Glide.with(context)
                            .load(teamLogoUrl)
                            .into(teamsLogo);
                }
                Log.d(String.valueOf(team), "count");
            }
        }
        catch (SQLiteException e){
            Toast toast = Toast.makeText(inflater.getContext(), "Baza danych jest niedostępna!", Toast.LENGTH_SHORT);

            toast.show();
        }

        int finalLeagueId = leagueId;
        scoresButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("team", teamName.getText().toString());
            args.putInt("leagueId", finalLeagueId);
            Fragment fragment = new TeamResultFragment();
            fragment.setArguments(args);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.contentTeamView, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        });

        scheduleButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("team", teamName.getText().toString());
            args.putInt("leagueId", finalLeagueId);

            Fragment fragment = new TeamScheduleFragment();
            fragment.setArguments(args);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.contentTeamView, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        });

        playersButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("teamId", teamId);
            Fragment fragment = new PlayersFragment();
            fragment.setArguments(args);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.contentTeamView, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        });
        return view;
    }

}
