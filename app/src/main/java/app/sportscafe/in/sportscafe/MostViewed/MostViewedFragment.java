package app.sportscafe.in.sportscafe.MostViewed;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

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

import javax.net.ssl.HttpsURLConnection;

import app.sportscafe.in.sportscafe.App.Article;
import app.sportscafe.in.sportscafe.App.SCDataBaseClass;
import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;


public class MostViewedFragment extends Fragment {
    TabHost.TabSpec spec;
    SCDataBaseClass dataBaseClass;
    View v;
    ViewGroup root;
    Article[] dayitems, weekitems, monthitems;
    int length;
    private TabHost mTabHost;
    private OnFragmentInteractionListener mListener;

    public MostViewedFragment() {
        // Required empty public constructor
    }


    public static MostViewedFragment newInstance() {
        MostViewedFragment fragment = new MostViewedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = container;
        v = inflater.inflate(R.layout.fragment_most_viewed, container, false);
        dataBaseClass = new SCDataBaseClass(getContext());
//        RecyclerView rv=(RecyclerView)v.findViewById(R.id.mvrecycler);
//        RecyclerView.LayoutManager manager= new LinearLayoutManager(getContext());
//        rv.setLayoutManager(manager);

        Article[] dayArrayFromDB, weekArrayFromDB, monthArrayFromDB;
        ArrayList<Article> dayListFromDB = dataBaseClass.getArticleList(Utilites.ARTICLE_TYPE_MVDAY);
        ArrayList<Article> weekListFromDB = dataBaseClass.getArticleList(Utilites.ARTICLE_TYPE_MVWEEK);
        ArrayList<Article> monthListFromDB = dataBaseClass.getArticleList(Utilites.ARTICLE_TYPE_MVMONTH);
        if (dayListFromDB.size() == 0 || monthListFromDB.size() == 0 || weekListFromDB.size() == 0) {
            AsyncMostViewed asyncMostViewed = new AsyncMostViewed();
            asyncMostViewed.execute();
        } else {
            dayArrayFromDB = new Article[dayListFromDB.size()];
            dayArrayFromDB = dayListFromDB.toArray(dayArrayFromDB);
            weekArrayFromDB = new Article[weekListFromDB.size()];
            weekArrayFromDB = weekListFromDB.toArray(weekArrayFromDB);
            monthArrayFromDB = new Article[monthListFromDB.size()];
            monthArrayFromDB = monthListFromDB.toArray(monthArrayFromDB);
            inflateTabs(dayArrayFromDB, monthArrayFromDB, weekArrayFromDB);
            AsyncMostViewed asyncMostViewed = new AsyncMostViewed();
            asyncMostViewed.execute();
        }
        return v;
    }

    private void parseJSON(JSONObject jsonObject) throws JSONException {
        JSONArray dayJSON = jsonObject.getJSONArray(MostViewedConstants.DAY);
        JSONArray weekJSON = jsonObject.getJSONArray(MostViewedConstants.WEEK);
        JSONArray monthJSON = jsonObject.getJSONArray(MostViewedConstants.MONTH);
        dayitems = getArrayFromJSON(dayJSON, Utilites.ARTICLE_TYPE_MVDAY);
        monthitems = getArrayFromJSON(monthJSON, Utilites.ARTICLE_TYPE_MVMONTH);
        weekitems = getArrayFromJSON(weekJSON, Utilites.ARTICLE_TYPE_MVWEEK);
        inflateTabs(dayitems, monthitems, weekitems);


    }
    public void setTabColor(TabHost tabhost) {

        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE); //unselected


            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.RED); //1st tab selected
    }

    private void inflateTabs(Article[] dayitems, Article[] monthitems, Article[] weekitems) {
        mTabHost = (TabHost) v.findViewById(R.id.tabHost);
        mTabHost.setup();
        mTabHost.clearAllTabs();
        mTabHost.setOnTabChangedListener(
                new TabHost.OnTabChangeListener() {
                    @Override
                    public void onTabChanged(String tabId) {
                        setTabColor(mTabHost);
                    }
                }
        );
//        setTabColor(mTabHost);
        final Article[] dayItems = dayitems;
        final Article[] weekItems = weekitems;
        final Article[] monthItems = monthitems;

        spec = mTabHost.newTabSpec("day");
        spec.setIndicator("Day");
        spec.setContent(new TabHost.TabContentFactory() {
                            @Override
                            public View createTabContent(String tag) {
                                LayoutInflater tabInflater = getActivity().getLayoutInflater();
                                View view = tabInflater.inflate(R.layout.mostviewed_tab_layout, null);
                                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tabList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                                MostViewedAdapter adapter = new MostViewedAdapter(getContext(),
                                        dayItems, getActivity());
                                recyclerView.setAdapter(adapter);
                                return view;
                            }
                        }
        );
        mTabHost.addTab(spec);
        // Week
        spec = mTabHost.newTabSpec("Week");
        spec.setIndicator("Week");
        spec.setContent(new TabHost.TabContentFactory() {

                            @Override
                            public View createTabContent(String tag) {
                                LayoutInflater tabInflater = getActivity().getLayoutInflater();
                                View view = tabInflater.inflate(R.layout.mostviewed_tab_layout, null);
                                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tabList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                                MostViewedAdapter adapter = new MostViewedAdapter(getContext(),
                                        weekItems, getActivity());
                                recyclerView.setAdapter(adapter);
                                return view;
                            }
                        }
        );
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec("month");
        spec.setIndicator("Month");
        spec.setContent(new TabHost.TabContentFactory() {

                            @Override
                            public View createTabContent(String tag) {
                                LayoutInflater tabInflater = getActivity().getLayoutInflater();
                                View view = tabInflater.inflate(R.layout.mostviewed_tab_layout, null);
                                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tabList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                                MostViewedAdapter adapter = new MostViewedAdapter(getContext(),
                                        monthItems, getActivity());
                                recyclerView.setAdapter(adapter);
                                return view;
                            }
                        }
        );
        mTabHost.addTab(spec);
    }

    private Article[] getArrayFromJSON(JSONArray jsonArray, String articleType) throws JSONException {
        Article[] mvItemsArray;
        Article item;
        JSONObject article;
        ArrayList<Article> mvList = new ArrayList<>();
        length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            item = new Article();
            article = new JSONObject();
            article = jsonArray.getJSONObject(i);
            item.setId(article.getString(MostViewedConstants.ID));
            item.setTitle(article.getString(MostViewedConstants.TITLE));
            JSONObject images = article.getJSONObject(MostViewedConstants.IMAGES);
            JSONObject feature = images.getJSONObject(MostViewedConstants.FEATURED);
            item.setImageUrl(feature.getString(MostViewedConstants.PATH));
            item.setDate(article.getString(MostViewedConstants.MODIFICATION_DATE));
            JSONObject classification = article.getJSONObject(MostViewedConstants.CLASSIFICATION);
            JSONObject sections = classification.getJSONObject(MostViewedConstants.SECTIONS);
            item.setSport(sections.getString(MostViewedConstants.SPORT));
            item.setArticleType(articleType);
            mvList.add(item);
        }
        dataBaseClass.insertData(mvList);
        mvItemsArray = new Article[mvList.size()];
        mvItemsArray = mvList.toArray(mvItemsArray);
        return mvItemsArray;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    class AsyncMostViewed extends AsyncTask<Void, Void, JSONObject> {
        JSONArray dayArray = new JSONArray();
        JSONArray weekArray = new JSONArray();
        JSONArray monthArray = new JSONArray();


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

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject data = new JSONObject();

            try {
                dayArray = fetchFromRESTAPI(MostViewedConstants.QUERY_DAY);
                weekArray = fetchFromRESTAPI(MostViewedConstants.QUERY_WEEK);
                monthArray = fetchFromRESTAPI(MostViewedConstants.QUERY_MONTH);

                data.put(MostViewedConstants.DAY, dayArray);
                data.put(MostViewedConstants.WEEK, weekArray);
                data.put(MostViewedConstants.MONTH, monthArray);
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return data;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                parseJSON(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
