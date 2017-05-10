package com.casii.droid.musicalstructure;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://freemusicarchive.org/api/get/tracks.json?api_key=";
    private static final String API_KEY = "XV83RQ1KZQ0PTLTE";
    private static ViewPager viewPager;
    private ProgressDialog progress;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);
        // Find the ViewPager in the activity_main.xml layout with the ID view_pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        // Find the TabLayout in the activity_main.xml layout with the ID tabs that shows the tabs
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        AsyncData asyncTask = new AsyncData();
        asyncTask.execute();
        showProgressBar();
    }

    /**
     * Initial user interface update
     * Update the screen to display information from the given {@link Song}s.
     */
    private void updateUi(List<Song> s) {
        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter categoryAdapter = new CategoryAdapter(getSupportFragmentManager(), getApplicationContext(), s);
        viewPager.setAdapter(categoryAdapter);
        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showProgressBar() {
        progress = new ProgressDialog(this, R.style.DialogStyle);
        progress.setMessage(getString(R.string.progressBar_message));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the songs in the response.
     */
    private class AsyncData extends AsyncTask<String, Void, List<Song>> {

        @Override
        protected List<Song> doInBackground(String... urls) {
            // Create URL object
            URL urlSong = createUrl(URL + API_KEY);
            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponseSong = "";
            try {
                jsonResponseSong = makeHttpRequest(urlSong);
            } catch (IOException e) {
                // TODO Handle the IOException
            }
            // Extract relevant fields from the JSON response and create an {@link Song}(s) object
            List<Song> songs = extractResultFromJson(jsonResponseSong);
            // Return the {@link Song} object as the result for the {@link AsyncData}
            return songs;
        }

        /**
         * Update the screen with the given songs (which was the result of the
         * {@link AsyncData}).
         */
        @Override
        protected void onPostExecute(List<Song> songs) {
            if (songs == null) {
                return;
            }
            // Updates the user interface
            progress.dismiss();
            updateUi(songs);
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            // If the URL is null, then return early
            if (url == null) {
                return jsonResponse;
            }
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
            } catch (IOException e) {
                // TODO: Handle the exception
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link Song} object by parsing out information
         * about the first song from the input songJSON string.
         */
        private List<Song> extractResultFromJson(String songJSON) {
            List<Song> songs = new ArrayList<>();
            try {
                JSONObject baseJsonResponse = new JSONObject(songJSON);
                JSONArray resultsArray = baseJsonResponse.getJSONArray("dataset");

                // If there are results in the results array
                if (resultsArray.length() > 0) {
                    for (int i = 0; i < resultsArray.length(); i++) {
                        // Extract out the i result (which is an song)
                        JSONObject result = resultsArray.getJSONObject(i);
                        String title = result.getString("track_title");
                        String mp3File = result.getString("track_file");
                        String artistName = result.getString("artist_name");
                        String artistUrl = result.getString("artist_url");
                        songs.add(new Song(title, mp3File, new Artist(artistName, artistUrl)));
                    }
                }
            } catch (JSONException e) {
                Log.d("async", "Problem parsing the song JSON results", e);
            }
            return songs;
        }
    }
}
