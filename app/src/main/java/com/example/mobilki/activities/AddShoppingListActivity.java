package com.example.mobilki.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilki.R;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private ArrayAdapter<CharSequence> adapter;

    private String measure;
    private ArrayList<Item> items;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        items = new ArrayList<>();

        initFirebaseConnection();

        //inicjalizacja widoku
        initViews();

        //obsluga zdarzenia wyboru jednostki miary
        initSpinnerListener();

        //tworzenie pozycji zakupowej
        initAddItemButtonListener();

        //tworzenie ogloszenia
        initAddShLButtonListener();


    }

    private void initFirebaseConnection() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Advertisements");
        databaseReference.keepSynced(true);
    }

    private void initAddShLButtonListener() {
        //tworzenie ogloszenia
        addShLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkShLValues()){
                    String shop = shopEditText.getText().toString().trim();
                    String city = cityEditText.getText().toString().trim();
                    String address = addressEditText.getText().toString().trim();

                    if(shopEditText.getText().toString().isEmpty())
                        shop = "Any";
                    DatabaseReference newPost = databaseReference.push();
                    ShoppingList shoppingList = new ShoppingList(newPost.getKey(),shop,items,address,city,firebaseUser.getUid());
                    newPost.setValue(shoppingList).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Shopping list posted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ShoppingListActivity.class));
                            finish();
                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initAddItemButtonListener() {
        //tworzenie pozycji zakupowej
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkItemValues()){
                    String itemName = itemEditText.getText().toString();
                    float itemQuantity = Float.parseFloat(quantityEditText.getText().toString());
                    Item item = new Item(itemName,itemQuantity,measure);
                    items.add(item);
                    itemEditText.setText("");
                    quantityEditText.setText("");
                    redraw();
                    Log.d("AddLog","Item added");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initSpinnerListener() {
        //obsluga zdarzenia wyboru jednostki miary
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
    }

    //inicjalizacja wszystkich view w naszym activity
    private void initViews() {
        mSpinner = findViewById(R.id.measurementSpinner);
        shopEditText = findViewById(R.id.shopEditText);
        cityEditText = findViewById(R.id.cityEditText);
        addressEditText = findViewById(R.id.addressEditText);
        itemEditText = findViewById(R.id.itemNameEditText);
        quantityEditText = findViewById(R.id.itemQuantityEditText);
        itemsTextView = findViewById(R.id.addedItemsTextView);
        addItemBtn = findViewById(R.id.addItemButton);
        addShLBtn = findViewById(R.id.addShLButton);
        adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.measurements, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    //sprawdzenie czy podano poprawne wartosci do utworzenia ogloszenia
    private boolean checkShLValues() {
        return !cityEditText.getText().toString().isEmpty()
                && !addressEditText.getText().toString().isEmpty()
                && !items.isEmpty();
    }

    //sprawdzenie czy podano poprawne wartosci do utworzenia pozycji listy zakupowej
    private boolean checkItemValues() {
        return !itemEditText.getText().toString().isEmpty()
                && isNumeric(quantityEditText.getText().toString());

    }

    //odswiezanie widoku pozycji zakupowych
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

    //sprawdzenie czy lancuch znakow jest liczba
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}