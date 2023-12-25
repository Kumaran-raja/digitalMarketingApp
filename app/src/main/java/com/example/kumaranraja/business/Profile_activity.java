package com.example.kumaranraja.business;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class Profile_activity extends AppCompatActivity {


    EditText name,phone,email,address,dob;
    Button submit,dobButton;
    ProgressBar process;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users Details");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        mAuth = FirebaseAuth.getInstance();


        phone.setEnabled(false);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload()
                    .addOnFailureListener(e -> {
                        Log.e("Profile_activity", "Failed to reload user: " + e.getMessage());
                    })
                    .addOnCompleteListener(task -> {
                        String userEmail = currentUser.getEmail();
                        if (userEmail != null) {
                            email.setText(userEmail);
                            email.setEnabled(false);
                        } else {
                            Log.e("Profile_activity", "User email is null");
                        }
                    });
        } else {
            // Redirect user to login screen or handle accordingly
            Log.e("Profile_activity", "User is not signed in");
        }

        address=findViewById(R.id.address);
        dob=findViewById(R.id.dob);
        dobButton=findViewById(R.id.dobselector);
        submit=findViewById(R.id.submit);

        process=findViewById(R.id.process);
        //calander activity

        process.setVisibility(View.INVISIBLE);


        ImageView backButton = findViewById(R.id.backactivity);
        backButton.setOnClickListener(v -> finish());



        dobButton.setOnClickListener(v -> {
            // Create a DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Profile_activity.this,
                    (view, year, month, dayOfMonth) -> {
                        // Handle the selected date
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth; // Month is zero-based
                        dob.setText(selectedDate);
                    },
                    // Set the default date (optional)
                    1990, 0, 1 // January 1, 1990 (you can set the initial date as per your requirements)
            );

            // Show the DatePickerDialog
            datePickerDialog.show();
        });



        //submit process
        submit.setOnClickListener(view -> {

            if(name.length()<=4 || name.length()>=15){
                name.requestFocus();
                name.setError("Minimum require 4 letter");
            }
            else if(!(phone.length() ==10)){
                phone.requestFocus();
                phone.setError("Enter Correct phone number");
            }
            else if(!isValidEmail(email.getText().toString())){
                email.requestFocus();
                email.setError("Enter Correct Email Address");
            } else if (address.length()>=42) {
                address.requestFocus();
                address.setError("42 character only allowed");
            } else if (dob==null) {
                dobButton.requestFocus();
                dob.setError("chose your DOB below calander");
            }
            else{
                process.setVisibility(View.INVISIBLE);
                String name1 = name.getText().toString();
                String phone1 = phone.getText().toString();
                String email1 = email.getText().toString();
                String dob1 = dob.getText().toString();
                String address1 = address.getText().toString();

                myRef.child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Log the raw data for debugging
                            Log.d("Profile_activity", "Raw Data: " + dataSnapshot.getValue());

                            // Retrieve the user's data from the database
                            User user = dataSnapshot.getValue(User.class);

                            if (user != null) {
                                // Populate the EditText fields with the retrieved data
                                name.setText(user.getName());
                                phone.setText(user.getPhone());
                                email.setText(user.getEmail());
                                dob.setText(user.getDob());
                                address.setText(user.getAddress());

                                String refer = user.getSponsor();
                                String uniqueCode= user.getProfileID();
                                String currentDate= user.getRegDate();
                                String idStatus= user.getStatus();
                                String plan= user.getPlan();

                                User user1 = new User(name1, phone1, email1, dob1, address1,refer,uniqueCode,currentDate,idStatus,plan);
                                myRef.child(Objects.requireNonNull(mAuth.getUid())).setValue(user1);
                            } else {
                                Log.e("Profile_activity", "User object is null");
                            }
                        } else {
                            // Handle the case where data doesn't exist
                            Log.e("Profile_activity", "Data does not exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors here
                        Log.e("Profile_activity", "Error retrieving data: " + databaseError.getMessage());
                    }
                });




                // Show a success message to the user
                Toast.makeText(Profile_activity.this, "Profile Edit Successful", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(Profile_activity.this,allwork.class);
                startActivity(i);
            }
        });


        // retrieve data from database


        myRef.child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Log the raw data for debugging
                    Log.d("Profile_activity", "Raw Data: " + dataSnapshot.getValue());

                    // Retrieve the user's data from the database
                    User user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        // Populate the EditText fields with the retrieved data
                        name.setText(user.getName());
                        phone.setText(user.getPhone());
                        email.setText(user.getEmail());
                        dob.setText(user.getDob());
                        address.setText(user.getAddress());
                    } else {
                        Log.e("Profile_activity", "User object is null");
                    }
                } else {
                    // Handle the case where data doesn't exist
                    Log.e("Profile_activity", "Data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
                Log.e("Profile_activity", "Error retrieving data: " + databaseError.getMessage());
            }
        });
    }
    private boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}