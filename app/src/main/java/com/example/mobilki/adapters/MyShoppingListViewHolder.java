package com.example.mobilki.adapters;

import android.content.Intent;
import android.graphics.Color;
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
    public void setStatusTextColor(ShoppingList sh){
        if(sh.getStatus().equals("posted")){
            statusTextView.setTextColor(Color.parseColor("#9fdfbb"));
        }
        else if(sh.getStatus().equals("accepted")){
            statusTextView.setTextColor(Color.parseColor("#66cc92"));
        }else if(sh.getStatus().equals("bought")){
            statusTextView.setTextColor(Color.parseColor("#267347"));
        }else if(sh.getStatus().equals("delivered")){
            statusTextView.setTextColor(Color.parseColor("#133924"));
        }
    }

    public void onBind(final ShoppingList shoppingList, final boolean active) {
        statusTextView.setText(shoppingList.getStatus());
        setStatusTextColor(shoppingList);
        addressTextView.setText(shoppingList.getAddress());
        quantityTextView.setText(Integer.toString(shoppingList.getItems().size()));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ShoppingListDetailedActivity.class);
                intent.putExtra("sh", shoppingList);
                if(active){
                    intent.putExtra("edit",false);
                }else{
                    intent.putExtra("edit",true);
                }
                intent.putExtra("active", active);
                view.getContext().startActivity(intent);
            }
        });
    }
}
