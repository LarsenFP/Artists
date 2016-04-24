package valery.pankov.artists;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.InputMismatchException;
import java.util.List;

import app.AppController;
import model.Artists;

/**
 * Created by Valery on 24.04.2016.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Artists> artistsItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Artists> artistsItems) {
        this.activity = activity;
        this.artistsItems = artistsItems;
    }

    @Override
    public int getCount() {
        return artistsItems.size();
    }

    @Override
    public Object getItem(int location) {
        return artistsItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView genres = (TextView) convertView.findViewById(R.id.genres);
        TextView albums = (TextView) convertView.findViewById(R.id.Nalbums);
        TextView tracks = (TextView) convertView.findViewById(R.id.Ntracks);


        // getting movie data for the row
        Artists m = artistsItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // name
        name.setText(m.getName());

        // albums
        albums.setText(String.valueOf(m.getAlbums()));



        // genres
        String genresStr = "";
        for (String str : m.getGenres()) {
            genresStr += str + ", ";
        }
        genresStr = genresStr.length() > 0 ? genresStr.substring(0,
                genresStr.length() - 2) : genresStr;
        genres.setText(genresStr);

        // tracks
        tracks.setText(String.valueOf(m.getTracks()));

        return convertView;
    }

}