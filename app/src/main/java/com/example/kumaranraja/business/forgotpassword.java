package com.example.kumaranraja.business;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {


    private EditText resetEmail;
    Button submit;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        resetEmail=findViewById(R.id.resetemail);
        submit = findViewById(R.id.submit);
        auth=FirebaseAuth.getInstance();

        ImageView backButton = findViewById(R.id.backactivity);
        backButton.setOnClickListener(v -> finish());

        submit.setOnClickListener(view -> {
            String email=resetEmail.getText().toString();
            resetpassword(email);
        });

    }
    public void resetpassword(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(forgotpassword.this, "Password Reset Link Send To Your Email Kindly Check", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(forgotpassword.this, "Enter Your Registered Email Address", Toast.LENGTH_SHORT).show();
            }
        });
    }
}