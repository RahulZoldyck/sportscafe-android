package app.sportscafe.in.sportscafe.Articles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.sportscafe.in.sportscafe.R;

/**
 * Created by rb on 30/11/15.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>
{
    Context context;
    ArrayList<Article> articles = new ArrayList<>();
    public static String LOGGING="LOGGING";
    public ArticleAdapter(ArrayList<Article> articles_array,Context context)
    {
        this.articles = articles_array;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView_title;
        TextView textView_summary;
        TextView textView_author;
        TextView textView_sport;
        ImageView image;
        public ViewHolder(View V)
        {
            super(V);
            textView_title = (TextView)V.findViewById(R.id.title);
            textView_summary = (TextView)V.findViewById(R.id.summary);
            textView_author = (TextView)V.findViewById(R.id.author);
            textView_sport = (TextView)V.findViewById(R.id.sport);
            image = (ImageView)V.findViewById(R.id.imageView);
        }

    }
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position)
    {
        holder.textView_title.setText(articles.get(position).getTitle());
        //holder.textView_summary.setText(articles.get(position).getSummary());
        holder.textView_author.setText(articles.get(position).getAuthor());
        holder.textView_sport.setText(articles.get(position).getSport().toUpperCase());
        Picasso.with(context)
                .load(articles.get(position).getImage_URL())
                .placeholder(R.drawable.sportscafe)
                .into(holder.image);
    }

    @Override
    public int getItemCount()
    {
        return articles.size();
    }


}
