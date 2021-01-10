package com.example.mobilki.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mobilki.R;
import com.example.mobilki.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RateActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button skipBtn;
    Button rateBtn;
    TextView rateTitle;
    CircleImageView circleImageView;
    String courierId;
    DatabaseReference databaseReference;
    double rate;
    int rateCount;
    String firstName;
    String lastName;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        Intent intent = getIntent();
        courierId = intent.getExtras().getString("courierId");
        circleImageView = findViewById(R.id.profile_image);

        ratingBar = findViewById(R.id.ratingBar);
        skipBtn = findViewById(R.id.skipBtn);
        rateBtn = findViewById(R.id.rateBtn);
        rateTitle = findViewById(R.id.rateTitleUser);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(courierId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User _user = snapshot.getValue(User.class);
                firstName = _user.getFirstName();
                lastName = _user.getLastName();
                rate = _user.getRate();
                rateCount = _user.getRateCount();
                rateTitle.setText(firstName + " " + lastName);
                if (Objects.equals(_user.getImageUrl(), "default")) {
                    circleImageView.setImageResource(R.drawable.profile_icon);
                } else {
                    Picasso.get().load(_user.getImageUrl()).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        skipBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),MyShLActivity.class));
            finish();
        });

        rateBtn.setOnClickListener(v -> {
            double newRate;
            if(rateCount == 0) {
                newRate = ratingBar.getRating();
            }
            else {
                newRate = (rate + ratingBar.getRating()) / 2;
            }
            rateCount ++;
            databaseReference.child("rate").setValue(newRate);
            databaseReference.child("rateCount").setValue(rateCount);
            startActivity(new Intent(getApplicationContext(),MyShLActivity.class));
            finish();
        });




    }
}