package com.example.kumaranraja.business;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText email, password;
    Button submit;
    FirebaseAuth auth;
    ProgressBar process;
    TextView forgotpassword,newaccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        newaccount=findViewById(R.id.registeration);
        process=findViewById(R.id.progressbar);
        process.setVisibility(View.INVISIBLE);
        auth = FirebaseAuth.getInstance();
        newaccount.setOnClickListener(view -> {
            process.setVisibility(View.VISIBLE);
            Intent i=new Intent(MainActivity.this,registerationpage.class);
            startActivity(i);
        });
        //forgot password activity
        forgotpassword = findViewById(R.id.forgotpasssword);

        forgotpassword.setOnClickListener(view -> {
            process.setVisibility(View.VISIBLE);
            Intent i=new Intent(MainActivity.this, forgotpassword.class);
            startActivity(i);
        });

        //login activity
        submit.setOnClickListener(view -> {

            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
            if (userEmail.isEmpty()) {
                email.requestFocus();
                email.setError("Enter Your Email");
            } else if (userPassword.isEmpty()) {
                password.requestFocus();
                password.setError("Enter Your Password");
            } else {
                process.setVisibility(View.VISIBLE);
                signin(userEmail, userPassword);

            }


        });
    }
    private void clearInputFields() {
        email.setText("");
        password.setText("");
    }

    public void signin(String userEmail, String userPassword) {
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    Intent i = new Intent(MainActivity.this, allwork.class);
                    startActivity(i);
                    clearInputFields();
                    process.setVisibility(View.INVISIBLE);
                } else {
                    // Email is not verified
                    Toast.makeText(MainActivity.this, "Email not verified. Check your email for a verification link.", Toast.LENGTH_SHORT).show();
                    process.setVisibility(View.INVISIBLE);
                }
                process.setVisibility(View.INVISIBLE);

            } else {
                process.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Check your Email and Password", Toast.LENGTH_SHORT).show();
            }

        });

    }




}