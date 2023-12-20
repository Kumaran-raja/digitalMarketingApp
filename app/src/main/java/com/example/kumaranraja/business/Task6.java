package com.example.kumaranraja.business;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Task6 extends AppCompatActivity {

    WebView webView;
    Button submittask;
    ProgressBar process;

    private FirebaseAuth mAuth;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task4);
        webView = findViewById(R.id.webvideo);
        submittask = findViewById(R.id.submit);
        process = findViewById(R.id.process);
        process.setVisibility(View.INVISIBLE);

        ImageView backButton = findViewById(R.id.backactivity);
        backButton.setOnClickListener(v -> finish());


        sharedPreferences = getSharedPreferences("Task6Prefs", MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        //video show
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/NarULzc40Dk?si=bnKKqPXdi2QcqEEY\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        submittask.setEnabled(false);
        new CountDownTimer(10000, 10) { //Set Timer for 10 seconds
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                submittask.setEnabled(true);
            }
        }.start();

        submittask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isButtonClickable()) {
                    Toast.makeText(Task6.this, "Task6 Today Already Completed", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add amount to taskwallet
                addAmountToTaskWallet();

                // Disable button for the day
                disableButtonForDay();

                // Navigate to allwork activity
                Intent i = new Intent(Task6.this, Today_task.class);
                startActivity(i);
            }

        });
    }

    private boolean isButtonClickable() {
        if (mAuth.getCurrentUser() == null) {
            return false; // User not authenticated, button not clickable
        }

        String uid = mAuth.getCurrentUser().getUid();
        String lastClickDate = sharedPreferences.getString(uid + "_lastClickDate", "");

        // Get current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Check if the button was clicked today and if the last click date is not today
        return !lastClickDate.equals(currentDate);
    }
    // Disable button for the day
    private void disableButtonForDay() {
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getCurrentUser().getUid();
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Save the current date as the last click date for the specific user
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            editor.putString(uid + "_lastClickDate", currentDate).apply();
        }
    }

    // Add amount to taskwallet
    private void addAmountToTaskWallet() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference profileref = database.getReference("Users Details");
        profileref.child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the user's data from the database
                    User user = dataSnapshot.getValue(User.class);

                    String profileid=user.getProfileID();
                    SharedPreferences sharedPreferences = getSharedPreferences("Task1Prefs", MODE_PRIVATE);
                    int amountToAdd = 5; // You can change this value as needed
                    int currentTaskWalletAmount = sharedPreferences.getInt("taskwallet", 0);

                    checkFirebaseTaskWallet(currentTaskWalletAmount, amountToAdd,profileid);

                } else {
                    Toast.makeText(Task6.this, "", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });

    }
    // Update taskwallet in allwork activity

    private void updateTaskWalletInAllWork(int updatedTaskWalletAmount) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference profileref = database.getReference("Users Details");
        profileref.child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the user's data from the database
                    User user = dataSnapshot.getValue(User.class);

                    String profileid=user.getProfileID();
                    SharedPreferences allWorkPreferences = getSharedPreferences("AllWorkPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = allWorkPreferences.edit();
                    editor.putInt("taskwallet", updatedTaskWalletAmount);
                    editor.apply();

                    // Update taskwallet in Firebase
                    updateTaskWalletInFirebase(updatedTaskWalletAmount,profileid);

                } else {
                    Toast.makeText(Task6.this, "", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }



    private void updateTaskWalletInFirebase(int updatedTaskWalletAmount,String profileid) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        if (uid != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("WalletAvailableAmount").child(profileid).child(uid).child("taskwallet");

            // Update taskwallet value in Firebase
            myRef.setValue(updatedTaskWalletAmount)
                    .addOnSuccessListener(aVoid -> {
                        // Update in Firebase successful
                        Toast.makeText(Task6.this, "Task3 amount Added Successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        Toast.makeText(Task6.this, "Task3 amount cannot Added Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void checkFirebaseTaskWallet(int currentTaskWalletAmount, int amountToAdd,String profileID) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        if (uid != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("WalletAvailableAmount").child(profileID).child(uid).child("taskwallet");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Firebase taskwallet value exists
                        int firebaseTaskWalletAmount = dataSnapshot.getValue(Integer.class);
                        int updatedTaskWalletAmount = firebaseTaskWalletAmount + amountToAdd;

                        // Update taskwallet in shared preferences
                        sharedPreferences.edit().putInt("taskwallet", updatedTaskWalletAmount).apply();

                        // Update taskwallet in allwork activity
                        updateTaskWalletInAllWork(updatedTaskWalletAmount);

                        // Update taskwallet in Firebase
                        updateTaskWalletInFirebase(updatedTaskWalletAmount,profileID);
                    } else {
                        // Firebase taskwallet value does not exist
                        int updatedTaskWalletAmount = currentTaskWalletAmount + amountToAdd;

                        // Update taskwallet in shared preferences
                        sharedPreferences.edit().putInt("taskwallet", updatedTaskWalletAmount).apply();

                        // Update taskwallet in allwork activity
                        updateTaskWalletInAllWork(updatedTaskWalletAmount);

                        // Update taskwallet in Firebase
                        updateTaskWalletInFirebase(updatedTaskWalletAmount,profileID);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled if needed
                }
            });
        }
    }
}