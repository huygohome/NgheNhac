package com.example.nghenhac;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FavoriteSongActivity extends AppCompatActivity {
    SongAdapter adapter;
    Button back;
    ListView listViewFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_song);

        back = findViewById(R.id.back);
        listViewFavorites = findViewById(R.id.listViewFavorites);

        back.setOnClickListener(view -> finish());

        // Hiển thị danh sách yêu thích
        adapter = new SongAdapter(this, FavoriteSongs.getFavoriteSongs(),
                (song,position) -> {
                    // Xử lý khi click bài nhạc
                    Intent intent = new Intent(FavoriteSongActivity.this, MusicPlayerActivity.class);
                    intent.putExtra("song", song);
                    startActivity(intent);
                },
                song -> {
                    removeFromFavorites(song);
                });

        listViewFavorites.setAdapter(adapter);
    }

    private void removeFromFavorites(Song song) {
        // Xóa bài hát khỏi danh sách yêu thích
        FavoriteSongs.removeFavorite(song);

        // Hiển thị thông báo
        Toast.makeText(this, "Đã xóa khỏi mục yêu thích!", Toast.LENGTH_SHORT).show();

        // Cập nhật lại danh sách
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged(); // Cập nhật lại adapter
    }
}
