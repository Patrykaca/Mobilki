package com.example.mobilki.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mobilki.R;
import com.example.mobilki.classes.Item;

import java.util.ArrayList;
import java.util.List;

public class AddShoppingListActivity extends AppCompatActivity {

    private EditText shopEditText;
    private EditText cityEditText;
    private EditText addressEditText;
    private EditText itemEditText;
    private EditText quantityEditText;
    private Spinner mSpinner;
    private Button addItemBtn;
    private Button addShLBtn;
    private TextView itemsTextView;

    private String measure;

    private List<Item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        items = new ArrayList<>();

        mSpinner = findViewById(R.id.measurementSpinner);

        shopEditText = findViewById(R.id.shopEditText);
        cityEditText = findViewById(R.id.cityEditText);
        addressEditText = findViewById(R.id.addressEditText);
        itemEditText = findViewById(R.id.itemNameEditText);
        quantityEditText = findViewById(R.id.itemQuantityEditText);

        itemsTextView = findViewById(R.id.addedItemsTextView);

        addItemBtn = findViewById(R.id.addItemButton);
        addShLBtn = findViewById(R.id.addShLButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.measurements, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                measure = parent.getItemAtPosition(position).toString();
                Log.d("Measure:",measure);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = itemEditText.getText().toString();
                float itemQuantity = Float.parseFloat(quantityEditText.getText().toString());
                Item item = new Item(itemName,itemQuantity,measure);
                items.add(item);
                itemEditText.setText("");
                quantityEditText.setText("");
                redraw();
                Log.d("AddLog","Item added");
            }
        });
        addShLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void redraw() {
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