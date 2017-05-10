package com.casii.droid.musicalstructure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Casi on 04.05.2017.
 * {@link ArtistAdapter} is an {@link ArrayAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link Artist} objects.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {
    /**
     * Create a new {@link ArtistAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param artists is the list of {@link Artist}s to be displayed.
     */
    public ArtistAdapter(Context context, List<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.artist_item, parent, false);
        }
        // Get the {@link Artist} object located at this position in the list
        Artist currentArtist = getItem(position);
        // Find the TextView in the artist_item.xml layout with the ID name.
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name);
        // Get the name from the currentArtist object and set this text on
        // the name TextView.
        nameTextView.setText(currentArtist.getName());
        // Return the whole list item layout (containing 1 TextView) so that it can be shown in
        // the ListView.
        return listItemView;
    }
}