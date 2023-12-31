package com.example.kumaranraja.business;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class activation_and_withdrawal_history extends AppCompatActivity {
    private TableLayout tableLayout;
    private FirebaseAuth mAuth;
    TextView history;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_and_withdrawal_history);

        tableLayout = findViewById(R.id.tableLayout);
        history=findViewById(R.id.history);
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference profileref = database.getReference("Users Details");
        DatabaseReference payoutHistory = database.getReference("Plan Upgrade Fees & Withdrawal History");

        profileref.child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String profileID = user.getProfileID();

                        payoutHistory.child(mAuth.getUid()).child(profileID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    history.setVisibility(View.INVISIBLE);
                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String taskDate = childSnapshot.child("Task date").getValue(String.class);
                                        String amountFrom = childSnapshot.child("Amount From").getValue(String.class);
                                        int amount = childSnapshot.child("amount").getValue(Integer.class);
                                        addTableRow(taskDate, amountFrom, amount);
                                    }
                                } else {
                                    history.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle the error
                                Log.e("RetrieveDataActivity", "Error getting data", databaseError.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Log.e("AnotherActivity", "Error getting data", databaseError.toException());
            }
        });
    }

    private void addTableRow(String date, String description, int amount) {
        TableRow tableRow = new TableRow(this);

        TextView sno = new TextView(this);
        sno.setText(String.valueOf(tableLayout.getChildCount()));
        sno.setPadding(8, 25, 8, 25);
        sno.setGravity(Gravity.CENTER);
        sno.setTextSize(20);
        sno.setBackgroundResource(R.drawable.background_shape);
        tableRow.addView(sno);

        TextView amountColumn = new TextView(this);
        amountColumn.setText(String.valueOf(amount));
        amountColumn.setPadding(8, 25, 8, 25);
        amountColumn.setGravity(Gravity.CENTER);
        amountColumn.setTextSize(20);
        amountColumn.setBackgroundResource(R.drawable.background_shape);
        tableRow.addView(amountColumn);

        TextView descriptionColumn = new TextView(this);
        descriptionColumn.setText(description);
        descriptionColumn.setPadding(8, 25, 8, 25);
        descriptionColumn.setGravity(Gravity.CENTER);
        descriptionColumn.setTextSize(20);
        descriptionColumn.setBackgroundResource(R.drawable.background_shape);
        tableRow.addView(descriptionColumn);

        TextView dateColumn = new TextView(this);
        dateColumn.setText(date);
        dateColumn.setPadding(8, 25, 8, 25);
        dateColumn.setGravity(Gravity.CENTER);
        dateColumn.setTextSize(20);
        dateColumn.setBackgroundResource(R.drawable.background_shape);
        tableRow.addView(dateColumn);

        tableLayout.addView(tableRow);
    }
}