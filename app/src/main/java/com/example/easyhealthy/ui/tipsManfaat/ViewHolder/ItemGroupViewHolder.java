package com.example.easyhealthy.ui.tipsManfaat.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;

public class ItemGroupViewHolder extends RecyclerView.ViewHolder {

    public TextView headerTitle;
    public RecyclerView group_recyclerView;
    public RecyclerView.LayoutManager manager;

    public ItemGroupViewHolder(@NonNull View itemView) {
        super( itemView );

        manager = new LinearLayoutManager( itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        headerTitle = itemView.findViewById( R.id.itemTitle );
        group_recyclerView = itemView.findViewById( R.id.recycle_view_list_home );
        group_recyclerView.setLayoutManager( manager );
    }
}
