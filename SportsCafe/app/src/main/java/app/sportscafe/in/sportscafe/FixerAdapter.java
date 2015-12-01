package app.sportscafe.in.sportscafe;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by rahul on 12/1/15.
 */
public class FixerAdapter extends ArrayAdapter<Matches> {
    public FixerAdapter(Context context, Matches[] objects) {
        super(context, R.layout.fixture_layout, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater r = LayoutInflater.from(getContext());
        View v = r.inflate(R.layout.fixture_layout, parent, false);
        LinearLayout ll=(LinearLayout)v.findViewById(R.id.ll);
        TextView title=(TextView)v.findViewById(R.id.cardtitle);
        TextView team1=(TextView)v.findViewById(R.id.cardteam1);
        TextView team2=(TextView)v.findViewById(R.id.cardteam2);
        TextView matcht=(TextView)v.findViewById(R.id.cardmatch);
        TextView timeorscore=(TextView)v.findViewById(R.id.cardtimeorscore);
        timeorscore.setTypeface(null, Typeface.BOLD);
        Matches match=getItem(position);
        title.setText(match.getGame()+" /"+match.getTournament());
        team1.setText(match.getTeam1());
        team2.setText(match.getTeam2());
        try {
            matcht.setText("Match "+match.getId()+"-"+getDate(match.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(match.getStatus().equals("Completed")){
            timeorscore.setText(match.getScore());
            ll.setBackgroundColor(Color.parseColor("#3f85f4"));
        }

        else{
            try {
                timeorscore.setText(getTime(match.getDate()));
                ll.setBackgroundColor(Color.parseColor("#CE2127"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    private String getDate(String date) throws ParseException {

        java.text.DateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dates=format.parse(date);
        java.text.DateFormat getformat=new SimpleDateFormat("MMM d, yyyy");

        return getformat.format(dates);
    }

    private String getTime(String date) throws ParseException {
        java.text.DateFormat format=new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dates=format.parse(date);
        String amorpm="AM";
        String hr="00",mm;
        mm=String.valueOf(dates.getMinutes());
        if(dates.getHours()>12){
            amorpm="PM";
            hr=String.valueOf(dates.getHours()-12);
        }
        if(dates.getHours()==12){
            amorpm="PM";
            hr=String.valueOf(12);
        }
        if(dates.getHours()<12){
            amorpm="AM";
            hr=String.valueOf(dates.getHours());
        }

        if(dates.getMinutes()<10)
            mm="0"+String.valueOf(dates.getMinutes());



        return hr+":"+mm+" "+amorpm;
    }
   // 2015-10-31T18:30:00.000Z
}
