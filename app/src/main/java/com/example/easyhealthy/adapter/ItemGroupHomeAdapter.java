package com.example.easyhealthy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;
import com.example.easyhealthy.ui.tipsManfaat.Model.ItemData;
import com.example.easyhealthy.ui.tipsManfaat.Model.ItemGroup;

import java.util.List;

public class ItemGroupHomeAdapter extends RecyclerView.Adapter<ItemGroupHomeAdapter.MyviewHolder> {

    private Context context;
    private List<ItemGroup> datalist;

    public ItemGroupHomeAdapter(Context context, List<ItemGroup> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from( context ).inflate( R.layout.layout_group_home,parent,false );
        return new MyviewHolder( itemview );
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.item_title.setText( datalist.get( position ).getHeaderTitle() );

        List<ItemData>  itemData = datalist.get( position ).getListResep();

        ItemHomeAdapter itemListAdapter = new ItemHomeAdapter( context,itemData );
        holder.recyclerView_item_list.setHasFixedSize( true );
        holder.recyclerView_item_list.setLayoutManager( new LinearLayoutManager( context,LinearLayoutManager.HORIZONTAL,false ) );
        holder.recyclerView_item_list.setAdapter( itemListAdapter );

        holder.recyclerView_item_list.setNestedScrollingEnabled( false );


        //button more
        holder.btn_more.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( context,"MORE", Toast.LENGTH_SHORT ).show();
            }
        } );

    }

    @Override
    public int getItemCount() {
        return (datalist != null ? datalist.size() : 0);
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView item_title;
        RecyclerView recyclerView_item_list;
        Button btn_more;

        public MyviewHolder(@NonNull View itemView) {
            super( itemView );
            item_title = itemView.findViewById(R.id.itemTitle);
            btn_more = itemView.findViewById(R.id.btnMoreHome);
            recyclerView_item_list = itemView.findViewById(R.id.recycle_view_home);
        }
    }


}
