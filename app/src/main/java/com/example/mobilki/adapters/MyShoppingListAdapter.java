package com.example.mobilki.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.R;
import com.example.mobilki.classes.ShoppingList;

import java.util.List;

public class MyShoppingListAdapter extends RecyclerView.Adapter<MyShoppingListViewHolder> {

    private LayoutInflater inflater;
    private List<ShoppingList> lists;

    public MyShoppingListAdapter(Context context, List<ShoppingList> lists) {
        this.lists = lists;
        this.inflater = LayoutInflater.from(context);
    }

    public void setLists(List<ShoppingList> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public MyShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_sh_l_item,parent,false);
        return new MyShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyShoppingListViewHolder holder, int position) {
        holder.onBind(lists.get(position));

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
