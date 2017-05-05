package com.casii.droid.musicalstructure;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Casi on 04.05.2017.
 * {@link CategoryAdapter} is a {@link FragmentPagerAdapter} that can provide the layout for
 * each list item based on a data source.
 */
public class CategoryAdapter extends FragmentPagerAdapter {
    /**
     * Context of the app
     */
    private Context context;
    private List<Song> songs;

    /**
     * Create a new {@link CategoryAdapter} object.
     *
     * @param context is the context of the app
     * @param fm      is the fragment manager that will keep each fragment's state in the adapter
     *                across swipes.
     */
    public CategoryAdapter(FragmentManager fm, Context context, List<Song> songs) {
        super(fm);
        this.context = context;
        this.songs = songs;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SongsFragment(songs);
            default:
                List<Artist> artists = new ArrayList<>();
                for (Song song : songs) {
                    artists.add(song.getArtist());
                }
                return new ArtistsFragment(artists);
        }
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.category_songs);
        } else {
            return context.getString(R.string.category_artists);
        }
    }
}