package com.example.nghenhac;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteSongActivity extends AppCompatActivity {
    SongAdapter adapter;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_song);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Hiển thị danh sách yêu thích
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SongAdapter(this, FavoriteSongs.getFavoriteSongs(),
                song -> {
                    // Xử lý khi click bài nhạc
                    Intent intent = new Intent(FavoriteSongActivity.this, MusicPlayerActivity.class);
                    intent.putExtra("song", song);
                    startActivity(intent);
                },
                song -> {
                    removeFromFavorites(song);
                });

        recyclerView.setAdapter(adapter);
    }
    private void removeFromFavorites(Song song) {
        // Xóa bài hát khỏi danh sách yêu thích
        FavoriteSongs.removeFavorite(song);

        // Hiển thị thông báo
        Toast.makeText(this, "Đã xóa khỏi mục yêu thích!", Toast.LENGTH_SHORT).show();

        // Cập nhật lại danh sách
        adapter.notifyDataSetChanged();
    }
}

