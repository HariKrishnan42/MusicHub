package com.example.musichub.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musichub.Activities.Landing;
import com.example.musichub.R;

import java.util.ArrayList;

public class MinimizeAdapter extends RecyclerView.Adapter<MinimizeAdapter.ViewHolder> {
    private Landing landing;
    private ArrayList<String> name;

    public MinimizeAdapter(Landing landing, ArrayList<String> name) {
        this.landing = landing;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_player_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MinimizeAdapter.ViewHolder holder, int position) {
        holder.t1.setText(name.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView t1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.songTitle);
        }
    }
}
