package model;

/**
 * Created by Valery on 24.04.2016.
 */
import java.util.ArrayList;

public class Artists {
    private String name, thumbnailUrl;
    private int tracks, albums;
    private ArrayList<String> genres;

    public Artists() {
    }

    public Artists(String name, String thumbnailUrl, int albums, int tracks,
                   ArrayList<String> genres) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.albums = albums;
        this.tracks = tracks;
        this.genres = genres;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getAlbums() {
        return albums;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

}
