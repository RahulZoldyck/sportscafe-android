package app.sportscafe.in.sportscafe.Fixtures;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.sportscafe.in.sportscafe.R;


public class FixtureAdaptor extends RecyclerView.Adapter<FixtureAdaptor.ViewHolder> {
    Matches[] matches;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView t1,t2,title,timeorresult;
        public ViewHolder(View v) {
            super(v);
            t1=(TextView)v.findViewById(R.id.team1);
            t2=(TextView)v.findViewById(R.id.team2);
            title=(TextView)v.findViewById(R.id.title);
            timeorresult=(TextView)v.findViewById(R.id.resultortime);

        }
    }

    public FixtureAdaptor(Matches[] matches){
        this.matches=matches;
    }


    @Override
    public FixtureAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fixture_adapter_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FixtureAdaptor.ViewHolder holder, int position) {
        holder.t1.setText(matches[position].getTeam1());
        holder.t2.setText(matches[position].getTeam2());
       if(matches[position].getStatus().equals("Completed"))
           holder.timeorresult.setText(matches[position].getScore());
       else
           holder.timeorresult.setText(matches[position].getDate());

       holder.title.setText(matches[position].getGame()+" / "+matches[position].getTournament());
    }

    @Override

    public int getItemCount() {
        return matches.length;
    }
}
