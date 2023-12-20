package com.example.kumaranraja.business;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Map;


public class Downline extends AppCompatActivity {
    private TableLayout tableLayout;
    FirebaseAuth mAuth;
    ImageView backButton,textalert;
    private SharedPreferences sharedPreferences;
    private boolean updateOccurred = false;

    private DatabaseReference downlinesRef;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downline);
        tableLayout = findViewById(R.id.tableLayout);
        textalert=findViewById(R.id.textalert);
        mAuth = FirebaseAuth.getInstance();
        backButton=findViewById(R.id.backactivity);
        backButton.setOnClickListener(v -> finish());
        sharedPreferences = getSharedPreferences("amount", MODE_PRIVATE);
        String uid = mAuth.getUid();
        downlinesRef = FirebaseDatabase.getInstance()
                .getReference("DOWNLINES");

        if (uid != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference codesRef = database.getReference("Users Details").child(uid);

            // Attach a ValueEventListener to retrieve the data
            codesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot.getValue() returns a Map<String, Object>
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            textalert.setVisibility(View.INVISIBLE);
                            // Populate the EditText fields with the retrieved data
                            String profileID=user.getProfileID();

                            DatabaseReference downlinesRef = FirebaseDatabase.getInstance()
                                    .getReference("DOWNLINES")
                                    .child(profileID);

                            downlinesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot downlineSnapshot : dataSnapshot.getChildren()) {

                                        String downlineKey = downlineSnapshot.getKey();
                                        String downlineProfileId = downlineSnapshot.child("Downline ProfileID").getValue(String.class);
                                        String regDate = downlineSnapshot.child("registrationDate").getValue(String.class);
                                        String downlineName = downlineSnapshot.child("Downline Name").getValue(String.class);
                                        String status = downlineSnapshot.child("status").getValue(String.class);
                                        String plan = downlineSnapshot.child("plan").getValue(String.class);
                                        //all detail show in downline table view
                                        addTableRow(downlineName,downlineProfileId,regDate,status,plan);
                                        if(status.equals("ACTIVE")){
                                            switch ((plan)){
                                                case "Bronze":
                                                    int referamount=30;
                                                    referamountadded(referamount,profileID);
                                                case "Silver":
                                                    int referamount2=120;
                                                    referamountadded(referamount2,profileID);
                                                case "Gold":
                                                    int referamount3=250;
                                                    referamountadded(referamount3,profileID);
                                                case "Diamond":
                                                    int referamount4=600;
                                                    referamountadded(referamount4,profileID);
                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle the error
                                    Log.e("RetrieveDataActivity", "Error getting data", databaseError.toException());
                                }
                            });

                        }
                        else{
                            textalert.setVisibility(View.VISIBLE);
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

    private void referamountadded(int referamount,String profileid) {
        String uid = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("WalletAvailableAmount");
        myRef.child(profileid).child(uid).child("referralWallet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long referralWalletValue = dataSnapshot.getValue(Long.class);


                    if (referralWalletValue != null) {
                        int amount=referamount;
                        int referwallet= (int) (referralWalletValue+amount);
                        myRef.child("referralWallet").setValue(referwallet);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Downline.this, "Error retrieving taskwallet from Firebase", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addTableRow(String DownlineName,String downlineProfileId,String regDate,String status,String plan) {
        TableRow tableRow = new TableRow(this);

        TextView sno = new TextView(this);
        sno.setText(String.valueOf(tableLayout.getChildCount()));
        sno.setPadding(8, 25, 8, 25);
        sno.setGravity(Gravity.CENTER);
        sno.setTextSize(20);
        tableRow.addView(sno);

        TextView downlineName = new TextView(this);
        downlineName.setText(DownlineName);
        downlineName.setPadding(8, 25, 8, 25);
        downlineName.setGravity(Gravity.CENTER);
        downlineName.setTextSize(20);
        tableRow.addView(downlineName);

        TextView downlineprofileId = new TextView(this);
        downlineprofileId.setText(downlineProfileId);
        downlineprofileId.setPadding(8, 25, 8, 25);
        downlineprofileId.setGravity(Gravity.CENTER);
        downlineprofileId.setTextSize(20);
        tableRow.addView(downlineprofileId);

        TextView downlineDOJ = new TextView(this);
        downlineDOJ.setText(regDate);
        downlineDOJ.setPadding(8, 25, 8, 25);
        downlineDOJ.setGravity(Gravity.CENTER);
        downlineDOJ.setTextSize(20);
        tableRow.addView(downlineDOJ);

        TextView Downlineplan = new TextView(this);
        Downlineplan.setText(plan);
        Downlineplan.setPadding(8, 25, 8, 25);
        Downlineplan.setGravity(Gravity.CENTER);
        Downlineplan.setTextSize(20);
        tableRow.addView(Downlineplan);


        TextView DownlineIDstatus= new TextView(this);
        DownlineIDstatus.setText(status);
        DownlineIDstatus.setPadding(8, 25, 8, 25);
        DownlineIDstatus.setGravity(Gravity.CENTER);
        DownlineIDstatus.setTextSize(20);
        tableRow.addView(DownlineIDstatus);
        tableLayout.addView(tableRow);
    }

}