package valery.pankov.artists;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.AppController;
import model.Artists;

public class MainActivity extends AppCompatActivity {
    // Log tag
    final String LOG_TAG = "myLogs";
    //private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://cache-default06e.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json";
    private ProgressDialog pDialog;
    private List<Artists> artistsList = new ArrayList<Artists>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, artistsList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(LOG_TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Artists artists = new Artists();
                                artists.setName(obj.getString("name"));
                                artists.setAlbums(obj.getInt("albums"));
                                artists.setTracks(obj.getInt("tracks"));

                                JSONObject cover = obj.getJSONObject("cover");
                                artists.setThumbnailUrl(cover.getString("small"));
                                Log.d(LOG_TAG, "small: " + cover.getString("small"));

                                //JSONArray coverArray = obj.getJSONArray("cover");
                                //ArrayList<String> covers = new ArrayList<String>();

                                //for (int j = 0; j < coverArray.length(); j++) {
                                //     covers.add((String) coverArray.get(j));
                                //}
                                //Log.d(LOG_TAG, "name: " + artists.toString());




                                // Genre is json array
                                JSONArray genresArray = obj.getJSONArray("genres");
                                ArrayList<String> genres = new ArrayList<String>();
                                for (int j = 0; j < genresArray.length(); j++) {
                                    genres.add((String) genresArray.get(j));
                                }
                                artists.setGenres(genres);
                                //Log.d(LOG_TAG, "genres: " + genres.toString());

                                // adding movie to movies array
                                artistsList.add(artists);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }



}
