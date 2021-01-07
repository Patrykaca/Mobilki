package com.example.mobilki.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mobilki.R;
import com.example.mobilki.classes.ShoppingList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyResponsesActivity extends AppCompatActivity {

    private ArrayList<ShoppingList> lists = new ArrayList<>();

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_responses);
    }
}