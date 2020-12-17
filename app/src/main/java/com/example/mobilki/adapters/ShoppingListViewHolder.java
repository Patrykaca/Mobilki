package com.example.mobilki.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.R;
import com.example.mobilki.activities.ShoppingListDetailedActivity;
import com.example.mobilki.classes.ShoppingList;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder{
    private TextView shop;
    private TextView itemAmount;
    private CardView cardView;
    private ShoppingList shL;
    View view;

    public ShoppingListViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        shop = itemView.findViewById(R.id.shopTextView);
        itemAmount = itemView.findViewById(R.id.itemsAmount);
        cardView = itemView.findViewById(R.id.shoppingListItem);
    }
    public void onBind(ShoppingList sh){
        shL=sh;
        shop.setText(sh.getShop().toString());
        itemAmount.setText(Integer.toString(sh.getItems().size()));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ShoppingListDetailedActivity.class);
                //intent.putExtra("sh",shL);
                view.getContext().startActivity(intent);
            }
        });
    }

}