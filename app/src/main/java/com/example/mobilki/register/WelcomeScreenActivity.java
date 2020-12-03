package com.example.mobilki.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mobilki.HomeActivity;
import com.example.mobilki.R;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        TextView welcomeTxt = (TextView)findViewById(R.id.welcomTextId);

        Button startUsing = (Button)findViewById(R.id.welcomeBtnId);

        startUsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntend = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(startIntend);
            }
        });

        welcomeTxt.setText("Welcome in our app!\nThanks for registering\nwe wish you'll\nenjoy using it.\n");


    }
}