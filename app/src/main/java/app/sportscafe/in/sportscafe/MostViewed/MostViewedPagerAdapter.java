package app.sportscafe.in.sportscafe.MostViewed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MostViewedPagerAdapter extends FragmentPagerAdapter {
    MVItem[] items;
    public MostViewedPagerAdapter(FragmentManager fm,MVItem[] items) {
        super(fm);
        this.items=items;
    }

    @Override
    public Fragment getItem(int position) {
        return MostViewedPagerFragment.newInstance(items[position]);
    }

    @Override
    public int getCount() {
        return items.length;
    }
}
