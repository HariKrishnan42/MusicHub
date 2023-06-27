package com.example.musichub.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.musichub.Activities.Media_Play;
import com.example.musichub.Models.MusicDetail;
import com.example.musichub.Models.MyMediaPlayer;
import com.example.musichub.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MusicDetail> musicDetails;

    private MusicDetail detail;

    public MusicListAdapter(Context context, ArrayList<MusicDetail> musicDetail) {
        this.context = context;
        this.musicDetails = musicDetail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(MusicListAdapter.ViewHolder holder, int position) {
        if (musicDetails.get(position).getDuration() != null) {
            detail = musicDetails.get(position);
            holder.musicName.setText(detail.getName());
            holder.authorName.setText(detail.getArtist());
            Bitmap image = getSongImage(detail.getPath());
            if (image != null) {
//                Glide.with(context)
//                        .asBitmap()
//                        .load(image)
//                        .apply(new RequestOptions().override(500, 500)) // Optional: Set desired image size
//                        .into(new CustomTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                                holder.musicIcon.setImageBitmap(resource);
//                            }
//
//                            @Override
//                            public void onLoadCleared(Drawable placeholder) {
//
//                            }
//                        });
            }

        }
        holder.layout.setOnClickListener(view -> {
            MyMediaPlayer.players().reset();
            MyMediaPlayer.currentSong = position;
            Intent intent = new Intent(context, Media_Play.class);
            intent.putExtra("bundle", musicDetails);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
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

    private Bitmap getSongImage(String uri) {
        byte[] art = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        art = retriever.getEmbeddedPicture();
        if (art != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
            return bitmap;
        }
        return null;
    }
}
