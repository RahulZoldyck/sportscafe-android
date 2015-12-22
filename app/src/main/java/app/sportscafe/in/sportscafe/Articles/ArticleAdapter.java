package app.sportscafe.in.sportscafe.Articles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.sportscafe.in.sportscafe.App.Article;
import app.sportscafe.in.sportscafe.App.ContentActivity;
import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.MostViewed.MostViewedConstants;
import app.sportscafe.in.sportscafe.R;

/**
 * Created by rb on 30/11/15.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>
{
    Context context;
    String imageWidth = "600";
    String imageHeight = "300";
    String imageQuality = "70";
    ArrayList<Article> articles = new ArrayList<>();
    String articleType;
    OkHttpClient okHttpClient = new OkHttpClient();
    OkHttpDownloader okHttpDownloader;
    Picasso picasso;
    public ArticleAdapter(ArrayList<Article> articles_array,Context context,String articleType)
    {
        this.articles = articles_array;
        this.context = context;
        this.articleType=articleType;
        okHttpClient.setCache(new Cache(context.getCacheDir(), Integer.MAX_VALUE));
        okHttpDownloader = new OkHttpDownloader(okHttpClient);
        picasso = new Picasso.Builder(context).downloader(okHttpDownloader).build();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        LinearLayout overlayLayout;
        TextView textViewTitle;
        TextView textViewSummary;
        TextView textViewAuthor;
        TextView textViewSport;
        TextView textViewDate;
        CardView cardView;
        ImageView image;
        public ViewHolder(View V)
        {
            super(V);
            overlayLayout = (LinearLayout)V.findViewById(R.id.overlay_layout);
            cardView = (CardView)V.findViewById(R.id.card_view);
            textViewTitle = (TextView)V.findViewById(R.id.title);
            textViewSummary = (TextView)V.findViewById(R.id.summary);
            textViewAuthor = (TextView)V.findViewById(R.id.author);
            textViewSport = (TextView)V.findViewById(R.id.sport);
            textViewDate = (TextView)V.findViewById(R.id.time);
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
            intent.putExtra(MostViewedConstants.ARG_ITEM,articles.get(getAdapterPosition()));
            context.startActivity(intent,options.toBundle());
        }
    }
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;
        if(articleType.equals("long feature"))      //Inflate different view for news and feature
        {
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_feature,parent,false);
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
        {
            String authorName = articles.get(position).getAuthor();
            int i = authorName.indexOf(' ');
            String authorFirst = "";
            if(i==-1)
            {
                authorFirst = authorName;
            }
            else
            {
                authorFirst = authorName.substring(0, i);
            }
            holder.textViewAuthor.setText(authorFirst);
        }
        holder.textViewDate.setText(articles.get(position).getTime());
        holder.textViewSport.setText(articles.get(position).getSport().toUpperCase());
        if(articleType.equals("long feature"))
        {
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP)
            {
                int dpValue = 5+8; // margin in dips
                float d = context.getResources().getDisplayMetrics().density;
                int margin = (int)(dpValue * d); // margin in pixels
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)holder.overlayLayout.getLayoutParams();
                params.leftMargin = margin; params.bottomMargin = margin;params.rightMargin=margin;
                holder.overlayLayout.setLayoutParams(params);
            }
            picasso.load(Utilites.getInitialImageURL(imageWidth, imageHeight, imageQuality, articles.get(position).getImageUrl()))
                    .placeholder(R.drawable.sportscafe)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.image);
        }
        else
        {
            picasso.load(Utilites.getInitialImageURL("300", "300", "80", articles.get(position).getImageUrl()))
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount()
    {
        return articles.size();
    }


}
