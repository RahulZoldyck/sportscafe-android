package app.sportscafe.in.sportscafe.MostViewed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

import app.sportscafe.in.sportscafe.App.Article;
import app.sportscafe.in.sportscafe.App.ContentActivity;
import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;

/**
 * Created by rahul on 16/12/15.
 */
public class MostViewedAdapter extends ArrayAdapter<Article> {
    Context context;
    public MostViewedAdapter(Context context, Article[] objects) {
        super(context, R.layout.fragment_most_viewed_pager, objects);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(context);
        Article article=getItem(position);
        View v=inflater.inflate(R.layout.fragment_most_viewed_pager, parent, false);
        ImageView image =(ImageView)v.findViewById(R.id.MVimage);
        TextView title =(TextView)v.findViewById(R.id.MVtitle);
        TextView tag= (TextView)v.findViewById(R.id.MVtag);
        Picasso.with(v.getContext()).load(Utilites.getInitialImageURL("300", "300", "60", article.getImageUrl())).placeholder(R.mipmap.logo).into(image);
        title.setText(article.getTitle());
        tag.setText(article.getSport());
        return v;
    }
}
