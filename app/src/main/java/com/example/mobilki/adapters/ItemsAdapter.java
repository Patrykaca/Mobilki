package com.example.mobilki.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.R;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {

//    private List<Item> items;
//
//    public ItemsAdapter(List<Item> items) {
//        this.items = items;
//    }
//
//    @NonNull
//    @Override
//    public ItemOwnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.items_list_item, parent, false);
//
//        return new ItemOwnViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ItemOwnViewHolder holder, int position) {
//        Log.d("ViewHolder","Binding view holder");
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class ItemOwnViewHolder extends RecyclerView.ViewHolder {
//        public ItemOwnViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }

    //private LayoutInflater inflater;
    private List<Item> items;

    public final void removeItem(int position){
        items.remove(position);
        notifyDataSetChanged();

    }

    public ItemsAdapter(Context context, List<Item> i) {
        Log.d("AdapterLog", "Creating Adapter");
        //this.inflater = LayoutInflater.from(context);
        this.items = i;
        Log.d("AdapterLog", "Adapter created");
    }

    public void setLists(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("AdapterLog", "Inflating layout");
        //View view = inflater.inflate(R.layout.items_list_item, parent, false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_list_item, parent, false);
        Log.d("AdapterLog", "View holder will be created");
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        Log.d("AdapterLog", "View holder will be binded");
        Item item = items.get(position);
        holder.onBind(item,position);
        holder.view.findViewById(R.id.deleteItemButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
