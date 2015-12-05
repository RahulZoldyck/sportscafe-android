package app.sportscafe.in.sportscafe.Fixtures;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
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
    RecyclerView mRecyclerView;
    View vh;
    Context c;
    SwipeRefreshLayout layout;
    ListView lv;
    public static final String[] games={"football","hockey","cricket","badminton"};
    public  HashMap<String,String> map=new HashMap<>();

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
        lv=(ListView)v.findViewById(R.id.listview007);
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
            yesterday.setDate(today.getDate()-2);
            Date tomorrow=new Date(System.currentTimeMillis());
            tomorrow.setDate(today.getDate()+2);
            String ISOtomo=df.format(tomorrow)+getResources().getString(R.string.formatModification);
            String ISOyes=df.format(yesterday)+getResources().getString(R.string.formatModification);



           String rawjson="{\"msg\": {"+
                "\"$redact\": {"+
                "\"$cond\": {"+
                        "\"if\": {"+
                        "\"$or\": [{"+
                            "\"$and\": [{"+
                                "\"$gte\": [\""+ISOyes+"\", \"$tournamentStartDate\"]"+
                            "}, {"+
                                "\"$lt\": [\""+ISOtomo+"\", \"$tournamentEndDate\"]"+
                            "}]"+
                       " }, {"+
                            "\"$and\": [{"+
                                "\"$gte\": [\"$matchStartDate\", \""+ISOyes+"\"]"+
                            "}, {"+
                                "\"$lt\": [\"$matchStartDate\", \""+ISOtomo+"\"]"+
                            "}]"+
                       " }, {"+
                            "\"$and\": [{"+
                                "\"$not\": {"+
                                    "\"$ifNull\": [\"$tournamentStartDate\", false]"+
                                "}"+
                            "}, {"+
                                "\"$not\": {"+
                                    "\"$ifNull\": [\"$matchStartDate\", false]"+
                                "}"+
                            "}]"+
                        "}]"+
                    "},"+
                    "\"then\": \"$$DESCEND\","+
                            "\"else\": \"$$PRUNE\""+
                "}"+
            "}}"+
            "}\"";

            // getting from REST API
            try {

                JSONObject js=new JSONObject(rawjson);
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
                data=res.getJSONObject("data");

            } catch (Exception e) {
                e.printStackTrace();
            }


            return data;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
           manifest(jsonObject);

        }
    }

    private void manifest(JSONObject jsonObject) {

        List<Matches> list=new ArrayList<>();

        Matches matches=new Matches();


        JSONArray gam=new JSONArray();
        JSONArray teams=new JSONArray();
        JSONArray matchs=new JSONArray();
        JSONObject gms=new JSONObject();
        try {
            for(String game : games) {
                try {
                    gam=jsonObject.getJSONArray(game);
                }catch (Exception e){
                    continue;
                }

                for (int i=0;i<gam.length();i++) {
                    gms=gam.getJSONObject(i);
                    teams = gms.getJSONArray("teams");
                    for(int j=0;j<teams.length();j++){
                        JSONObject team = new JSONObject();
                        team = teams.getJSONObject(j);
                        boolean tryit=false;
                        try{
                             tryit=gms.getString("gameType")!=null;

                        }catch (Exception e){
                            tryit=false;
                        }
                        if(tryit){
                            if(gms.getString("gameType").equals("individuals")){
                            JSONObject player=new JSONObject();
                            player=team.getJSONObject("playerA");
                            matches.setCountry(player.getString("country"));
                            map.put(player.getString("playerId"),player.getString("playerNameShort"));


                        }
                            else {

                                map.put(team.getString("teamId"), team.getString("teamDisplayNameShort"));
                            }
                        }
                        else {

                            map.put(team.getString("teamId"), team.getString("teamDisplayNameShort"));
                        }
                    }

                    matchs = gms.getJSONArray("matches");
                    for(int k=0;k<matchs.length();k++){
                        JSONObject mat=matchs.getJSONObject(k);
                        matches=new Matches();
                        matches.setGame(game);
                        String link=mat.getString("matchReportOrPreviewLink");
                        if(link!=null)
                            matches.setLink(link);
                        else
                            matches.setLink("-1");
                        matches.setStatus(mat.getString("matchStatus"));
                        if(gms.getString("gameType").equals("individuals")){
                            matches.setTournament(gms.getString("tournamentSuperName"));
                            matches.setDate(mat.getString("matchDate"));
                            matches.setId("");
                            JSONObject venue=gms.getJSONObject("tournamentVenue");
                            matches.setVenue(venue.getString("city")+","+venue.getString("country"));
                            JSONArray team=new JSONArray();
                            List<String> play=new ArrayList<>();
                            for(int r=0;r<team.length();r++){
                                JSONObject teamObject=new JSONObject();
                                teamObject=team.getJSONObject(r);
                                JSONObject teamA=new JSONObject();
                                teamA=teamObject.getJSONObject("playerA");
                                play.add(teamA.getString("playerId"));

                            }
                            String[] players=new String[play.size()];
                            players=play.toArray(players);
                            matches.setTeamId1(players[0]);
                            matches.setTeamId2(players[1]);
                            matches.setTeam1(map.get(players[0]));
                            matches.setTeam2(map.get(players[1]));


                        }
                        else{
                            matches.setTournament(gms.getString("tournamentName"));
                            matches.setDate(mat.getString("matchStartDate"));
                            matches.setId(String.valueOf(mat.getInt("matchId")));
                            JSONObject venue=mat.getJSONObject("matchVenue");
                            matches.setVenue(venue.getString("city"));
                            JSONArray teamid=mat.getJSONArray("teamIds");
                            matches.setTeamId1(teamid.getString(0));
                            matches.setTeamId2(teamid.getString(1));
                            matches.setTournamentId(gms.getString("tournamentId"));
                            matches.setTeam1(map.get(teamid.getString(0)));
                            matches.setTeam2(map.get(teamid.getString(1)));
                        }

                        // TODO :check what happens to individual
                        JSONObject result=mat.getJSONObject("matchResult");
                        JSONArray scores=result.getJSONArray("matchFinalScore");
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
            if(i.getStatus().equals("Completed"))
                list.add(i);

        }
        for (Matches i :matchArray){
            if(i.getStatus().equals("Upcoming"))
                list.add(i);

        }
        matchArray=new Matches[list.size()];
        matchArray=list.toArray(matchArray);

        FixerAdapter adapter=new FixerAdapter(getContext(),matchArray);
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
