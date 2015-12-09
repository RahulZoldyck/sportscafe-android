package app.sportscafe.in.sportscafe.Articles;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
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

import app.sportscafe.in.sportscafe.App.Article;
import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;

public class ArticleContentActivity extends AppCompatActivity
{
    ArrayList<Article> articles = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter;
    Integer position;
    TextView textviewTitle, textviewSummary , textViewAuthor;
    ListView listviewContent;
    ImageView imageViewFullArticle;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);
        if(Build.VERSION.SDK_INT>=21)
        {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_image_transition));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e)
        {
            Log.d(Utilites.getTAG(),e.toString());
        }
        Bundle bundle = getIntent().getExtras();

        articles = bundle.getParcelableArrayList(Utilites.getStateArticles());
        position = bundle.getInt(getResources().getString(R.string.extra_position));

        getSupportActionBar().setTitle(articles.get(position).getTitle());

        new AsyncArticleContent().execute(articles.get(position).getId());

        textviewTitle = (TextView)findViewById(R.id.article_title);
        textviewSummary = (TextView)findViewById(R.id.article_summary);
        textViewAuthor = (TextView)findViewById(R.id.article_author);
        listviewContent = (ListView)findViewById(R.id.article_content);
        imageViewFullArticle = (ImageView)findViewById(R.id.article_image);

        textviewTitle.setText(articles.get(position).getTitle());
        textviewSummary.setText(articles.get(position).getSummary());
        textViewAuthor.setText(articles.get(position).getAuthor());
        Picasso.with(getApplicationContext())
                .load(articles.get(position).getImageUrl())
                .placeholder(R.drawable.sportscafe)
                .into(imageViewFullArticle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_article_content,menu);
        return true;
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
                JSONObject dataJSON = jsonResult.getJSONObject("data");
                String content = dataJSON.getString("content");
                articles.get(position).setContent(content);
                CharSequence[] content_sequence = {Html.fromHtml(articles.get(position).getContent())};
                adapter = new ArrayAdapter<CharSequence>(getApplicationContext(),android.R.layout.simple_list_item_1,content_sequence);
                listviewContent.setAdapter(adapter);
            } catch (JSONException e)
            {
                Log.d(Utilites.getTAG(),e+"");
            }
        }
    }
}
