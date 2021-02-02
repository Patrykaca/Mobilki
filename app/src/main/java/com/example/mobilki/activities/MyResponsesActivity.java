package com.example.mobilki.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilki.R;
import com.example.mobilki.adapters.MyShoppingListAdapter;
import com.example.mobilki.classes.ShoppingList;
import com.example.mobilki.notifications.MyResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MyResponsesActivity extends AppCompatActivity {

    private ArrayList<ShoppingList> lists = new ArrayList<>();

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private TextView textView;

    private RecyclerView responsesRecyclerView;
    private MyShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_responses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initFirebaseConnection();

        responsesRecyclerView = findViewById(R.id.responsesRecyclerView);
        textView = findViewById(R.id.nothing);
        if(lists.isEmpty())
            textView.setVisibility(View.VISIBLE);
        else
            textView.setVisibility(View.INVISIBLE);
        adapter = new MyShoppingListAdapter(this,lists,true);
        responsesRecyclerView.setAdapter(adapter);
        responsesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setTitle("My responses");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(MyResponsesActivity.this, ShoppingListActivity.class));
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        lists.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ShoppingList newShL = snapshot.getValue(ShoppingList.class);
                if(firebaseUser.getUid().equals(newShL.getCourierID())){
                    lists.add(newShL);
                    if(lists.isEmpty())
                        textView.setVisibility(View.VISIBLE);
                    else
                        textView.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ShoppingList newShL = snapshot.getValue(ShoppingList.class);
                for(int i = 0; i< lists.size(); i++){
                    if(lists.get(i).getId().equals(newShL.getId())){
                        lists.remove(lists.get(i));
                        lists.add(i,newShL);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ShoppingList newShL = snapshot.getValue(ShoppingList.class);
                for(int i = 0; i< lists.size(); i++){
                    if(lists.get(i).getId().equals(newShL.getId())){
                        lists.remove(lists.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void initFirebaseConnection() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Advertisements");
        databaseReference.keepSynced(true);
    }
}