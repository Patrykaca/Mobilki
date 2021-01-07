package com.example.mobilki.adapters;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.R;
import com.example.mobilki.classes.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    private TextView itemName;
    private TextView itemQuantity;
    private TextView itemMeasure;
    private Button deleteButton;

    View view;
    Item item;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        itemName = view.findViewById(R.id.itemName);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemMeasure = view.findViewById(R.id.itemMeasure);

        deleteButton = view.findViewById(R.id.deleteItemButton);
        Log.d("ItemViewHolder","Item view holder created");
    }

    public void onBind(Item item, int position){
        this.item = item;
        itemName.setText(item.getName());
        itemQuantity.setText(Float.toString(item.getAmount()));
        itemMeasure.setText(item.getMeasurement());
        Log.d("ItemViewHolder","View binded");

    }
}
