package com.example.nghenhac;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSongs {
    private static List<Song> favoriteSongs = new ArrayList<>();

    public static List<Song> getFavoriteSongs() {
        return favoriteSongs;
    }

    public static void addFavorite(Song song, Context context) {
        if (!favoriteSongs.contains(song)) {
            favoriteSongs.add(song);
            Toast.makeText(context, "Đã thêm vào mục yêu thích!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Bài hát đã tồn tại trong mục yêu thích!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void removeFavorite(Song song) {
        favoriteSongs.remove(song);
    }
}
