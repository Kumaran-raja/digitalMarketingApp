package com.example.kumaranraja.business;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import java.util.Objects;


public class Downline extends AppCompatActivity {
    private TableLayout tableLayout;
    FirebaseAuth mAuth;
    ImageView backButton;
    TextView msgview;

    private DatabaseReference downlinesRef;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downline);
        tableLayout = findViewById(R.id.tableLayout);
        mAuth = FirebaseAuth.getInstance();
        backButton=findViewById(R.id.backactivity);
        backButton.setOnClickListener(v -> finish());
        msgview=findViewById(R.id.msgview);
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
                            String profileID=user.getProfileID();

                            DatabaseReference downlinesRef = FirebaseDatabase.getInstance()
                                    .getReference("DOWNLINES")
                                    .child(profileID);

                            downlinesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        msgview.setVisibility(View.INVISIBLE);
                                    }
                                    for (DataSnapshot downlineSnapshot : dataSnapshot.getChildren()) {
                                        String downlineKey = downlineSnapshot.getKey();
                                        String downlineProfileId = downlineSnapshot.child("Downline ProfileID").getValue(String.class);
                                        String regDate = downlineSnapshot.child("registrationDate").getValue(String.class);
                                        String downlineName = downlineSnapshot.child("Downline Name").getValue(String.class);
                                        String status = downlineSnapshot.child("status").getValue(String.class);
                                        String plan = downlineSnapshot.child("plan").getValue(String.class);
                                         //all detail show in downline table view
                                        addTableRow(downlineName, downlineProfileId, regDate, status, plan);
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


    private void addTableRow(String DownlineName,String downlineProfileId,String regDate,String status,String plan) {
        TableRow tableRow = new TableRow(this);

        TextView sno = new TextView(this);
        sno.setText(String.valueOf(tableLayout.getChildCount()));
        sno.setPadding(8, 25, 8, 25);
        sno.setGravity(Gravity.CENTER);
        sno.setBackgroundResource(R.drawable.background_shape);
        sno.setTextSize(20);
        tableRow.addView(sno);

        TextView downlineName = new TextView(this);
        downlineName.setText(DownlineName);
        downlineName.setPadding(8, 25, 8, 25);
        downlineName.setGravity(Gravity.CENTER);
        downlineName.setTextSize(20);
        downlineName.setBackgroundResource(R.drawable.background_shape);
        tableRow.addView(downlineName);

        TextView downlineprofileId = new TextView(this);
        downlineprofileId.setText(downlineProfileId);
        downlineprofileId.setPadding(8, 25, 8, 25);
        downlineprofileId.setGravity(Gravity.CENTER);
        downlineprofileId.setTextSize(20);
        downlineprofileId.setBackgroundResource(R.drawable.background_shape);
        tableRow.addView(downlineprofileId);

        TextView downlineDOJ = new TextView(this);
        downlineDOJ.setText(regDate);
        downlineDOJ.setPadding(8, 25, 8, 25);
        downlineDOJ.setGravity(Gravity.CENTER);
        downlineDOJ.setTextSize(20);
        downlineDOJ.setBackgroundResource(R.drawable.background_shape);
        tableRow.addView(downlineDOJ);

        TextView Downlineplan = new TextView(this);
        Downlineplan.setText(plan);
        Downlineplan.setPadding(8, 25, 8, 25);
        Downlineplan.setGravity(Gravity.CENTER);
        Downlineplan.setTextSize(20);
        Downlineplan.setBackgroundResource(R.drawable.background_shape);
        tableRow.addView(Downlineplan);


        TextView DownlineIDstatus= new TextView(this);
        DownlineIDstatus.setText(status);
        DownlineIDstatus.setPadding(8, 25, 8, 25);
        DownlineIDstatus.setGravity(Gravity.CENTER);
        DownlineIDstatus.setTextSize(20);
        DownlineIDstatus.setBackgroundResource(R.drawable.background_shape);
        tableRow.addView(DownlineIDstatus);
        tableLayout.addView(tableRow);
    }

}