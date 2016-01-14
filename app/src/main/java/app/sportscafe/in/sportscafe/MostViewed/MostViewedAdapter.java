package app.sportscafe.in.sportscafe.MostViewed;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.sportscafe.in.sportscafe.App.Article;
import app.sportscafe.in.sportscafe.App.ContentActivity;
import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;

/**
 * Created by rahul on 16/12/15.
 */

public class MostViewedAdapter extends RecyclerView.Adapter<MostViewedAdapter.CustomViewHolder> {
    Activity activity;
    private Article[] articles;
    private Context mContext;


    public MostViewedAdapter(Context context, Article[] articles, Activity activity) {
        this.articles = articles;
        this.mContext = context;
        this.activity = activity;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_most_viewed_pager, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        Article article = articles[i];
        final int pos = i;
        //Download image using picasso library
        Picasso.with(mContext).load(Utilites.getInitialImageURL(Utilites.image_width, Utilites.image_height, "60", article.getImageUrl()))
                .placeholder(R.mipmap.logo)
                .into(customViewHolder.image);
        customViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                View image = v.findViewById(R.id.MVimage);
                image.setTransitionName("shared_img_transition");
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                activity, image, image.getTransitionName());
                Intent intent = new Intent(mContext,
                        ContentActivity.class);
                intent.putExtra(MostViewedConstants.ARG_ITEM,
                        articles[pos]);
                mContext.startActivity(intent, options.toBundle());
            }
        });

        //Setting text view title
        customViewHolder.title.setText(article.getTitle());
        customViewHolder.tag.setText(article.getSport());
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView tag;
        LinearLayout linearLayout;

        public CustomViewHolder(View view) {
            super(view);
            Typeface montserrat = Typeface.createFromAsset(mContext.getAssets(),  "fonts/montserrat_regular.ttf");
            this.image = (ImageView) view.findViewById(R.id.MVimage);
            this.title = (TextView) view.findViewById(R.id.MVtitle);
            this.title.setTypeface(montserrat);
            this.tag = (TextView) view.findViewById(R.id.MVtag);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.mvCard);
        }
    }
}