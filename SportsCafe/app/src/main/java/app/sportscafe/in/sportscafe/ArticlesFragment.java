package app.sportscafe.in.sportscafe;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    ArrayList<String> arrayList;
    ArrayAdapter adapter;

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
        final ListView listView = (ListView)view.findViewById(R.id.listview);
        arrayList = new ArrayList();
        adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                task = new getArticlesTask();
                task.execute();
                adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,arrayList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
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

    public class getArticlesTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... params)
        {
            String link = util.ret_getArticlesWithConditionsURL();
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
                        arrayList.add(title);
                    }
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
