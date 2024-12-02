package com.example.nghenhac;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity {

    private ImageView imgCover;
    private TextView tvSongName, tvArtistName, tvCurrentTime, tvTotalTime;
    private ImageButton btnPlayPause, btnPrevious, btnNext;
    private SeekBar seekBar;

    private MediaPlayer mediaPlayer;
    private Song currentSong;

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // Ánh xạ giao diện
        imgCover = findViewById(R.id.imgCover);
        tvSongName = findViewById(R.id.tvSongName);
        tvArtistName = findViewById(R.id.tvArtistName);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        seekBar = findViewById(R.id.seekBar);

        // Nhận dữ liệu từ MainActivity
        currentSong = (Song) getIntent().getSerializableExtra("song");
        if (currentSong != null) {
            setupSongInfo();
        }

        // Tạo MediaPlayer
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(currentSong.getFilePath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cập nhật thời gian tổng bài hát
        tvTotalTime.setText(formatDuration(mediaPlayer.getDuration()));

        // Sự kiện nút Play/Pause
        btnPlayPause.setOnClickListener(v -> {
            if (isPlaying) {
                pauseMusic();
            } else {
                playMusic();
            }
        });

        // Xử lý thanh SeekBar
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Cập nhật tiến trình SeekBar
        mediaPlayer.setOnCompletionListener(mp -> {
            pauseMusic();
            mediaPlayer.seekTo(0);
        });

        new Thread(() -> {
            while (mediaPlayer != null) {
                try {
                    if (mediaPlayer.isPlaying()) {
                        runOnUiThread(() -> {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            tvCurrentTime.setText(formatDuration(mediaPlayer.getCurrentPosition()));
                        });
                    }
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setupSongInfo() {
        imgCover.setImageResource(R.drawable.music);
        tvSongName.setText(currentSong.getName());
        tvArtistName.setText(currentSong.getArtist());
    }

    private void playMusic() {
        mediaPlayer.start();
        isPlaying = true;
        btnPlayPause.setImageResource(R.drawable.pause); // Đổi icon sang pause
    }

    private void pauseMusic() {
        mediaPlayer.pause();
        isPlaying = false;
        btnPlayPause.setImageResource(R.drawable.play); // Đổi icon sang play
    }

    private String formatDuration(long durationInMs) {
        int minutes = (int) (durationInMs / 1000 / 60);
        int seconds = (int) (durationInMs / 1000 % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}