package com.example.nghenhac;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineMusicActivity extends AppCompatActivity {

    private static final String API_KEY = "30740f3cfaa2504aa4b332341c63e3b0";
    private static final String API_METHOD = "chart.gettoptracks";
    private static final String API_FORMAT = "json";

    private ListView listView;
    private OnlineSongAdapter adapter;
    private ArrayList<MusicResponse.Track> trackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music);

        // Khởi tạo danh sách bài hát và ListView
        listView = findViewById(R.id.listView);
        trackList = new ArrayList<>();

        // Tạo adapter
        adapter = new OnlineSongAdapter(this, trackList);
        listView.setAdapter(adapter);

        // Lấy danh sách bài hát từ API
        fetchSongs();
    }

    private void fetchSongs() {
        MusicApi apiService = ApiClient.getClient().create(MusicApi.class);
        Call<MusicResponse> call = apiService.getTracks(API_METHOD, API_KEY, API_FORMAT, 90);

        call.enqueue(new Callback<MusicResponse>() {
            @Override
            public void onResponse(Call<MusicResponse> call, Response<MusicResponse> response) {
                if (response.isSuccessful()) {
                    MusicResponse musicResponse = response.body();
                    if (musicResponse != null && musicResponse.tracks != null) {
                        List<MusicResponse.Track> tracks = musicResponse.tracks.track;
                        trackList.clear();  // Clear any previous data
                        trackList.addAll(tracks); // Add new data
                        adapter.notifyDataSetChanged(); // Cập nhật ListView
                    }
                } else {
                    Toast.makeText(OnlineMusicActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MusicResponse> call, Throwable t) {
                Toast.makeText(OnlineMusicActivity.this, "Không thể kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
