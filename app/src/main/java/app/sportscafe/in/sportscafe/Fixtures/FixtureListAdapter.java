package app.sportscafe.in.sportscafe.Fixtures;

import android.content.Context;
import android.content.res.Resources;
import android.text.Layout;
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
import java.util.TimeZone;

import app.sportscafe.in.sportscafe.R;

/**
 * Created by rahul on 15/12/15.
 */
public class FixtureListAdapter extends ArrayAdapter<FixtureListItem> {
    Context context;
    public FixtureListAdapter(Context context, FixtureListItem[] objects) {
        super(context, R.layout.fixture_list_layout, objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.fixture_list_layout, parent, false);
        FixtureListItem listItem=getItem(position);
        String date=listItem.getDateTime();
        ImageView teamImg1=(ImageView)v.findViewById(R.id.listTeamImage1);
        ImageView teamImg2=(ImageView)v.findViewById(R.id.listTeamImage2);
        TextView teamName1=(TextView)v.findViewById(R.id.listTeamName1);
        TextView teamName2=(TextView)v.findViewById(R.id.listTeamName2);
        TextView matchName=(TextView)v.findViewById(R.id.listMatchName);
        TextView dateTextView=(TextView)v.findViewById(R.id.listDate);
        TextView monthTextView=(TextView)v.findViewById(R.id.listMonth);
        TextView timeTextView=(TextView)v.findViewById(R.id.listTime);
        Picasso.with(context).load(listItem.getImageUrl1()).placeholder(R.mipmap.logo).into(teamImg1);
        Picasso.with(context).load(listItem.getImageUrl2()).placeholder(R.mipmap.logo).into(teamImg2);
        teamName1.setText(listItem.getTeam1());
        teamName2.setText(listItem.getTeam2());
        matchName.setText(listItem.getMatchName());

        try {
            timeTextView.setText(getTime(date));
            dateTextView.setText(getDate(date));
            monthTextView.setText(getMonth(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return v;
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

    private String getMonth(String date)throws ParseException  {
        Resources resources = getContext().getResources();
        java.text.DateFormat format=new SimpleDateFormat(resources.getString(R.string.parseISO));
        format.setTimeZone(TimeZone.getTimeZone(resources.getString(R.string.gmt)));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(format.parse(date));
        java.text.DateFormat getformat=new SimpleDateFormat(resources.getString(R.string.list_month_format));

        return getformat.format(calendar.getTime());
    }

    private String getDate(String date) throws ParseException {
        Resources resources = getContext().getResources();
        java.text.DateFormat format=new SimpleDateFormat(resources.getString(R.string.parseISO));
        format.setTimeZone(TimeZone.getTimeZone(resources.getString(R.string.gmt)));
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(format.parse(date));
        java.text.DateFormat getformat=new SimpleDateFormat(resources.getString(R.string.list_date_format));

        return getformat.format(calendar.getTime());
    }
}
