package app.sportscafe.in.sportscafe.Fixtures;

import java.util.ArrayList;

/**
 * Created by rahul on 15/12/15.
 */
public class FixtureCardItem {
    private String sport, tournamentName;
    private ArrayList<FixtureListItem> listItems;

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public ArrayList<FixtureListItem> getListItems() {
        return listItems;
    }

    public void setListItems(ArrayList<FixtureListItem> listItems) {
        this.listItems = listItems;
    }
}
