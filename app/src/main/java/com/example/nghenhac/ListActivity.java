package com.example.nghenhac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    Button favoriteSongsButton, recentSongsButton;
    ListView listView;
    EditText searchBar;

    SongAdapter adapter;
    List<Song> allSongs = new ArrayList<>(); // Danh sách tất cả bài hát
    List<Song> filteredSongs = new ArrayList<>(); // Danh sách bài hát đã lọc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        // Tải danh sách yêu thích
        FavoriteSongs.loadFavorites(this);
        searchBar = findViewById(R.id.searchBar);
        favoriteSongsButton = findViewById(R.id.favoriteSongsButton);
        listView = findViewById(R.id.listView);
        recentSongsButton = findViewById(R.id.recentSongsButton);

        if (checkPermission()) {
            loadMusicFromDevice();
        } else {
            requestPermission();
        }

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Lọc bài hát khi người dùng nhập từ khóa tìm kiếm
                String query = editable.toString().toLowerCase();
                filterSongs(query);
            }
        });

        favoriteSongsButton.setOnClickListener(view -> {
            Intent intent = new Intent(ListActivity.this, FavoriteSongActivity.class);
            startActivity(intent);
        });

        recentSongsButton.setOnClickListener(v -> {
            // Mở HistoryActivity để hiển thị bài hát gần đây
            Intent intent = new Intent(ListActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Không cần yêu cầu quyền cho các phiên bản trước Android 6.0
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
    }

    private void loadMusicFromDevice() {
        // Gọi hàm quét nhạc để tải danh sách nhạc vào ListNhac
        ListNhac.loadMusicFromDevice(this);

        // Lấy danh sách nhạc từ ListNhac
        allSongs.clear(); // Xóa dữ liệu cũ (nếu có)
        allSongs.addAll(ListNhac.getSongs()); // Lấy danh sách nhạc từ ListNhac

        // Ban đầu hiển thị toàn bộ danh sách bài hát
        filteredSongs.clear();
        filteredSongs.addAll(allSongs);

        // Tạo adapter với danh sách `filteredSongs`
        adapter = new SongAdapter(this, filteredSongs,
                (song,position) -> {
                    // Xử lý khi click bài nhạc
                    // Tìm index thực trong danh sách allSongs
                    int realIndex = allSongs.indexOf(song);
                    if (realIndex != -1) {
                        addSongToHistory(song.getName()); // Thêm vào lịch sử
                        Intent intent = new Intent(ListActivity.this, MusicPlayerActivity.class);
                        intent.putExtra("currentSongIndex", realIndex); // Truyền index thực
                        startActivity(intent);
                    }
                },
                song -> {
                    // Xử lý khi giữ lâu bài nhạc
                    FavoriteSongs.addFavorite(song, this); // Thêm bài hát vào danh sách yêu thích
                });

        listView.setAdapter(adapter);
    }

    // Hàm lọc danh sách bài hát theo tên bài hát hoặc tên ca sĩ
    private void filterSongs(String query) {
        filteredSongs.clear();
        for (Song song : allSongs) {
            // Kiểm tra xem tên bài hát hoặc ca sĩ có chứa từ khóa tìm kiếm hay không
            if (song.getName().toLowerCase().contains(query) || song.getArtist().toLowerCase().contains(query)) {
                filteredSongs.add(song);
            }
        }
        // Cập nhật ListView với danh sách đã lọc
        adapter.notifyDataSetChanged();
    }

    // Hàm lưu bài hát vào SharedPreferences
    private void addSongToHistory(String songName) {
        SharedPreferences sharedPreferences = getSharedPreferences("History", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();

        // Lưu bài hát và thời gian vào SharedPreferences
        editor.putLong(songName, currentTime);
        editor.apply();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Lưu danh sách yêu thích
        FavoriteSongs.saveFavorites(this);
    }
}
