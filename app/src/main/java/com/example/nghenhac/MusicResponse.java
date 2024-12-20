package com.example.nghenhac;

import java.util.List;

public class MusicResponse {
    public Tracks tracks;

    public static class Tracks {
        public List<Track> track;
    }

    public static class Track {
        public String name;
        public Artist artist; // Thay đổi từ String thành đối tượng Artist
        public String duration;
        public String url; // URL phát nhạc


        public static class Artist {
            public String name; // Lấy tên nghệ sĩ từ đối tượng artist
             // Nếu cần lấy URL của nghệ sĩa
        }


    }
}


