package com.casii.droid.musicalstructure;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongsFragment extends Fragment {

    private List<Song> songs;

    public SongsFragment() {
        // Required empty public constructor
    }

    public SongsFragment(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_items, container, false);
        // Create an {@link SongAdapter}, whose data source is a list of {@link Song}s. The
        // adapter knows how to create list items for each item in the list.
        SongAdapter adapter = new SongAdapter(getActivity(), songs);
        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // list_items.xml layout file.
        final ListView listView = (ListView) rootView.findViewById(R.id.list);
        // Set a click listener to start the audio screen when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), PlayActivity.class);
                String title = ((TextView) view.findViewById(R.id.title_text_view)).getText().toString();
                String artist = ((TextView) view.findViewById(R.id.artist_name_text_view)).getText().toString();
                intent.putExtra("TITLE", title);
                intent.putExtra("ARTIST", artist);
                getContext().startActivity(intent);
            }
        });
        // Make the {@link ListView} use the {@link SongAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Song} in the list.
        listView.setAdapter(adapter);
        return rootView;
    }

}
