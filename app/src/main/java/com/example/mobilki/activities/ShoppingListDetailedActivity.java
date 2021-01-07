package com.example.mobilki.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilki.R;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ShoppingListDetailedActivity extends AppCompatActivity {
    private TextView cityTextView;
    private TextView addressTextView;
    private TextView itemsTextView;
    private TextView shopTextView;

    private ShoppingList sh;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_detailed);

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

        initFirebaseConnection();

    }

    private void initFirebaseConnection() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Advertisements").child(sh.getId());
        databaseReference.keepSynced(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailed_menu,menu);
        if(getIntent().getExtras()!=null){
            if(getIntent().getExtras().getBoolean("edit")){
                menu.getItem(2).setVisible(false);
            }
            else{
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteOption:{
                databaseReference.removeValue();
                Toast.makeText(getApplicationContext(), "Delete pressed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),ShoppingListActivity.class));
                finish();
                break;
            }

            case R.id.editOption:{
                Toast.makeText(getApplicationContext(), "Edit pressed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AddShoppingListActivity.class);
                intent.putExtra("sh",sh);
                startActivity(intent);
                break;
            }
            case R.id.respondOption:{
                //TODO tworzenie nowego czatu oraz przypisanie do tej listy zakupow id osoby ktora ja realizuje
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}