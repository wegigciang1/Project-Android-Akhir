package com.example.easyhealthy.ui.dashboard.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;

public class DataBeratViewHolder extends RecyclerView.ViewHolder {
    public TextView berat;
    public TextView tanggal;

    public DataBeratViewHolder(@NonNull View itemView) {
        super(itemView);
        berat = itemView.findViewById(R.id.tanggalInputRecycle);
        tanggal = itemView.findViewById(R.id.beratInputRecycle);
    }
}
