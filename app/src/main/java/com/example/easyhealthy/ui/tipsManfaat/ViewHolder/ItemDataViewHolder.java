package com.example.easyhealthy.ui.home.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;

public class ItemDataViewHolder extends RecyclerView.ViewHolder {

    public TextView judul;
    public ImageView image;

    public ItemDataViewHolder(@NonNull View itemView) {
        super( itemView );
        judul = itemView.findViewById( R.id.tvTitle );
        image = itemView.findViewById( R.id.itemImage );
    }
}
