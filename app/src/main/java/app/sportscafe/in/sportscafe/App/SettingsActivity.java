package app.sportscafe.in.sportscafe.App;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import app.sportscafe.in.sportscafe.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void gamePrefClicked(View view) {
        final ArrayList itemsSelected = new ArrayList();
        final SharedPreferences sharedPreferences = getSharedPreferences("gamePref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();
        Set<String> gameSet = sharedPreferences.getStringSet("key", null);
        String[] gameArray={};
        if (gameSet != null) {
            gameArray = gameSet.toArray(new String[gameSet.size()]);
        }
        String[] totalGames = getResources().getStringArray(R.array.game_pref);
        boolean[] checkedList=new boolean[totalGames.length];
        boolean isPresent;
        int i =0;
        for(String game : totalGames){
            isPresent=false;
            for(String checkedGame : gameArray){
                if(checkedGame.equals(game))
                    isPresent=true;

            }
            checkedList[i]=isPresent;

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Preference");
        builder.setMultiChoiceItems(totalGames, checkedList,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                            if (isSelected) {
                            itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Set<String> itemSelected = new HashSet<String>(itemsSelected);
                        editor.putStringSet("key", itemSelected);
                        editor.apply();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
    }
}
