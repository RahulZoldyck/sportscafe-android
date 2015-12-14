package app.sportscafe.in.sportscafe.Articles;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import app.sportscafe.in.sportscafe.App.Article;
import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;

public class ArticlesFragment extends Fragment
{
    public Context context;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;

    ArticleAdapter adapter;
    AsyncArticlesTask asyncArticlesTask;

    public String articleType1;
    public String articleType2;

    String image_width="600";
    String image_height="300";

    private OnFragmentInteractionListener mListener;

    public ArticlesFragment()
    {

    }

    public static ArticlesFragment newInstance(String articletype1,String articletype2)
    {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle args = new Bundle();
        args.putString(ArticleConstants.ARTICLE_TYPE1, articletype1);
        args.putString(ArticleConstants.ARTICLE_TYPE2,articletype2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.articleType1 = getArguments().getString(ArticleConstants.ARTICLE_TYPE1, "default");
        this.articleType2 = getArguments().getString(ArticleConstants.ARTICLE_TYPE2, "default");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);
        context = getContext();
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefreshlayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleAdapter(new ArrayList<Article>(),context,articleType1);
        recyclerView.setAdapter(adapter);
        new AsyncArticlesTask().execute();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new AsyncArticlesTask().execute();
                adapter.notifyDataSetChanged();

            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }
    public class AsyncAuthorByID extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            return null;
        }
    }
    public class AsyncArticlesTask extends AsyncTask<Void,Void,Void>
    {
        ArrayList<Article> articles = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params)
        {
            if(articleType1.equals("news"))
                getArticles(articleType1);
            getArticles(articleType2);
            adapter = new ArticleAdapter(articles,context,articleType1);
            adapter.notifyDataSetChanged();
            return null;
        }
        private void getArticles(String articletype)
        {
            String link = Utilites.getArticlesWithConditionsURL();
            JSONObject msg = new JSONObject();
            try
            {
                JSONObject conditions = new JSONObject();
                conditions.accumulate(ArticleConstants.PUBLISHED,true);
                Log.d(Utilites.getTAG(),articleType1);
                conditions.accumulate(ArticleConstants.CLASSIFICATIONS_SECTIONS_ARTICLETYPE,articletype);
                JSONObject projection = new JSONObject();
                projection.accumulate(ArticleConstants.CONTENT,0);
                JSONObject options = new JSONObject();
                JSONObject sort = new JSONObject();
                sort.accumulate(ArticleConstants.PUBLISH_DATE,-1);
                options.accumulate(ArticleConstants.SORT,sort);
                options.accumulate(ArticleConstants.LIMIT,10);
                msg.accumulate(ArticleConstants.CONDITIONS,conditions);
                msg.accumulate(ArticleConstants.PROJECTION,projection);
                msg.accumulate(ArticleConstants.OPTIONS,options);
                JSONObject object = new JSONObject();
                object.accumulate(ArticleConstants.MSG,msg);
                try
                {
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    String param = object.toString();
                    byte[] bytes = param.getBytes();
                    OutputStream out = connection.getOutputStream();
                    out.write(bytes);
                    out.close();
                    // handle the response
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    String result = "";
                    while ((line = reader.readLine()) != null)
                        result = result + line;
                    Log.d(Utilites.getTAG(), result);
                    JSONArray jsonArray = new JSONArray(result);
                    Integer length = jsonArray.length();
                    for (int i = 0; i < length; i++)
                    {
                        JSONObject json_article = new JSONObject();
                        json_article = jsonArray.getJSONObject(i);
                        String title = json_article.getString(ArticleConstants.TITLE);
                        String date = json_article.getString(ArticleConstants.MODIFICATION_DATE);
                        JSONObject author = json_article.getJSONObject(ArticleConstants.AUTHOR);
                        String authorName = author.getString(ArticleConstants.AUTHOR_NAME);
                        String id = json_article.getString(ArticleConstants.ID);
                        String summary = json_article.getString(ArticleConstants.CONTENT_SUMMARY);
                        JSONObject images = json_article.getJSONObject(ArticleConstants.IMAGES);
                        JSONObject featured = images.getJSONObject(ArticleConstants.FEATURED);
                        String imageURL = featured.getString(ArticleConstants.PATH);
                        JSONObject classifications = json_article.getJSONObject(ArticleConstants.CLASSIFICATIONS);
                        JSONObject sections = classifications.getJSONObject(ArticleConstants.SECTIONS);
                        String articleType = sections.getString(ArticleConstants.ARTICLE_TYPE);
                        String sport = sections.getString(ArticleConstants.SPORT);
                        Log.d(Utilites.getTAG(),sport);
                        Article article_temp = new Article();
                        article_temp.setId(id);
                        article_temp.setTitle(title);
                        article_temp.setSummary(summary);
                        article_temp.setImageUrl(imageURL);
                        article_temp.setArticleType(articleType);
                        article_temp.setSport(sport);
                        article_temp.setAuthor(authorName);
                        article_temp.setDate(date);
                        articles.add(article_temp);
                    }
                }
                    catch (Exception e)
                    {
                        Log.d(Utilites.getTAG(),"Error in Connecting : "+e);
                    }

                } catch (JSONException e)
                {
                    Log.d(Utilites.getTAG(),"Error in JSONAccum : "+e);
                }
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }
}
