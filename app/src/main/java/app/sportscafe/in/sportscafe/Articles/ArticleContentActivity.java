package app.sportscafe.in.sportscafe.Articles;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;

public class ArticleContentActivity extends AppCompatActivity
{
    ArrayList<Article> articles = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter;
    Integer position;
    TextView textview_title;
    TextView textview_summary;
    ListView listview_content;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e)
        {
            Log.d(Utilites.getTAG(),e.toString());
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();

        articles = bundle.getParcelableArrayList(Utilites.getStateArticles());
        position = bundle.getInt(getResources().getString(R.string.extra_position));

        new AsyncArticleContent().execute(articles.get(position).getId());

        textview_title = (TextView)findViewById(R.id.article_title);
        textview_summary = (TextView)findViewById(R.id.article_summary);
        listview_content = (ListView)findViewById(R.id.article_content);
        imageView = (ImageView)findViewById(R.id.article_image);

        textview_title.setText(articles.get(position).getTitle());
        textview_summary.setText(articles.get(position).getSummary());
        Picasso.with(getApplicationContext())
                .load(articles.get(position).getImage_URL())
                .placeholder(R.drawable.sportscafe)
                .into(imageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public class AsyncArticleContent extends AsyncTask<String,Void,Void>
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
                JSONObject contentJSON = jsonResult.getJSONObject("data");
                String content = contentJSON.getString("content");
                articles.get(position).setContent(content);
                CharSequence[] content_sequence = {Html.fromHtml(articles.get(position).getContent())};
                adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),android.R.layout.simple_list_item_1,content_sequence);
                listview_content.setAdapter(adapter);
            } catch (JSONException e)
            {
                Log.d(Utilites.getTAG(),e+"");
            }
        }
    }
}
