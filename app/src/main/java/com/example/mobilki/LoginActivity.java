package com.example.mobilki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilki.activities.ShoppingListActivity;
import com.example.mobilki.classes.ShoppingList;
import com.example.mobilki.register.RegisterActivity;
import com.example.mobilki.register.WelcomeScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEditText, passwordEditText;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.firstNameText);
        passwordEditText = findViewById(R.id.lastNameText);


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        try {  //hide title bar in login screen
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored) {

        }
        Button loginBtn = (Button)findViewById(R.id.registerBtn2);
        Button registerBtn = (Button)findViewById(R.id.loginBtn2);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = loginEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if(email.isEmpty() || password.isEmpty())
                    Toast.makeText(getApplicationContext(),R.string.fields_reuired,Toast.LENGTH_SHORT).show();
                else {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(getApplicationContext(), ShoppingListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                                Toast.makeText(getApplicationContext(),R.string.authorization_failed,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

//                Intent startIntend = new Intent(getApplicationContext(), ShoppingListActivity.class);
//                startActivity(startIntend);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start register activity
                Intent startIntend = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(startIntend);
            }
        });
    }
}