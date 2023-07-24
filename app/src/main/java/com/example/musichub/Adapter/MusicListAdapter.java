package com.example.musichub.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musichub.Activities.Media_Play;
import com.example.musichub.Fragments.HomeFrag;
import com.example.musichub.Models.MusicDetail;
import com.example.musichub.Models.MyMediaPlayer;
import com.example.musichub.Models.PlayerController;
import com.example.musichub.R;

import java.util.ArrayList;


public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private ArrayList<MusicDetail> musicDetails;

    private HomeFrag homeFrag;

    private MusicDetail detail;

    public MusicListAdapter(HomeFrag homeFrag, ArrayList<MusicDetail> musicDetail) {
        this.homeFrag = homeFrag;
        this.musicDetails = musicDetail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(MusicListAdapter.ViewHolder holder, int position) {
        if (musicDetails.get(position).getDuration() != null) {
            detail = musicDetails.get(position);
            holder.musicName.setText(detail.getName());
            holder.authorName.setText(detail.getArtist());
            byte[] image = getSongImage(detail.getPath());
            if (image != null) {
                Glide.with(homeFrag.getContext()).load(image).into(holder.musicIcon);
            } else {
                Glide.with(homeFrag.getContext()).load(R.drawable.app_icon).into(holder.musicIcon);
            }

        }
        holder.layout.setOnClickListener(view -> {
            PlayerController.position = position;
            Intent intent = new Intent(homeFrag.getContext(), Media_Play.class);
            homeFrag.startActivityForResult(intent, 200);
        });
    }

    @Override
    public int getItemCount() {
        if (musicDetails.size() != 0) {
            return musicDetails.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView musicName, authorName;
        ImageView musicIcon;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            musicName = (TextView) itemView.findViewById(R.id.musicName);
            authorName = (TextView) itemView.findViewById(R.id.author_name);
            layout = (ConstraintLayout) itemView.findViewById(R.id.selectMusic);
            musicIcon = (ImageView) itemView.findViewById(R.id.music_icon);
        }
    }

    private byte[] getSongImage(String uri) {
        byte[] art = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        art = retriever.getEmbeddedPicture();
        return art;
    }
}
