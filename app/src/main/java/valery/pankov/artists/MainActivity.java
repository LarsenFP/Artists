package valery.pankov.artists;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import adapter.CustomListAdapter;
import app.AppController;
import model.Artists;

public class MainActivity extends AppCompatActivity {
    // Log tag
    final String LOG_TAG = "myLogs";
    //private static final String TAG = MainActivity.class.getSimpleName();

    // URL
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
        JsonArrayRequest artistReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(LOG_TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Artists artists = new Artists();

                                String name = new String(obj.getString("name").getBytes("ISO-8859-1"), "UTF-8");
                                artists.setName(name);
                                artists.setAlbums(obj.getInt("albums"));
                                artists.setTracks(obj.getInt("tracks"));

                                JSONObject cover = obj.getJSONObject("cover");
                                artists.setThumbnailUrl(cover.getString("small"));
                                Log.d(LOG_TAG, "small: " + cover.getString("small"));

                                // Genres is json array
                                JSONArray genresArray = obj.getJSONArray("genres");
                                ArrayList<String> genres = new ArrayList<String>();
                                for (int j = 0; j < genresArray.length(); j++) {
                                    genres.add((String) genresArray.get(j));
                                }
                                artists.setGenres(genres);
                                //Log.d(LOG_TAG, "genres: " + genres.toString());

                                // adding artists to artists array
                                artistsList.add(artists);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
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

        //Getting item id and name of artist
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                        + id);
                TextView text = (TextView) view.findViewById(R.id.name);

                String artistname = text.getText().toString();

                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("POS",position);
                intent.putExtra("NAME",artistname);
                Log.d(LOG_TAG, "name: " + artistname);
                startActivity(intent);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(artistReq);
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
