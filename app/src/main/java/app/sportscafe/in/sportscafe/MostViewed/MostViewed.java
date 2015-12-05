package app.sportscafe.in.sportscafe.MostViewed;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;



public class MostViewed extends Fragment implements MostViewedPagerFragment.OnFragmentInteractionListener{

    MVItem[] dayitems,weekitems,monthitems;
    ViewPager day,week,month;
    SwipeRefreshLayout sr;

    private OnFragmentInteractionListener mListener;

    public MostViewed() {
        // Required empty public constructor
    }


    public static MostViewed newInstance() {
        MostViewed fragment = new MostViewed();
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
        View v = inflater.inflate(R.layout.fragment_most_viewed, container, false);
         day=(ViewPager) v.findViewById(R.id.pagerday);
         week=(ViewPager) v.findViewById(R.id.pagerweek);
         month=(ViewPager) v.findViewById(R.id.pagermonth);
        AsyncMostViewed asyncMostViewed=new AsyncMostViewed();
        asyncMostViewed.execute();
         sr=(SwipeRefreshLayout)v.findViewById(R.id.mvRefresh);
        sr.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        AsyncMostViewed asyncMostViewed=new AsyncMostViewed();
                        asyncMostViewed.execute();
                    }
                }
        );


        return v;
    }

    class AsyncMostViewed extends AsyncTask<Void,Void,JSONObject>{
        JSONArray day=new JSONArray();
        JSONArray week=new JSONArray();
        JSONArray month =new JSONArray();


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
            String queryDay="{\"msg\":{\"conditions\":{\"published\":true,\"classifications.sections.misc\":\"mvday\"},\"projection\":{\"content\":0},\"options\":{\"sort\":{\"publishDate\":-1},\"limit\":4}}}";
            String queryWeek="{\"msg\":{\"conditions\":{\"published\":true,\"classifications.sections.misc\":\"mvweek\"},\"projection\":{\"content\":0},\"options\":{\"sort\":{\"publishDate\":-1},\"limit\":4}}}";
            String queryMonth="{\"msg\":{\"conditions\":{\"published\":true,\"classifications.sections.misc\":\"mvmonth\"},\"projection\":{\"content\":0},\"options\":{\"sort\":{\"publishDate\":-1},\"limit\":4}}}";

            try {
                day=fetchRESTAPI(queryDay);
                week=fetchRESTAPI(queryWeek);
                month=fetchRESTAPI(queryMonth);

                data.put("day",day);
                data.put("week",week);
                data.put("month",month);
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
        JSONArray dayJSON=jsonObject.getJSONArray("day");
        JSONArray weekJSON=jsonObject.getJSONArray("week");
        JSONArray monthJSON=jsonObject.getJSONArray("month");
        dayitems=getArrayFromJSON(dayJSON);
        monthitems=getArrayFromJSON(monthJSON);
        weekitems=getArrayFromJSON(weekJSON);
        MostViewedPagerAdapter dayAdapter=new MostViewedPagerAdapter(getChildFragmentManager(),dayitems);
        MostViewedPagerAdapter weekAdapter=new MostViewedPagerAdapter(getChildFragmentManager(),weekitems);
        MostViewedPagerAdapter monthAdapter=new MostViewedPagerAdapter(getChildFragmentManager(),monthitems);
        day.setAdapter(dayAdapter);
        week.setAdapter(weekAdapter);
        month.setAdapter(monthAdapter);
        sr.setRefreshing(false);


    }

    private MVItem[] getArrayFromJSON(JSONArray jsonArray) throws JSONException {
        MVItem[] mvItemsArray;
        MVItem item;
        JSONObject article;
        List<MVItem> mvList=new ArrayList<>();
        for(int i =0; i< jsonArray.length();i++){
            item=new MVItem();
            article=new JSONObject();
            article=jsonArray.getJSONObject(i);
            item.setTitle(article.getString("title"));
            JSONObject images=article.getJSONObject("images");
            JSONObject feature=images.getJSONObject("featured");
            item.setImg(Utilites.getMVImgURL()+feature.getString("path"));
            JSONObject classification=article.getJSONObject("classifications");
            JSONObject sections=classification.getJSONObject("sections");
            item.setTag(sections.getString("sport"));
            mvList.add(item);
        }
        mvItemsArray=new MVItem[mvList.size()];
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
