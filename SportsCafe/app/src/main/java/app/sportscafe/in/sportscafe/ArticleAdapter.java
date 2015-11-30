package app.sportscafe.in.sportscafe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rb on 30/11/15.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>
{
    public String LOGGING="LOGGING";
    MainActivity activity_parent;
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> summary = new ArrayList<>();
    ArrayList<String> image_URL = new ArrayList<>();
    public ArticleAdapter(ArrayList<String> param_title, ArrayList<String> param_summary, ArrayList<String> param_imageURL, MainActivity activity)
    {
        title = param_title;
        summary = param_summary;
        image_URL = param_imageURL;
        activity_parent = activity;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView_title;
        TextView textView_summary;
        ImageView image;
        public ViewHolder(View V)
        {
            super(V);
            textView_title = (TextView)V.findViewById(R.id.title);
            textView_summary = (TextView)V.findViewById(R.id.summary);
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
        holder.textView_title.setText(title.get(position));
        holder.textView_summary.setText(summary.get(position));
        holder.image.setImageBitmap(loadBitmap(position,image_URL.get(position)));
    }

    @Override
    public int getItemCount()
    {
        return title.size();
    }

    public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>
    {
        Bitmap bitmap;
        @Override
        protected Bitmap doInBackground(String... params)
        {
            try
            {
                InputStream in = new URL(params[0]).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                activity_parent.addBitmapToMemoryCache(String.valueOf(params[1]), bitmap);
            } catch (Exception e)
            {
                Log.d(LOGGING,"Exception : "+e);
            }

            return bitmap;
        }
    }
    public Bitmap loadBitmap(int resId,String url)
    {
        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = activity_parent.getBitmapFromMemCache(imageKey);
        Log.d(LOGGING,"Param : "+imageKey);
        if (bitmap != null)
        {
            Log.d(LOGGING,"Bitmap Exists");
            return bitmap;
        } else
        {
            BitmapWorkerTask task = new BitmapWorkerTask();
            task.execute(url,imageKey);
            return BitmapFactory.decodeResource(activity_parent.getResources(),R.drawable.sportscafe);
        }
    }

}
