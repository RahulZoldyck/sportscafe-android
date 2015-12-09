package app.sportscafe.in.sportscafe.Fixtures;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

import app.sportscafe.in.sportscafe.R;
import app.sportscafe.in.sportscafe.App.Utilites;


public class Fixture extends android.support.v4.app.Fragment {
    View vh;
    SwipeRefreshLayout layout;
    ListView lv;
    int length,teamLength,matchesLength;
    public  HashMap<String,String> mapIdtoTeam =new HashMap<>();

    private OnFragmentInteractionListener mListener;


    public static Fixture newInstance() {
        Fixture fragment = new Fixture();
        return fragment;
    }

    public Fixture() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_fixtures, container, false);
            vh=v;
        lv=(ListView)v.findViewById(R.id.fixtureListView);
        lv.setDivider(null);
        lv.setDividerHeight(0);
        AsyncFixtures get=new AsyncFixtures();
        get.execute();
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Matches m= (Matches) parent.getItemAtPosition(position);
//                        if(m!=null && !m.getLink().equals("-1")){
//                            Uri uri = Uri.parse(m.getLink());
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(intent);
//                        }
                    }
                }
        );

        layout=(SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        layout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        AsyncFixtures get=new AsyncFixtures();
                        get.execute();
                    }
                }
        );

        return v;
    }

    public class AsyncFixtures extends AsyncTask<Void,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            JSONObject data=new JSONObject();
            TimeZone tz = TimeZone.getTimeZone(getResources().getString(R.string.utc));
            DateFormat df = new SimpleDateFormat(getResources().getString(R.string.dateformatISO));
            df.setTimeZone(tz);
            Date today=new Date(System.currentTimeMillis());
            Date yesterday=new Date(System.currentTimeMillis());
            yesterday.setDate(today.getDate()-3);
            Date tomorrow=new Date(System.currentTimeMillis());
            tomorrow.setDate(today.getDate()+5);
            String ISOtomo=df.format(tomorrow)+getResources().getString(R.string.formatModification);
            String ISOyes=df.format(yesterday)+getResources().getString(R.string.formatModification);

            // getting from REST API
            try {

                JSONObject js=new JSONObject(FixtureConstants.getQuery(ISOyes,ISOtomo));
                String params=js.toString();
                byte[] bytes=params.getBytes();

                URL url= new URL(Utilites.getFixturesURL());
                HttpsURLConnection connection=(HttpsURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setFixedLengthStreamingMode(bytes.length);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");

                OutputStream out=connection.getOutputStream();
                out.write(bytes);
                out.close();
                InputStream in =connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                String line;
                String result="";
                while((line=reader.readLine())!=null){
                    result=result+line;
                }
                JSONObject res=new JSONObject(result);
                data=res.getJSONObject(FixtureConstants.DATA);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return data;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
           parseJSON(jsonObject);

        }
    }

    private void parseJSON(JSONObject jsonObject) {

        List<Matches> list=new ArrayList<>();

        Matches matches=new Matches();


        JSONArray gameArray=new JSONArray();
        JSONArray teams=new JSONArray();
        JSONArray matchesArray=new JSONArray();
        JSONObject tournamentObject=new JSONObject();
        try {
            for(String game : FixtureConstants.GAMES) {
                try {
                    gameArray=jsonObject.getJSONArray(game);
                }catch (Exception e){
                    continue;
                }
                length=gameArray.length();
                for (int i=0;i<length;i++) {
                    tournamentObject=gameArray.getJSONObject(i);
                    teams = tournamentObject.getJSONArray(FixtureConstants.TEAMS);
                    teamLength=teams.length();
                    for(int j=0;j<teamLength;j++){
                        JSONObject team = new JSONObject();
                        team = teams.getJSONObject(j);
                        boolean tryit=false;
                        try{
                             tryit=tournamentObject.getString(FixtureConstants.GAME_TYPE)!=null;

                        }catch (Exception e){
                            tryit=false;
                        }
                        if(tryit){
                            if(tournamentObject.getString(FixtureConstants.GAME_TYPE).equals(FixtureConstants.INDIVIDUALS)){
                            JSONObject player=new JSONObject();
                            player=team.getJSONObject(FixtureConstants.PLAYER_A);
                            matches.setCountry(player.getString(FixtureConstants.COUNTRY));
                            mapIdtoTeam.put(player.getString(FixtureConstants.PLAYER_ID),player.getString(FixtureConstants.PLAYER_NAME_SHORT));


                        }
                            else {

                                mapIdtoTeam.put(team.getString(FixtureConstants.TEAM_ID), team.getString(FixtureConstants.TEAM_DISPLAY_NAME_SHORT));
                            }
                        }
                        else {

                            mapIdtoTeam.put(team.getString(FixtureConstants.TEAM_ID), team.getString(FixtureConstants.TEAM_DISPLAY_NAME_SHORT));
                        }
                    }

                    matchesArray = tournamentObject.getJSONArray(FixtureConstants.MATCHES);
                    matchesLength=matchesArray.length();
                    for(int k=0;k<matchesLength;k++){
                        JSONObject matchObject=matchesArray.getJSONObject(k);
                        matches=new Matches();
                        matches.setGame(game);
                        String link=matchObject.getString(FixtureConstants.MATCH_REPORT_OR_PREVIEW_LINK);
                        if(link!=null)
                            matches.setLink(link);
                        else
                            matches.setLink("-1");
                        matches.setStatus(matchObject.getString(FixtureConstants.MATCH_STATUS));
                        if(tournamentObject.getString(FixtureConstants.GAME_TYPE).equals(FixtureConstants.INDIVIDUALS)){
                            matches.setTournament(tournamentObject.getString(FixtureConstants.TOURNAMENT_SUPER_NAME));
                            matches.setDate(matchObject.getString(FixtureConstants.MATCH_DATE));
                            matches.setId("");
                            JSONObject venue=tournamentObject.getJSONObject(FixtureConstants.TOURNAMENT_VENUE);
                            matches.setVenue(venue.getString(FixtureConstants.CITY)+","+venue.getString(FixtureConstants.COUNTRY));
                            //todo : Debug this team
                            JSONArray team=new JSONArray();
                            List<String> play=new ArrayList<>();
                            for(int r=0;r<team.length();r++){
                                JSONObject teamObject=new JSONObject();
                                teamObject=team.getJSONObject(r);
                                JSONObject teamA=new JSONObject();
                                teamA=teamObject.getJSONObject(FixtureConstants.PLAYER_A);
                                play.add(teamA.getString(FixtureConstants.PLAYER_ID));

                            }
                            String[] players=new String[play.size()];
                            players=play.toArray(players);
                            matches.setTeamId1(players[0]);
                            matches.setTeamId2(players[1]);
                            matches.setTeam1(mapIdtoTeam.get(players[0]));
                            matches.setTeam2(mapIdtoTeam.get(players[1]));


                        }
                        else{
                            matches.setTournament(tournamentObject.getString(FixtureConstants.TOURNAMENT_NAME));
                            matches.setDate(matchObject.getString(FixtureConstants.MATCH_START_DATE));
                            matches.setId(String.valueOf(matchObject.getInt(FixtureConstants.MATCH_ID)));
                            JSONObject venue=matchObject.getJSONObject(FixtureConstants.MATCH_VENUE);
                            matches.setVenue(venue.getString(FixtureConstants.CITY));
                            JSONArray teamid=matchObject.getJSONArray(FixtureConstants.TEAM_IDS);
                            matches.setTeamId1(teamid.getString(0));
                            matches.setTeamId2(teamid.getString(1));
                            matches.setTournamentId(tournamentObject.getString(FixtureConstants.TOURNAMENT_ID));
                            matches.setTeam1(mapIdtoTeam.get(teamid.getString(0)));
                            matches.setTeam2(mapIdtoTeam.get(teamid.getString(1)));
                        }

                        // TODO :check what happens to individual
                        JSONObject result=matchObject.getJSONObject(FixtureConstants.MATCH_RESULT);
                        JSONArray scores=result.getJSONArray(FixtureConstants.MATCH_FINAL_SCORE);
                        if(scores.length()!=0){
                            String score= String.valueOf(scores.getInt(0))+" - "+String.valueOf(scores.getInt(1));
                            matches.setScore(score);
                        }
                        else
                            matches.setScore("-1");
                        list.add(matches);

                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Matches[] matchArray=new Matches[list.size()];
        matchArray=list.toArray(matchArray);
        list=new ArrayList<>();
        for (Matches i :matchArray){
            if(i.getStatus().equals(getResources().getString(R.string.completed)))
                list.add(i);

        }
        for (Matches i :matchArray){
            if(i.getStatus().equals(getResources().getString(R.string.upcoming)))
                list.add(i);

        }
        matchArray=new Matches[list.size()];
        matchArray=list.toArray(matchArray);

        FixtureAdapter adapter=new FixtureAdapter(getContext(),matchArray);
        lv.setAdapter(adapter);
        layout.setRefreshing(false);

;    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

}
