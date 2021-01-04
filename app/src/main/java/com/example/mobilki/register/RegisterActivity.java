package com.example.mobilki.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilki.LoginActivity;
import com.example.mobilki.R;
import com.example.mobilki.User;
import com.example.mobilki.activities.ShoppingListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView firstNameText, lastNameText, ageText, passwordText, emailText;
    private String imageUrl = "default";
    Button loginBtn, registerBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        try {  //hide title bar in register screen
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored) {

        }

        registerBtn = (Button) findViewById(R.id.registerBtn2);
        loginBtn = findViewById(R.id.loginBtn2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);
        ageText = (EditText) findViewById(R.id.ageText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        emailText = (EditText) findViewById(R.id.emailText);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtn2:
                registerUser();
                progressBar.setVisibility(View.VISIBLE);
                break;
            case R.id.loginBtn2:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private boolean validateFields(String firstName, String lastName, String age, String password,
                                String email) {
        if (firstName.isEmpty()) {
            firstNameText.setError("Required!");
            firstNameText.requestFocus();
            return false;
        }
        if (lastName.isEmpty()) {
            lastNameText.setError("Required!");
            lastNameText.requestFocus();
            return false;
        }
        if (age.isEmpty()) {
            ageText.setError("Required!");
            ageText.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            passwordText.setError("Required!");
            passwordText.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            emailText.setError("Required!");
            emailText.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Invalid email!");
            emailText.requestFocus();
            return false;
        }

        if(password.length() < 6) {
            passwordText.setError("Password must be at least 6 characters!");
            passwordText.requestFocus();
            return false;
        }
        return true;
    }

    private void registerUser() {
        final String email = emailText.getText().toString().trim();
        final String firstName = firstNameText.getText().toString().trim();
        final String lastName = lastNameText.getText().toString().trim();
        final String age = ageText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();

        if(!this.validateFields(firstName, lastName, age, password, email)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser fUser = mAuth.getCurrentUser();

                            User user = new User(fUser.getUid(),firstName, lastName, email, age, password, imageUrl);



                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        //wchodzi w aktywnosc z listami zakupow jesli udalo sie zarejestrowac
                                        Intent intent = new Intent(getApplicationContext(), WelcomeScreenActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to sign up!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register!1", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}