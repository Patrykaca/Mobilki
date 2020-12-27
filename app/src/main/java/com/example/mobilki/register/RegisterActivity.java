package com.example.mobilki.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.mobilki.R;
import com.example.mobilki.User;
import com.example.mobilki.activities.ShoppingListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView firstNameText, lastNameText, ageText, passwordText, emailText;
    Button nextBtn, registerBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nextBtn = (Button) findViewById(R.id.nextBtn);
        registerBtn = (Button) findViewById(R.id.registerBtnPage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        registerBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);
        ageText = (EditText) findViewById(R.id.ageText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        emailText = (EditText) findViewById(R.id.emailText);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerBtnPage:
                registerUser();
                progressBar.setVisibility(View.VISIBLE);
                break;
            case R.id.nextBtn:
                startActivity(new Intent(this, WelcomeScreenActivity.class));
                break;
        }
    }



    private void registerUser() {
        final String email = emailText.getText().toString().trim();
        final String firstName = firstNameText.getText().toString().trim();
        final String lastName = lastNameText.getText().toString().trim();
        final String age = ageText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();

        if (firstName.isEmpty()) {
            firstNameText.setError("Required!");
            firstNameText.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            firstNameText.setError("Required!");
            firstNameText.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            firstNameText.setError("Required!");
            firstNameText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            firstNameText.setError("Required!");
            firstNameText.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            firstNameText.setError("Required!");
            firstNameText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Invalid email!");
            emailText.requestFocus();
            return;
        }

        if(password.length() < 6) {
            emailText.setError("Password must be at least 6 characters!");
            emailText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(firstName, lastName, email, age, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        //wchodzi w aktywnosc z listami zakupow jesli udalo sie zarejestrowac
                                        Intent intent = new Intent(getApplicationContext(), ShoppingListActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register!", Toast.LENGTH_SHORT).show();
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