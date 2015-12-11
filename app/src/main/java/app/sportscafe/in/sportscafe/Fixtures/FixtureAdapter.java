package app.sportscafe.in.sportscafe.Fixtures;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;


public class FixtureAdapter extends ArrayAdapter<Matches> {
    Context c;
    public FixtureAdapter(Context context, Matches[] objects) {
        super(context, R.layout.fixture_layout, objects);
        c=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater r = LayoutInflater.from(getContext());
        View v = r.inflate(R.layout.fixture_layout, parent, false);
        Matches match=getItem(position);
        CardView ll=(CardView) v.findViewById(R.id.ll);
        TextView title=(TextView)v.findViewById(R.id.cardtitle);
        TextView team1=(TextView)v.findViewById(R.id.cardteam1);
        TextView team2=(TextView)v.findViewById(R.id.cardteam2);
        TextView matchTextView=(TextView)v.findViewById(R.id.cardmatch);
        TextView sport=(TextView)v.findViewById(R.id.cardsport);
        ImageView teamflag1=(ImageView)v.findViewById(R.id.cardTeamimg1);
        ImageView teamflag2=(ImageView)v.findViewById(R.id.cardTeamimg2);
        TextView timeorscore=(TextView)v.findViewById(R.id.cardtimeorscore);
        timeorscore.setTypeface(null, Typeface.BOLD);
        String teamFlagurl1=Utilites.getTeamImg()+"/"+match.getGame()+"/"+match.getTournamentId()+"/"+match.getTeamId1()+".png";
        String teamFlagurl2=Utilites.getTeamImg()+"/"+match.getGame()+"/"+match.getTournamentId()+"/"+match.getTeamId2()+".png";
        Picasso.with(v.getContext())
                .load(teamFlagurl1)
                .placeholder(R.mipmap.logo)
                .into(teamflag1);
        Picasso.with(c).load(teamFlagurl2).placeholder(R.mipmap.logo).into(teamflag2);
        matchTextView.setTypeface(null,Typeface.BOLD_ITALIC);
        Log.d("sportscafe",teamFlagurl1);

        sport.setText(match.getGame().toUpperCase());
        title.setText(match.getTournament());
        team1.setText(match.getTeam1());
        team2.setText(match.getTeam2());
        try {
            matchTextView.setText("Match "+match.getId()+"-"+getDate(match.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(match.getStatus().equals(getContext().getResources().getString(R.string.completed))){
            timeorscore.setText(match.getScore());
            ll.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        }

        else{
            try {
                timeorscore.setText(getTime(match.getDate()));
                ll.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    private String getDate(String date) throws ParseException {

        Resources resources = getContext().getResources();
        java.text.DateFormat format=new SimpleDateFormat(resources.getString(R.string.parseISO));
        format.setTimeZone(TimeZone.getTimeZone(resources.getString(R.string.gmt)));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(format.parse(date));
        java.text.DateFormat getformat=new SimpleDateFormat(resources.getString(R.string.dateFormat));

        return getformat.format(calendar.getTime());
    }

    private String getTime(String date) throws ParseException {
        Resources resources = getContext().getResources();
        SimpleDateFormat format=new SimpleDateFormat(resources.getString(R.string.parseISO));
        format.setTimeZone(TimeZone.getTimeZone(resources.getString(R.string.gmt)));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime( format.parse(date));
        String amorpm=resources.getString(R.string.am);
        String hr="00",mm;
        hr=String.valueOf(calendar.get(Calendar.HOUR));
        mm=String.valueOf(calendar.get(Calendar.MINUTE));

        if(calendar.get(Calendar.HOUR_OF_DAY)>=12){
            amorpm=resources.getString(R.string.pm);
        }
        if(calendar.get(Calendar.HOUR_OF_DAY)<12){
            amorpm=resources.getString(R.string.am);
        }

        if(calendar.get(Calendar.MINUTE)<10)
            mm="0"+String.valueOf(calendar.get(Calendar.MINUTE));



        return hr+":"+mm+" "+amorpm;
    }

}
