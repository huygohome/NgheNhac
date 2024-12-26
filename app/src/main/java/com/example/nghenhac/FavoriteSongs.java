package com.example.nghenhac;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSongs {
    private static final String PREF_NAME = "FavoriteSongsPrefs";
    private static final String KEY_FAVORITES = "favorites";
    private static List<Song> favoriteSongs = new ArrayList<>();

    public static List<Song> getFavoriteSongs() {
        return favoriteSongs;
    }

    // Gọi loadFavorites() trong một phương thức khởi tạo, hoặc khi ứng dụng khởi động
    public static void initialize(Context context) {
        loadFavorites(context); // Đảm bảo danh sách được tải khi ứng dụng bắt đầu
    }

    // Tải danh sách yêu thích từ SharedPreferences
    public static void loadFavorites(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedFavorites = sharedPreferences.getString(KEY_FAVORITES, "");

        favoriteSongs.clear(); // Xóa dữ liệu cũ
        if (!savedFavorites.isEmpty()) {
            String[] songsData = savedFavorites.split(";");
            for (String songData : songsData) {
                String[] attributes = songData.split("\\|");
                if (attributes.length == 5) {
                    int imageResId = Integer.parseInt(attributes[0]);
                    String name = attributes[1];
                    String artist = attributes[2];
                    String duration = attributes[3];
                    String filePath = attributes[4];

                    Song song = new Song(imageResId, name, artist, duration, filePath);
                    favoriteSongs.add(song);
                }
            }
        }
    }

    public static void addFavorite(Song song, Context context) {
        if (!favoriteSongs.contains(song)) {
            favoriteSongs.add(song);
            saveFavorites(context); // Lưu lại danh sách sau khi thêm
            Toast.makeText(context, "Đã thêm vào mục yêu thích!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Bài hát đã tồn tại trong mục yêu thích!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void removeFavorite(Song song, Context context) {
        favoriteSongs.remove(song);
        saveFavorites(context); // Lưu lại danh sách sau khi xóa
        Toast.makeText(context, "Đã xóa khỏi mục yêu thích!", Toast.LENGTH_SHORT).show();
    }

    // Lưu danh sách yêu thích vào SharedPreferences
    public static void saveFavorites(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder stringBuilder = new StringBuilder();
        for (Song song : favoriteSongs) {
            stringBuilder.append(song.getImageResId()).append("|")
                    .append(song.getName()).append("|")
                    .append(song.getArtist()).append("|")
                    .append(song.getDuration()).append("|")
                    .append(song.getFilePath()).append(";");
        }

        editor.putString(KEY_FAVORITES, stringBuilder.toString());
        editor.apply();
    }

    public static boolean isFavorite(Song song, Context context) {
        loadFavorites(context); // Đảm bảo dữ liệu được cập nhật từ bộ nhớ
        return favoriteSongs.contains(song);
    }
}
