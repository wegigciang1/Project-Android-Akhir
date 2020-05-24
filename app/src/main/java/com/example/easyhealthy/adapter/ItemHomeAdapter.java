package com.example.easyhealthy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyhealthy.R;
import com.example.easyhealthy.interfaces.ItemClickListenerInterface;
import com.example.easyhealthy.ui.home.Model.ItemData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemHomeAdapter extends RecyclerView.Adapter<ItemHomeAdapter.MyViewHolder> {


    private Context context;
    private List<ItemData> itemDataList;

    public ItemHomeAdapter(Context context, List<ItemData> itemDataList) {
        this.context = context;
        this.itemDataList = itemDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from( context ).inflate( R.layout.layout_item_home,parent,false );
        return new MyViewHolder( itemview );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_item_title.setText( itemDataList.get( position ).getJudul() );
        Picasso.get().load( itemDataList.get( position ).getImage()).into( holder.img_item );

        //implements item click
        holder.setItemClickListener( new ItemClickListenerInterface() {
            @Override
            public void onItemClickListener(View view, int position) {
                Toast.makeText( context, ""+itemDataList.get( position ).getJudul(),Toast.LENGTH_SHORT).show();
            }
        } );
    }

    @Override
    public int getItemCount() {
        return (itemDataList != null ? itemDataList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_item_title;
        ImageView img_item;

        ItemClickListenerInterface itemClickListener;

        public void setItemClickListener (ItemClickListenerInterface itemClickListener){
            this.itemClickListener=itemClickListener;
        }


        public MyViewHolder(@NonNull View itemView) {
            super( itemView );
            txt_item_title = (TextView) itemView.findViewById( R.id.tvTitle );
            img_item = (ImageView) itemView.findViewById( R.id.itemImage );

            itemView.setOnClickListener( this );
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onItemClickListener( v, getAdapterPosition() );
        }
    }


}
