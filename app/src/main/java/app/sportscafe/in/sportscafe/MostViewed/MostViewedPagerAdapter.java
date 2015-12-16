package app.sportscafe.in.sportscafe.MostViewed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.sportscafe.in.sportscafe.App.Article;


public class MostViewedPagerAdapter extends FragmentPagerAdapter {
    Article[] items;
    public MostViewedPagerAdapter(FragmentManager fm,Article[] items) {
        super(fm);
        this.items=items;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return items.length;
    }
}
