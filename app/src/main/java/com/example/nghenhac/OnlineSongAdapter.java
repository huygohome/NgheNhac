package com.example.nghenhac;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class OnlineSongAdapter extends BaseAdapter {
    private List<MusicResponse.Track> tracks;
    private Context context;

    public OnlineSongAdapter(Context context, List<MusicResponse.Track> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder pattern for performance
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

        // Get current track
        MusicResponse.Track track = tracks.get(position);

        // Set image, name, and artist
        holder.imgSong.setImageResource(R.drawable.lastfm_icon); // Set a placeholder image
        holder.tvName.setText(track.name);
        holder.tvDuration.setText("➥");
        holder.tvArtist.setText(track.artist != null ? track.artist.name : "Unknown Artist");

        // Set up click listener to open song URL in the browser
        convertView.setOnClickListener(v -> {
            if (track.url != null && !track.url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(track.url));
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgSong;
        TextView tvName, tvArtist,tvDuration;
    }
}