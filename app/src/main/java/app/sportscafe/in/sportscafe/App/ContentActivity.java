package app.sportscafe.in.sportscafe.App;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import app.sportscafe.in.sportscafe.MostViewed.MostViewedConstants;
import app.sportscafe.in.sportscafe.MostViewed.MostViewedPagerFragment;
import app.sportscafe.in.sportscafe.R;

public class ContentActivity extends AppCompatActivity {
    Article article;
    String imgURL, title, tag, id;
    TextView content, header, summary;
    ImageView contentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_image_transition));
        }
        if (bundle != null) {
            article = bundle.getParcelable(MostViewedPagerFragment.ARG_ITEM);
            assert article != null;
            imgURL = article.getImageUrl();
            title = article.getTitle();
            tag = article.getSport();
            id = article.getId();
        }
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.d(Utilites.getTAG(), e.toString());
        }
        content = (TextView) findViewById(R.id.mvcontent);
        summary = (TextView) findViewById(R.id.mvsummary);
        contentImage = (ImageView) findViewById(R.id.mvContentImage);
        Picasso.with(this).load(Utilites.getInitialImageURL(Utilites.image_width, Utilites.image_height, imgURL)).into(contentImage);
        header = (TextView) findViewById(R.id.mvContentTitle);
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


    public class AsyncMostViewedContent extends AsyncTask<String, Void, Void> {
        String result;

        @Override
        protected Void doInBackground(String... params) {
            String url = Utilites.getArticleContentURL() + params[0];
            try {
                InputStream in = new URL(url).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                result = "";
                while ((line = reader.readLine()) != null)
                    result = result + line;
            } catch (Exception e) {
                Log.d(Utilites.getTAG(), e + "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONObject jsonResult = new JSONObject(result);
                JSONObject dataJSON = jsonResult.getJSONObject(MostViewedConstants.DATA);
                String contentString = dataJSON.getString(MostViewedConstants.CONTENT);
                String summaryString = dataJSON.getString(MostViewedConstants.SUMMARY);
                Html.ImageGetter imageGetter=new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        final Drawable[] d = new Drawable[1];

                        Target target=new Target() {

                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                d[0] =new BitmapDrawable(bitmap);
                                d[0].setBounds(0,0,d[0].getIntrinsicWidth(),d[0].getIntrinsicHeight());
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        };

                        Picasso.with(ContentActivity.this).load(source).into(target);

                        return d[0];
                    }
                };
                content.setText(Html.fromHtml(contentString,imageGetter,null));
                summary.setText(summaryString);
            } catch (JSONException e) {
                Log.d(Utilites.getTAG(), e + "");
            }
        }
    }
}
