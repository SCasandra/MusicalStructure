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
public class ArtistsFragment extends Fragment {
    private List<Artist> artists;

    public ArtistsFragment() {
        // Required empty public constructor
    }

    public ArtistsFragment(List<Artist> artists) {
        // Required empty public constructor
        this.artists = artists;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_items, container, false);

        // Create an {@link ArtistAdapter}, whose data source is a list of {@link Artist}s. The
        // adapter knows how to create list items for each item in the list.
        ArtistAdapter adapter = new ArtistAdapter(getActivity(), artists);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // list_items.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ArtistDetailsActivity.class);
                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                intent.putExtra("NAME", name);
                intent.putExtra("URL", artists.get(i).getUrl());
                getContext().startActivity(intent);
            }
        });
        // Make the {@link ListView} use the {@link ArtistAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Artist} in the list.
        listView.setAdapter(adapter);
        return rootView;
    }
}
