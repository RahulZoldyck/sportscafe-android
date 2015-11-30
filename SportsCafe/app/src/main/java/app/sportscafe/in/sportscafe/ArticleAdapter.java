package app.sportscafe.in.sportscafe;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rb on 30/11/15.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>
{
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> summary = new ArrayList<>();
    public ArticleAdapter(ArrayList<String> param_title,ArrayList<String> param_summary)
    {
        title = param_title;
        summary = param_summary;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView_title;
        TextView textView_summary;
        ImageView image;
        public ViewHolder(TextView t1,TextView t2,ImageView imageView)
        {
            super(t1);
            textView_title = t1;
            textView_summary = t2;
            image = imageView;
        }


    }
}
