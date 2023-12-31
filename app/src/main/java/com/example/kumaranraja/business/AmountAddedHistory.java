package com.example.kumaranraja.business;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AmountAddedHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_added_history);

        recyclerView = findViewById(R.id.recyclerView);
        mAuth = FirebaseAuth.getInstance();
        ImageView backButton = findViewById(R.id.backactivity);
        backButton.setOnClickListener(v -> finish());

        DatabaseReference profileref = database.getReference("Users Details");
        DatabaseReference payoutHistory = database.getReference("Payout History");

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
                                    List<Payout> dataList = new ArrayList<>();
                                    int counter = 1;

                                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                        String taskDate = childSnapshot.child("Task date").getValue(String.class);
                                        String amountFrom = childSnapshot.child("Amount From").getValue(String.class);
                                        int amount = childSnapshot.child("amount").getValue(Integer.class);
                                        long timestamp = childSnapshot.child("timestamp").getValue(Long.class);
                                        Payout data = new Payout(1, taskDate, amountFrom, amount,timestamp);
                                        dataList.add(data);
                                        counter++;

                                    }
                                    Collections.sort(dataList, (payout1, payout2) -> Long.compare(payout2.getTimestamp(), payout1.getTimestamp()));

                                    for (int i = 0; i < dataList.size(); i++) {
                                        dataList.get(i).setSno(i + 1);
                                    }

                                    /*
                                    Collections.sort(dataList, (payout1, payout2) -> {
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                            Date date1 = sdf.parse(payout1.getTaskDate());
                                            Date date2 = sdf.parse(payout2.getTaskDate());
                                            return date2.compareTo(date1);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            return 0;
                                        }
                                    });

                                    */


                                    // Set up RecyclerView with the adapter
                                    adapter = new MyAdapter(AmountAddedHistory.this, dataList);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(AmountAddedHistory.this));
                                    recyclerView.setAdapter(adapter);
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
}