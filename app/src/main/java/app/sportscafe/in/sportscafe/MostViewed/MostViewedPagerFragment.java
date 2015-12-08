package app.sportscafe.in.sportscafe.MostViewed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.sportscafe.in.sportscafe.App.Utilites;
import app.sportscafe.in.sportscafe.Articles.Article;
import app.sportscafe.in.sportscafe.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MostViewedPagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MostViewedPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostViewedPagerFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_ITEM = "item";
    private Article items;
    private String imgURL;
    private String tags;
    private String titles;
    private String id;

    private OnFragmentInteractionListener mListener;

    public MostViewedPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment MostViewedPagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MostViewedPagerFragment newInstance(Article item) {
        MostViewedPagerFragment fragment = new MostViewedPagerFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = getArguments().getParcelable(ARG_ITEM);
            assert items != null;
            imgURL=items.getImageUrl();
            tags = items.getSport();
            titles=items.getTitle();
            id=items.getId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_most_viewed_pager, container, false);
        CardView mvCard=(CardView)v.findViewById(R.id.mvCard);
        ImageView img=(ImageView)v.findViewById(R.id.MVimage);
        TextView title=(TextView)v.findViewById(R.id.MVtitle);
        TextView tag=(TextView)v.findViewById(R.id.MVtag);
        mvCard.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        View image = v.findViewById(R.id.MVimage);
                        image.setTransitionName("shared_mvimg_transition");
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),image,image.getTransitionName());
                        Intent i= new Intent(getContext(),MostViewedContentActivity.class);
                        i.putExtra(ARG_ITEM,items);
                        getContext().startActivity(i,options.toBundle());
                    }
                }
        );
        Picasso.with(v.getContext()).load(Utilites.getMVImgURL(0)+imgURL).placeholder(R.mipmap.logo).resize(300,300).into(img);
        title.setText(titles);
        tag.setTextColor(getResources().getColor(R.color.googleBlue));
        tag.setText(tags.toUpperCase());
        return v ;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
