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

public class MyShoppingListViewHolder extends RecyclerView.ViewHolder {
    View view;
    private TextView statusTextView,
                    addressTextView,
                    quantityTextView;
    CardView cardView;
    public MyShoppingListViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        statusTextView = view.findViewById(R.id.statusTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        quantityTextView = view.findViewById(R.id.quantityTextView);
        cardView = view.findViewById(R.id.my_sh_l_item_card);
    }

    public void onBind(final ShoppingList shoppingList) {
        statusTextView.setText(shoppingList.getStatus());
        addressTextView.setText(shoppingList.getAddress());
        quantityTextView.setText(Integer.toString(shoppingList.getItems().size()));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ShoppingListDetailedActivity.class);
                intent.putExtra("sh", shoppingList);
                intent.putExtra("edit",true);
                view.getContext().startActivity(intent);
            }
        });
    }
}