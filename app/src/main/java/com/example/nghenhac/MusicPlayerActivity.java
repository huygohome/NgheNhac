package com.example.nghenhac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class MusicPlayerActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 123;
    private ImageButton btnMode, btnPlayPause, btnNext, btnPrev, btnFavorite;
    private SeekBar seekBar;
    private TextView tvCurrentTime, tvDuration, tvSongTitle;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    private SharedPreferences sharedPreferences;
    private int loopMode = 0; // 0 - Lặp danh sách, 1 - Lặp bài hiện tại, 2 - Phát ngẫu nhiên

    private List<Song> songList = new ArrayList<>(); // Danh sách bài hát
    private int currentSongIndex = 0;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // Initialize UI components
        btnMode = findViewById(R.id.btnMode);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        seekBar = findViewById(R.id.seekBar);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvDuration = findViewById(R.id.tvDuration);
        tvSongTitle = findViewById(R.id.tvSongTitle);
        btnFavorite = findViewById(R.id.btnFavorite);

        // Gọi hàm quét nhạc
        while (true) {
            if (checkPermission()) {
                ListNhac.loadMusicFromDevice(this);
                break;
            } else {
                requestPermission();
            }
        }


        // Tạo danh sách bài hát
        songList = ListNhac.getSongs();

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("MusicAppPrefs", MODE_PRIVATE);

        // Lấy chế độ lặp từ SharedPreferences
        loopMode = sharedPreferences.getInt("loopMode", 0); // Mặc định là 0 (Lặp danh sách)
        updateLoopButton();
        // Nhận currentSongIndex từ Intent
        currentSongIndex = getIntent().getIntExtra("currentSongIndex", 0);
        // Load trạng thái yêu thích
        Intent intent = getIntent();
        boolean isFavoriteMode = intent.getBooleanExtra("isFavoriteMode", false);
        if (isFavoriteMode) {
            songList = (List<Song>) intent.getSerializableExtra("favoriteSongs"); // Lấy danh sách yêu thích
            currentSongIndex = intent.getIntExtra("currentSongIndex", 0); // Vị trí trong danh sách yêu thích
        } else {
            songList = ListNhac.getSongs(); // Danh sách chính
            currentSongIndex = getIntent().getIntExtra("currentSongIndex", 0);
        }
        // Gọi playSong() để phát nhạc
        playSong();

        // Play/Pause button
        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.pause);
                updateSeekBar();
            }
        });

        // Next button
        btnNext.setOnClickListener(v -> {
            currentSongIndex = (currentSongIndex + 1) % songList.size();
            playSong();
        });

        // Previous button
        btnPrev.setOnClickListener(v -> {
            currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
            playSong();
        });

        // SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                tvCurrentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnMode.setOnClickListener(v -> {
            loopMode = (loopMode + 1) % 3; // Chuyển chế độ
            updateLoopButton();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("loopMode", loopMode);
            editor.apply();
        });

        btnFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            saveFavoriteState(currentSongIndex, isFavorite);
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

    private void playSong() {
        if (songList.isEmpty()) {
            // Hiển thị thông báo lỗi hoặc xử lý tình huống danh sách rỗng
            tvSongTitle.setText("No songs available");
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        Song currentSong = songList.get(currentSongIndex);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(currentSong.getFilePath())); // Thay filePath bằng Uri
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        tvSongTitle.setText(currentSong.getName() + "\n" + currentSong.getArtist());
        updateFavoriteButton(isFavorite);

        mediaPlayer.setOnPreparedListener(mp -> {
            tvDuration.setText(formatTime(mediaPlayer.getDuration()));
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            btnPlayPause.setImageResource(R.drawable.pause);
            updateSeekBar();
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            if (loopMode == 0) { // Lặp danh sách
                currentSongIndex = (currentSongIndex + 1) % songList.size();
                playSong();
            } else if (loopMode == 1) { // Lặp bài hiện tại
                playSong();
            } else if (loopMode == 2) { // Phát ngẫu nhiên
                int previousIndex = currentSongIndex;
                do {
                    currentSongIndex = (int) (Math.random() * songList.size());
                } while (currentSongIndex == previousIndex);
                playSong();
            }
        });
    }

    private void updateLoopButton() {
        if (loopMode == 0) {
            btnMode.setImageResource(R.drawable.loop);
        } else if (loopMode == 1) {
            btnMode.setImageResource(R.drawable.repeat);
        } else if (loopMode == 2) {
            btnMode.setImageResource(R.drawable.random);
        }
    }

    private void updateFavoriteButton(boolean isFavorite) {
        btnFavorite.setImageResource(R.drawable.heart_filled512x512);
    }

    private void saveFavoriteState(int songIndex, boolean isFavorite) {
        Song currentSong = songList.get(songIndex);
        if (isFavorite) {
            FavoriteSongs.addFavorite(currentSong, this); // Thêm vào danh sách yêu thích
        } else {
            FavoriteSongs.removeFavorite(currentSong, this); // Xóa khỏi danh sách yêu thích
            Toast.makeText(this, "Đã xóa khỏi mục yêu thích!", Toast.LENGTH_SHORT).show();
        }
    }



    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()) {
            handler.postDelayed(this::updateSeekBar, 500);
        }
    }

    private String formatTime(int ms) {
        int minutes = ms / 1000 / 60;
        int seconds = (ms / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

}