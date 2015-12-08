package app.sportscafe.in.sportscafe.MostViewed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.Articles.Article;
import app.sportscafe.in.sportscafe.R;

public class MostViewedContentActivity extends AppCompatActivity {
    Article article;
    String imgURL,title,tag,id;
    TextView content,header,summary;
    ImageView contentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            article=bundle.getParcelable(MostViewedPagerFragment.ARG_ITEM);
            assert article != null;
            imgURL=article.getImageUrl();
            title=article.getTitle();
            tag=article.getSport();
            id=article.getId();
        }
        setContentView(R.layout.activity_most_viewed_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        content=(TextView)findViewById(R.id.mvcontent);
        summary=(TextView)findViewById(R.id.mvsummary);
        contentImage=(ImageView)findViewById(R.id.mvContentImage);
        Picasso.with(this).load(Utilites.getMVImgURL(1)+imgURL).into(contentImage);
        header=(TextView)findViewById(R.id.mvContentTitle);
        header.setText(title);

        new AsyncMostViewedContent().execute(id);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public class AsyncMostViewedContent extends AsyncTask<String,Void,Void>
    {
        String result;
        @Override
        protected Void doInBackground(String... params)
        {
            String url = Utilites.getArticleContentURL()+params[0];
            try
            {
                InputStream in = new URL(url).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                result="";
                while((line = reader.readLine())!=null)
                    result = result+line;
            } catch (Exception e)
            {
                Log.d(Utilites.getTAG(),e+"");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            try
            {
                JSONObject jsonResult = new JSONObject(result);
                JSONObject dataJSON = jsonResult.getJSONObject(MostViewedConstants.DATA);
                String contentString = dataJSON.getString(MostViewedConstants.CONTENT);
                String summaryString = dataJSON.getString(MostViewedConstants.SUMMARY);
                content.setText(Html.fromHtml(contentString));
                summary.setText(summaryString);
            } catch (JSONException e)
            {
                Log.d(Utilites.getTAG(),e+"");
            }
        }
    }
}
