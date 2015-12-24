package app.sportscafe.in.sportscafe.MyFeeds;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import javax.net.ssl.HttpsURLConnection;

import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.R;


public class MyFeedsFragment extends Fragment {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_feeds, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.feedRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

    class AsyncFeeds extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String query = Utilites.getQuery(params);
            JSONArray jsonArray = new JSONArray();
            try {
                 jsonArray = fetchFromRESTAPI(query);
            } catch (JSONException|IOException e) {
                e.printStackTrace();
            }
            
            return null;
        }
    }
}
