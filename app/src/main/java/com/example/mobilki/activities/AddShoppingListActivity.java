package com.example.mobilki.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.mobilki.User;
import com.example.mobilki.adapters.ItemsAdapter;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//tak na prawde dodawanie ogloszenia lub edycja istniejacego ogloszenia
public class AddShoppingListActivity extends AppCompatActivity {

    private EditText shopEditText;
    private EditText cityEditText;
    private EditText addressEditText;
    private EditText itemEditText;
    private EditText quantityEditText;
    private Spinner mSpinner;
    private Button addItemBtn;
    private Button addShLBtn;
//    private TextView itemsTextView;
    private RecyclerView itemsRecyclerView;
    private ArrayAdapter<CharSequence> adapter;

    private String measure;
    String nameSurname;
    private ArrayList<Item> items;
    private ItemsAdapter recyclerAdapter;
    ShoppingList sh;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    DatabaseReference reference;


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
        nameSurname = "";
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Advertisements");
        databaseReference.keepSynced(true);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                nameSurname = user.getFirstName() + " " + user.getLastName();
                Log.d("NameSurname:", nameSurname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    if(getIntent().getExtras() == null){
                        DatabaseReference newPost = databaseReference.push();

                        ShoppingList shoppingList = new ShoppingList(newPost.getKey(),shop,items,address,city,firebaseUser.getUid(),nameSurname,"posted","");
                        newPost.setValue(shoppingList).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Shopping list posted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),ShoppingListActivity.class));
                                finish();
                            }
                        });
                    }else{

                        HashMap hashMap = new HashMap();
                        hashMap.put("city",city);
                        hashMap.put("address",address);
                        hashMap.put("items",items);
                        hashMap.put("shop",shop);
                        databaseReference.child(sh.getId()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(getApplicationContext(), "Shopping list updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MyShLActivity.class));
                                finish();
                            }
                        });
                    }

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
                    recyclerAdapter.notifyDataSetChanged();
                    //redraw();
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
//        itemsTextView = findViewById(R.id.addedItemsTextView);
        addItemBtn = findViewById(R.id.addItemButton);
        addShLBtn = findViewById(R.id.addShLButton);
        itemsRecyclerView = findViewById(R.id.itemsList);

        adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.measurements, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        recyclerAdapter = new ItemsAdapter(this,items);
        //recyclerAdapter = new ItemsAdapter(items);
        itemsRecyclerView.setAdapter(recyclerAdapter);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //jesli to jednak widok edycji ....
        if(getIntent().getExtras() != null){
            sh = (ShoppingList) getIntent().getExtras().getSerializable("sh");
            Toast.makeText(this,sh.getAddress(),Toast.LENGTH_SHORT).show();
            addressEditText.setText(sh.getAddress());
            cityEditText.setText(sh.getCity());
            shopEditText.setText(sh.getShop());
            items.addAll(sh.getItems());
        }
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
        //itemsTextView.setText(builder.toString());
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