package app.sportscafe.in.sportscafe.Articles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.sportscafe.in.sportscafe.App.Article;
import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.App.ContentActivity;
import app.sportscafe.in.sportscafe.MostViewed.MostViewedPagerFragment;
import app.sportscafe.in.sportscafe.R;

/**
 * Created by rb on 30/11/15.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>
{
    Context context;
    ArrayList<Article> articles = new ArrayList<>();
    String articleType;

    public ArticleAdapter(ArrayList<Article> articles_array,Context context,String articleType)
    {
        this.articles = articles_array;
        this.context = context;
        this.articleType=articleType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textViewTitle;
        TextView textViewSummary;
        TextView textViewAuthor;
        TextView textViewSport;
        ImageView image;
        public ViewHolder(View V)
        {
            super(V);
            textViewTitle = (TextView)V.findViewById(R.id.title);
            textViewSummary = (TextView)V.findViewById(R.id.summary);
            textViewAuthor = (TextView)V.findViewById(R.id.author);
            textViewSport = (TextView)V.findViewById(R.id.sport);
            image = (ImageView)V.findViewById(R.id.imageView);
            V.setOnClickListener(this);

        }

        @SuppressLint("NewApi")
        @Override
        public void onClick(View view)
        {
            View v = view.findViewById(R.id.imageView);
            v.setTransitionName("shared_img_transition");
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context,v,v.getTransitionName());
            Intent intent = new Intent(context,ContentActivity.class);
            //intent.putParcelableArrayListExtra(Utilites.getStateArticles(),articles);
            intent.putExtra(MostViewedPagerFragment.ARG_ITEM,articles.get(getAdapterPosition()));
            context.startActivity(intent,options.toBundle());
        }
    }
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        if(articleType.equals("match report"))      //Inflate different view for news and match report
        {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_news,parent,false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, final int position)
    {
        holder.textViewTitle.setText(articles.get(position).getTitle());
        //holder.textViewSummary.setText(articles.get(position).getSummary());
        if(articleType.equals("match report"))
        {
            holder.textViewAuthor.setText(articles.get(position).getAuthor());
            holder.textViewSport.setText(articles.get(position).getSport().toUpperCase());
        }
        Picasso.with(context)
                .load(Utilites.getInitialImageURL(Utilites.image_width,Utilites.image_height,articles.get(position).getImageUrl()))
                .placeholder(R.drawable.sportscafe)
                .into(holder.image);
    }

    @Override
    public int getItemCount()
    {
        return articles.size();
    }


}
