package com.example.nghenhac;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    Button favoriteSongsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        favoriteSongsButton = findViewById(R.id.favoriteSongsButton);
        if (checkPermission()) {
            loadMusicFromDevice();
        } else {
            requestPermission();
        }
        favoriteSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListActivity.this, FavoriteSongActivity.class);
                startActivity(intent1);
            }
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
        // Gọi hàm quét nhạc
        ListNhac.loadMusicFromDevice(this);

        // Hiển thị danh sách nhạc trong RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SongAdapter adapter = new SongAdapter(this, ListNhac.getSongs(),
                song -> {
                    // Xử lý khi click bài nhạc
                    Intent intent = new Intent(ListActivity.this, MusicPlayerActivity.class);
                    intent.putExtra("song", song);
                    startActivity(intent);
                },
                song -> {
                    // Xử lý khi giữ lâu bài nhạc
                    FavoriteSongs.addFavorite(song,this); // Thêm bài hát vào danh sách yêu thích
                });

        recyclerView.setAdapter(adapter);
    }
}