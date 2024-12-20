package com.example.nghenhac;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // URL gốc của API bạn muốn sử dụng
    private static final String BASE_URL = "https://ws.audioscrobbler.com/";
    private static Retrofit retrofit = null;

    // Phương thức trả về một Retrofit instance
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Đường dẫn API gốc
                    .addConverterFactory(GsonConverterFactory.create()) // Chuyển đổi JSON sang Java Object
                    .build();
        }
        return retrofit;
    }
}
