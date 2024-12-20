package com.example.nghenhac;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicApi {
    @GET("2.0/") // Đường dẫn endpoint của API
    Call<MusicResponse> getTracks(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("limit") int limit
    );
}
