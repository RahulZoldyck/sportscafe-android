package app.sportscafe.in.sportscafe.MyFeeds;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

import app.sportscafe.in.sportscafe.App.Article;
import app.sportscafe.in.sportscafe.App.SCDataBaseClass;
import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.Articles.ArticleAdapter;
import app.sportscafe.in.sportscafe.Articles.ArticleConstants;
import app.sportscafe.in.sportscafe.R;


public class MyFeedsFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Article> articles;
    ArticleAdapter adapter;
    RecyclerView recyclerView;
    SCDataBaseClass scDataBaseClass;
    SCDataBaseClass.SCDBHelper scdbHelper;
    private String[] gamePrefs;
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public MyFeedsFragment() {
        // Required empty public constructor
    }


    public static MyFeedsFragment newInstance(String param1, String param2) {
        MyFeedsFragment fragment = new MyFeedsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        scDataBaseClass = new SCDataBaseClass(getActivity());
        articles = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_feeds, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.feedRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleAdapter(new ArrayList<Article>(), getActivity(), "long feature");
        recyclerView.setAdapter(adapter);
        new AsyncFeeds().execute("cricket", "football");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncFeeds().execute("cricket", "football");
                adapter.notifyDataSetChanged();

            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public JSONArray fetchFromRESTAPI(String query) throws JSONException, IOException {

        JSONObject js = new JSONObject(query);
        String ps = js.toString();
        byte[] bytes = ps.getBytes();

        URL url = new URL(Utilites.getArticlesWithConditionsURL());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setFixedLengthStreamingMode(bytes.length);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        OutputStream out = connection.getOutputStream();
        out.write(bytes);
        out.close();
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        String result = "";
        while ((line = reader.readLine()) != null) {
            result = result + line;
        }
        return new JSONArray(result);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class AsyncFeeds extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String query = Utilites.getQuery(params);
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = fetchFromRESTAPI(query);
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    String authorName = "SportsCafe";
                    JSONObject json_article = new JSONObject();
                    json_article = jsonArray.getJSONObject(i);
                    String title = json_article.getString(ArticleConstants.TITLE);
                    String date = json_article.getString(ArticleConstants.MODIFICATION_DATE);
                    String authorId = json_article.getString("authorId");                       //TODO Backend change to not provide null author
                    if (authorId.equals("Vijayaleti"))
                        authorName = "Vijayaleti ";
                    else {
                        JSONObject author = json_article.getJSONObject(ArticleConstants.AUTHOR);
                        authorName = author.getString(ArticleConstants.AUTHOR_NAME);
                    }
                    String id = json_article.getString(ArticleConstants.ID);
                    String summary;
                    try {
                        summary = json_article.getString(ArticleConstants.CONTENT_SUMMARY);
                    } catch (Exception e) {
                        summary = "";
                    }
                    JSONObject images = json_article.getJSONObject(ArticleConstants.IMAGES);
                    JSONObject featured = images.getJSONObject(ArticleConstants.FEATURED);
                    String imageURL = featured.getString(ArticleConstants.PATH);
                    JSONObject classifications = json_article.getJSONObject(ArticleConstants.CLASSIFICATIONS);
                    JSONObject sections = classifications.getJSONObject(ArticleConstants.SECTIONS);
                    String articleType = sections.getString(ArticleConstants.ARTICLE_TYPE);
                    String sport = sections.getString(ArticleConstants.SPORT);
                    Article article_temp = new Article();
                    article_temp.setId(id);
                    article_temp.setTitle(title);
                    article_temp.setSummary(summary);
                    article_temp.setImageUrl(imageURL);
                    article_temp.setArticleType(articleType);
                    article_temp.setSport(sport);
                    article_temp.setAuthor(authorName);
                    article_temp.setDate(date);
                    if (!articles.contains(article_temp))
                        articles.add(article_temp);
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Collections.sort(articles, new Utilites.ArticleComparator(getActivity()));
            Collections.reverse(articles);
            scDataBaseClass.insertData(articles);
            //TODO:gamePrefs
            articles = scDataBaseClass.getMyFeeds("cricket", "football");
            adapter = new ArticleAdapter(articles, getActivity(), "long feature");
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
