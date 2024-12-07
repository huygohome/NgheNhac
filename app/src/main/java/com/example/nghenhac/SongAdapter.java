package com.example.nghenhac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends BaseAdapter {
    private List<Song> songs;
    private Context context;
    private OnSongClickListener listener;
    private OnSongLongClickListener longClickListener; // Giữ lâu

    // Giao diện để lắng nghe sự kiện click
    public interface OnSongClickListener {
        void onSongClick(Song song, int position);
    }

    // Giao diện để lắng nghe sự kiện giữ lâu
    public interface OnSongLongClickListener {
        void onSongLongClick(Song song);
    }

    public SongAdapter(Context context, List<Song> songs, OnSongClickListener listener, OnSongLongClickListener longClickListener) {
        this.context = context;
        this.songs = songs;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false);
            holder = new ViewHolder();
            holder.imgSong = convertView.findViewById(R.id.imgSong);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvArtist = convertView.findViewById(R.id.tvArtist);
            holder.tvDuration = convertView.findViewById(R.id.tvDuration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Song song = songs.get(position);

        holder.imgSong.setImageResource(song.getImageResId());
        holder.tvName.setText(song.getName());
        holder.tvArtist.setText(song.getArtist());
        holder.tvDuration.setText(song.getDuration());

        // Xử lý sự kiện click
        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSongClick(song, position);
            }
        });

        // Xử lý sự kiện giữ lâu
        convertView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onSongLongClick(song);
            }
            return true;
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgSong;
        TextView tvName, tvArtist, tvDuration;
    }
}
