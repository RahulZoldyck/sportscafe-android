package app.sportscafe.in.sportscafe.MostViewed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import app.sportscafe.in.sportscafe.App.ContentActivity;
import app.sportscafe.in.sportscafe.App.SCDataBaseClass;
import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;



public class MostViewedFragment extends Fragment {
    private TabHost mTabHost;
    TabHost.TabSpec spec;
    SCDataBaseClass dataBaseClass;
    View v;
    ViewGroup root;
    Article[] dayitems,weekitems,monthitems;
    int length;

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
        root=container;
         v = inflater.inflate(R.layout.fragment_most_viewed, container, false);
        dataBaseClass=new SCDataBaseClass(getContext());
        mTabHost = (TabHost) v.findViewById(R.id.tabHost);
        mTabHost.setup();

        Article[] dayArrayFromDB,weekArrayFromDB,monthArrayFromDB;
        ArrayList<Article> dayListFromDB=dataBaseClass.getArticleList(Utilites.ARTICLE_TYPE_MVDAY);
        ArrayList<Article> weekListFromDB=dataBaseClass.getArticleList(Utilites.ARTICLE_TYPE_MVWEEK);
        ArrayList<Article> monthListFromDB=dataBaseClass.getArticleList(Utilites.ARTICLE_TYPE_MVMONTH);
        if(dayListFromDB.size()==0 || monthListFromDB.size()==0 || weekListFromDB.size()==0){
            AsyncMostViewed asyncMostViewed=new AsyncMostViewed();
            asyncMostViewed.execute();
        }
        else{
            dayArrayFromDB=new Article[dayListFromDB.size()];
            dayArrayFromDB=dayListFromDB.toArray(dayArrayFromDB);
            weekArrayFromDB=new Article[weekListFromDB.size()];
            weekArrayFromDB=weekListFromDB.toArray(weekArrayFromDB);
            monthArrayFromDB=new Article[monthListFromDB.size()];
            monthArrayFromDB=monthListFromDB.toArray(monthArrayFromDB);
            inflateTabs(dayArrayFromDB,monthArrayFromDB,weekArrayFromDB);
        }
        return v;
    }

    class AsyncMostViewed extends AsyncTask<Void,Void,JSONObject>{
        JSONArray dayArray=new JSONArray();
        JSONArray weekArray=new JSONArray();
        JSONArray monthArray =new JSONArray();


        public JSONArray fetchRESTAPI(String query) throws JSONException, IOException {


                JSONObject js=new JSONObject(query);
                String ps=js.toString();
                byte[] bytes=ps.getBytes();

                URL url= new URL(Utilites.getArticlesWithConditionsURL());
                HttpsURLConnection connection=(HttpsURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setFixedLengthStreamingMode(bytes.length);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");

                OutputStream out=connection.getOutputStream();
                out.write(bytes);
                out.close();
                InputStream in =connection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                String line;
                String result="";
                while((line=reader.readLine())!=null){
                    result=result+line;
                }
            return new JSONArray(result);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject data=new JSONObject();

            try {
                dayArray=fetchRESTAPI(MostViewedConstants.QUERY_DAY);
                weekArray=fetchRESTAPI(MostViewedConstants.QUERY_WEEK);
                monthArray=fetchRESTAPI(MostViewedConstants.QUERY_MONTH);

                data.put(MostViewedConstants.DAY,dayArray);
                data.put(MostViewedConstants.WEEK,weekArray);
                data.put(MostViewedConstants.MONTH,monthArray);
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

    private void parseJSON(JSONObject jsonObject) throws JSONException {
        JSONArray dayJSON=jsonObject.getJSONArray(MostViewedConstants.DAY);
        JSONArray weekJSON=jsonObject.getJSONArray(MostViewedConstants.WEEK);
        JSONArray monthJSON=jsonObject.getJSONArray(MostViewedConstants.MONTH);
        dayitems=getArrayFromJSON(dayJSON,Utilites.ARTICLE_TYPE_MVDAY);
        monthitems=getArrayFromJSON(monthJSON,Utilites.ARTICLE_TYPE_MVMONTH);
        weekitems=getArrayFromJSON(weekJSON,Utilites.ARTICLE_TYPE_MVWEEK);
        inflateTabs(dayitems,monthitems,weekitems);


    }

    private void inflateTabs(Article[] dayitems, Article[] monthitems, Article[] weekitems) {
        final Article[] dayItems=dayitems;
        final Article[] weekItems=weekitems;
        final Article[] monthItems=monthitems;

        spec= mTabHost.newTabSpec("day");
        spec.setIndicator("Day");
        spec.setContent(new TabHost.TabContentFactory() {

                            @Override
                            public View createTabContent(String tag) {
                                LayoutInflater li = getActivity().getLayoutInflater();
                                View view = li.inflate(R.layout.mostviewed_tab_layout, null);
                                ListView listView = (ListView) view.findViewById(R.id.tabList);
                                MostViewedAdapter adapter = new MostViewedAdapter(getContext(), dayItems);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                View image = view.findViewById(R.id.MVimage);
                                                image.setTransitionName("shared_img_transition");
                                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image, image.getTransitionName());
                                                Intent i = new Intent(getContext(), ContentActivity.class);
                                                i.putExtra(MostViewedConstants.ARG_ITEM, (Article) parent.getItemAtPosition(position));
                                                getContext().startActivity(i, options.toBundle());
                                            }
                                        }
                                );

                                return view;
                            }
                        }
        );
        mTabHost.addTab(spec);
        // Week
        spec= mTabHost.newTabSpec("Week");
        spec.setIndicator("Week");
        spec.setContent(new TabHost.TabContentFactory() {

                            @Override
                            public View createTabContent(String tag) {
                                LayoutInflater li = getActivity().getLayoutInflater();
                                View view = li.inflate(R.layout.mostviewed_tab_layout, null);
                                ListView listView = (ListView) view.findViewById(R.id.tabList);
                                MostViewedAdapter adapter = new MostViewedAdapter(getContext(), weekItems);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                View image = view.findViewById(R.id.MVimage);
                                                image.setTransitionName("shared_img_transition");
                                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image, image.getTransitionName());
                                                Intent i = new Intent(getContext(), ContentActivity.class);
                                                i.putExtra(MostViewedConstants.ARG_ITEM, (Article) parent.getItemAtPosition(position));
                                                getContext().startActivity(i, options.toBundle());
                                            }
                                        }
                                );

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
                                LayoutInflater li = getActivity().getLayoutInflater();
                                View view = li.inflate(R.layout.mostviewed_tab_layout, null);
                                ListView listView = (ListView) view.findViewById(R.id.tabList);
                                MostViewedAdapter adapter = new MostViewedAdapter(getContext(), monthItems);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @SuppressLint("NewApi")
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                View image = view.findViewById(R.id.MVimage);
                                                image.setTransitionName("shared_img_transition");
                                                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), image, image.getTransitionName());
                                                Intent i = new Intent(getContext(), ContentActivity.class);
                                                i.putExtra(MostViewedConstants.ARG_ITEM, (Article) parent.getItemAtPosition(position));
                                                getContext().startActivity(i, options.toBundle());
                                            }
                                        }
                                );

                                return view;
                            }
                        }
        );
        mTabHost.addTab(spec);
    }

    private Article[] getArrayFromJSON(JSONArray jsonArray,String articleType) throws JSONException {
        Article[] mvItemsArray;
        Article item;
        JSONObject article;
        ArrayList<Article> mvList = new ArrayList<>();
        length=jsonArray.length();
        for(int i =0; i< length;i++) {
            item=new Article();
            article=new JSONObject();
            article=jsonArray.getJSONObject(i);
            item.setId(article.getString(MostViewedConstants.ID));
            item.setTitle(article.getString(MostViewedConstants.TITLE));
            JSONObject images=article.getJSONObject(MostViewedConstants.IMAGES);
            JSONObject feature=images.getJSONObject(MostViewedConstants.FEATURED);
            item.setImageUrl(feature.getString(MostViewedConstants.PATH));
            item.setDate(article.getString(MostViewedConstants.MODIFICATION_DATE));
            JSONObject classification=article.getJSONObject(MostViewedConstants.CLASSIFICATION);
            JSONObject sections=classification.getJSONObject(MostViewedConstants.SECTIONS);
            item.setSport(sections.getString(MostViewedConstants.SPORT));
            item.setArticleType(articleType);
            mvList.add(item);
        }
        dataBaseClass.insertData(mvList);
        mvItemsArray=new Article[mvList.size()];
        mvItemsArray=mvList.toArray(mvItemsArray);
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
}
