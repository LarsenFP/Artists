package valery.pankov.artists;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import app.AppController;
import model.Artists;

/**
 * Created by Valery on 24.04.2016.
 */
public class InfoActivity extends AppCompatActivity {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    final String LOG_TAG = "myLogs";


    // URL
    private static final String url = "http://cache-default06e.cdn.yandex.net/download.cdn.yandex.net/mobilization-2016/artists.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_main);
        Bundle extras = getIntent().getExtras();
        final String title = extras.getString("NAME");
        final Integer pos = extras.getInt("POS");

        setTitle(title);

        // Creating volley request obj
        JsonArrayRequest artistReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(LOG_TAG, response.toString());


                try {
                    //Getting json item
                    JSONObject obj = response.getJSONObject(pos);
                    Log.d(LOG_TAG, "posi " + pos);

                    TextView albtracks = (TextView) findViewById(R.id.albtracks);
                    albtracks.setText("песен: " + obj.getString("tracks")+ " \u2022 " + "альбомов: " + obj.getString("albums"));

                    //Get and set description
                    String description = new String(obj.getString("description").getBytes("ISO-8859-1"), "UTF-8");
                    TextView biography = (TextView) findViewById(R.id.biography);
                    biography.setText(description + "\n");

                    // Genres is json array
                    JSONArray genresArray = obj.getJSONArray("genres");
                    ArrayList<String> genres = new ArrayList<String>();
                    for (int j = 0; j < genresArray.length(); j++) {
                        genres.add((String) genresArray.get(j));

                    }
                    TextView genre = (TextView) findViewById(R.id.genres);
                    String genreStr = genres.toString().replaceAll("\\[|\\]", "");
                    genre.setText(genreStr);


                    //Get link and set image
                    JSONObject cover = obj.getJSONObject("cover");
                    final NetworkImageView img = (NetworkImageView) (NetworkImageView) findViewById(R.id.img);
                    img.setImageUrl(cover.getString("big"), imageLoader);
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //Размер картинки понадобится в будущем
                    final int iw = img.getWidth();
                    final int ih = img.getHeight();
                    Log.d(LOG_TAG, "height " + ih);




                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v){
                            Display display = getWindowManager().getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            final int width = size.x;
                            final int height = size.y;
                            final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img.getLayoutParams();
                            int actheight = params.height;

                            //Анимация по увеличению картинки
                            Animation a = new Animation() {
                                @Override
                                protected void applyTransformation(float interpolatedTime, Transformation t) {
                                    params.width = (int)(iw + (width-iw) * interpolatedTime);
                                    params.height = (int)(ih+ (height*3/5-ih) * interpolatedTime);
                                    img.setLayoutParams(params);
                                }
                            };
                            a.setDuration(750);

                            //Анмация по обратному уменьшению картинки
                            Animation b = new Animation() {
                                @Override
                                protected void applyTransformation(float interpolatedTime, Transformation t) {
                                    params.width = (int)(width - (width-iw) * interpolatedTime);
                                    params.height = (int)(height*3/5 - (height*3/5-ih) * interpolatedTime);
                                    img.setLayoutParams(params);
                                }
                            };
                            b.setDuration(750);

                            //Сравниваем размер картинки с исходным и выбираем действие
                            if (actheight==ih){
                                img.startAnimation(a);
                            }
                            else{
                                img.startAnimation(b);
                            }

                        }

                    });

                    Log.d(LOG_TAG, "img " + cover.getString("big"));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, "Error: " + error.getMessage());

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(artistReq);
    }



}
