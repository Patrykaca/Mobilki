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
    private TextView nameSurnameTextView;
    private TextView quantityTextView;
    private TextView addressTextView;
    private CardView cardView;
    private ShoppingList shL;
    View view;

    public ShoppingListViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        nameSurnameTextView = itemView.findViewById(R.id.nameSurnameView);
        quantityTextView = itemView.findViewById(R.id.quantityTextView);
        addressTextView = itemView.findViewById(R.id.addressTextView);
        cardView = itemView.findViewById(R.id.shoppingListItem);
    }
    public void onBind(ShoppingList sh){
        shL=sh;
        nameSurnameTextView.setText(sh.getNameSurname().toString());
        quantityTextView.setText(Integer.toString(sh.getItems().size()));
        addressTextView.setText(sh.getAddress());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), ShoppingListDetailedActivity.class);
                    intent.putExtra("sh",shL);
                    intent.putExtra("edit",false);
                    intent.putExtra("active",false);
                    view.getContext().startActivity(intent);

            }
        });
    }

}