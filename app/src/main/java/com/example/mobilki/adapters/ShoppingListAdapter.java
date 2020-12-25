package com.example.mobilki.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.R;
import com.example.mobilki.classes.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListViewHolder> {

    private LayoutInflater inflater;
    private List<ShoppingList> lists;
    //private List<ShoppingList> fulllists;

    public ShoppingListAdapter(Context context, List<ShoppingList> list) {
        this.inflater = LayoutInflater.from(context);
        this.lists = list;
        //this.fulllists = new ArrayList<>(list);
    }

    public void setLists(List<ShoppingList> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.shopping_list_item,parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        ShoppingList shoppingList = lists.get(position);
        holder.onBind(shoppingList);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }


    public void filterList(String query) {

    }
}
