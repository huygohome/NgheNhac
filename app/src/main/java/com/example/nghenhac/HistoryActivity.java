package com.example.nghenhac;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    Button back;
    private ListView recentSongsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        back = findViewById(R.id.back);
        recentSongsListView = findViewById(R.id.recentSongsListView);
        back.setOnClickListener(view -> finish());
        // Lấy danh sách các bài hát đã nghe
        List<Song> recentSongs = getRecentSongs();

        // Hiển thị bài hát trong ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formatSongList(recentSongs));
        recentSongsListView.setAdapter(adapter);
    }

    // Lấy danh sách các bài hát đã nghe từ SharedPreferences
    private List<Song> getRecentSongs() {
        SharedPreferences sharedPreferences = getSharedPreferences("History", MODE_PRIVATE);
        Map<String, ?> allSongs = sharedPreferences.getAll(); // Lấy tất cả bài hát

        List<Song> recentSongs = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allSongs.entrySet()) {
            String songName = entry.getKey();
            Object value = entry.getValue();

            // Kiểm tra kiểu dữ liệu và thêm vào danh sách
            if (value instanceof Long) {
                long timestamp = (Long) value;
                // Tạo một đối tượng Song, bạn có thể mở rộng để thêm các thông tin như nghệ sĩ, hình ảnh
                Song song = new Song(R.drawable.music_icon, songName, "Unknown Artist", formatDuration(timestamp), null);
                song.setLastPlayed(timestamp); // Nếu Song có thêm trường `lastPlayed`
                recentSongs.add(song);
            }
        }

        // Sắp xếp danh sách theo thời gian nghe (mới nhất lên đầu)
        Collections.sort(recentSongs, (s1, s2) -> Long.compare(s2.getLastPlayed(), s1.getLastPlayed()));

        return recentSongs;
    }

    // Định dạng danh sách Song thành chuỗi hiển thị
    private List<String> formatSongList(List<Song> songs) {
        List<String> formattedList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        for (Song song : songs) {
            String formattedTime = dateFormat.format(new Date(song.getLastPlayed()));
            formattedList.add(song.getName() + " (Last played: " + formattedTime + ")");
        }

        return formattedList;
    }

    // Định dạng thời gian thành chuỗi dễ đọc
    private String formatDuration(long durationInMs) {
        int minutes = (int) (durationInMs / 1000 / 60);
        int seconds = (int) (durationInMs / 1000 % 60);
        return String.format("%d:%02d", minutes, seconds);
    }
}
