package app.sportscafe.in.sportscafe;

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

public class ArticlesFragment extends Fragment
{
    public RecyclerView recyclerView;
    public RecyclerView.Adapter recyclerAdapter;
    public RecyclerView.LayoutManager layoutManager;
    getArticlesTask task;
    SwipeRefreshLayout swipeRefreshLayout;
    Utilites util = new Utilites();
    ArticleAdapter adapter;
    String image_width="800";
    String image_height="400";
    private String mParam1;
    public String LOGGING = "LOGGING";
    //private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ArticlesFragment()
    {
        // Required empty public constructor
    }


    public static ArticlesFragment newInstance()
    {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefreshlayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        MainActivity activity = (MainActivity) getActivity();
        adapter = new ArticleAdapter(new ArrayList<String>(),new ArrayList<String>(),new ArrayList<String>(),activity);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                task = new getArticlesTask();
                task.execute();
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

    public void getArticles()
    {

    }
    public class loadImages extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            return null;
        }
    }
    public class getArticlesTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            ArrayList<String> titles = new ArrayList<>();
            ArrayList<String> summaries = new ArrayList<>();
            ArrayList<String> image_urls = new ArrayList<>();
            String link = util.ret_getArticlesWithConditionsURL();
            String link_image = util.ret_ImageURL();
            JSONObject msg = new JSONObject();
            try
            {
                JSONObject conditions = new JSONObject();
                conditions.accumulate("published",true);
                conditions.accumulate("classifications.sections.articleType","match report");
                JSONObject projection = new JSONObject();
                projection.accumulate("content",0);
                JSONObject options = new JSONObject();
                JSONObject sort = new JSONObject();
                sort.accumulate("publishDate",-1);
                options.accumulate("sort",sort);
                options.accumulate("limit",25);
                msg.accumulate("conditions",conditions);
                msg.accumulate("projection",projection);
                msg.accumulate("options",options);
                JSONObject object = new JSONObject();
                object.accumulate("msg",msg);
                try
                {
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type","application/json; charset=utf-8");
                    String param = object.toString();
                    Log.d(LOGGING,param);
                    byte[] bytes = param.getBytes();
                    OutputStream out = connection.getOutputStream();
                    out.write(bytes);
                    out.close();
                    // handle the response
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    String result="";
                    while((line = reader.readLine())!=null)
                        result = result+line;
                    Log.d(LOGGING,result);
                    JSONArray jsonArray = new JSONArray(result);
                    Integer length = jsonArray.length();
                    for(int i=0;i<length;i++)
                    {
                        JSONObject json_article = new JSONObject();
                        json_article = jsonArray.getJSONObject(i);
                        String title = json_article.getString("title");
                        String summary = json_article.getString("contentSummary");
                        JSONObject images = json_article.getJSONObject("images");
                        JSONObject featured = images.getJSONObject("featured");
                        String imageURL = featured.getString("path");
                        titles.add(title);
                        summaries.add(summary);
                        image_urls.add(link_image+"-cfill-w"+image_width+"-h"+image_height+"-gn/"+imageURL);
                    }
                    MainActivity activity = (MainActivity)getActivity();
                    adapter = new ArticleAdapter(titles,summaries,image_urls,activity);
                    adapter.notifyDataSetChanged();

                } catch (Exception e)
                {
                    Log.d(LOGGING,"Error in Connecting : "+e);
                }

            } catch (JSONException e)
            {
                Log.d(LOGGING,"Error in JSONAccum : "+e);
            }


            return null;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
