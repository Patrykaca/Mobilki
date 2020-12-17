package com.example.mobilki.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mobilki.R;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;

import java.util.List;

public class ShoppingListDetailedActivity extends AppCompatActivity {
    private TextView cityTextView;
    private TextView addressTextView;
    private TextView itemsTextView;
    private TextView shopTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_detailed);
        ShoppingList sh = null;
        if(getIntent().getExtras() != null){
            sh = (ShoppingList) getIntent().getExtras().getSerializable("sh");

            cityTextView = findViewById(R.id.cityTextView);
            addressTextView = findViewById(R.id.addressTextView);
            itemsTextView = findViewById(R.id.itemsTextView);
            shopTextView = findViewById(R.id.shopTextView);

            cityTextView.setText(sh.getCity().toString());
            addressTextView.setText(sh.getAddress().toString());
            shopTextView.setText(sh.getShop().toString());
            List<Item> items = sh.getItems();
            StringBuilder builder = new StringBuilder();
            for(Item i : items){
                builder.append(i.getName());
                builder.append(" ");
                builder.append(i.getAmount());
                builder.append(" ");
                builder.append(i.getMeasurement());
                builder.append("\n");
            }
            itemsTextView.setText(builder.toString());
        }



    }
}