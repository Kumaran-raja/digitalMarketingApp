package com.example.kumaranraja.business;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
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

public class allwork extends AppCompatActivity {

    TextView name,phone,email,sponsorid,DOJ,joinplan,withtrawalhistory;
    Button statusview;

    TextView taskwallet,profileid,downline,history;
    TextView referralWallet;
    TextView profile,todaytask,bankdetails,withdrawals;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference profileref = database.getReference("Users Details");

    DatabaseReference downlinesRef;

    DatabaseReference PayoutHistory = database.getReference("Plan Upgrade Fees & Withdrawal History");
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allwork);
        taskwallet=findViewById(R.id.taskincome);
        referralWallet=findViewById(R.id.refrralincome);
        profile=findViewById(R.id.profile);
        todaytask=findViewById(R.id.todaytask);
        bankdetails=findViewById(R.id.bankdetails);
        withdrawals=findViewById(R.id.withdrawal);
        mAuth = FirebaseAuth.getInstance();
        profileid=findViewById(R.id.profileid);
        downline=findViewById(R.id.downline);
        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        sponsorid=findViewById(R.id.sponsorprofileid);
        joinplan=findViewById(R.id.plan);
        DOJ=findViewById(R.id.DOJ);
        statusview=findViewById(R.id.status);
        history=findViewById(R.id.history);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        withtrawalhistory = findViewById(R.id.withdrawalhistory);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform your refresh operation here
                fetchData();
            }
        });
        profileref.child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the user's data from the database
                    User user = dataSnapshot.getValue(User.class);

                    assert user != null;
                    name.setText(user.getName());
                    phone.setText(user.getPhone());
                    email.setText(user.getEmail());

                } else {
                    Toast.makeText(allwork.this, "Edit Your Profile Details", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });
        // retrieve data from database
        profileref.child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        profileid.setText(user.getProfileID());

                        SharedPreferences sharedPreferences1 = getSharedPreferences("MyProPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        editor.putString("userProfileId",profileid.getText().toString());
                        editor.apply();

                        sponsorid.setText(user.getSponsor());
                        DOJ.setText(user.getRegDate());
                        joinplan.setText(user.getPlan());
                        statusview.setText(user.getStatus());
                        String status=statusview.getText().toString();
                        withdrawals.setEnabled(status.equals("ACTIVE"));
                        String proid=user.getProfileID();

                        taskwalletretrieve(proid);
                        referralwaletretrieve(proid);

                        statusview.setOnClickListener(view -> {

                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String userId = sharedPreferences.getString("userId", "");

                            String profile=profileid.getText().toString();
                            String planStatus=joinplan.getText().toString();

                            String sponsor=sponsorid.getText().toString();
                            Log.d("StatusButtonClick", "myprofileID: " + sponsor);
                            downlinesRef = FirebaseDatabase.getInstance()
                                    .getReference("DOWNLINES")
                                    .child(sponsor)
                                    .child(userId);



                            //taskwallet amount get for plan upgrade
                            int taskincome=Integer.parseInt(String.valueOf(taskwallet.getText()));
                            Log.d("StatusButtonClick", "taskincome: " + taskincome);
                            String IdStatus=statusview.getText().toString();
                            Log.d("StatusButtonClick", "IdStatus: " + IdStatus);
                            if(IdStatus.equals("ACTIVE")){
                                statusview.setEnabled(false);
                                Toast.makeText(allwork.this, "Already Your Id Active", Toast.LENGTH_SHORT).show();
                            } else if (IdStatus.equals("INACTIVE")) {

                                String planstatuschange="ACTIVE";
                                if(planStatus.equals("Bronze")){
                                    if(300 <= taskincome){

                                        downlinesRef.child("status").setValue("ACTIVE");
                                        String status1 = "ACTIVE";
                                        statuschange();
                                        int Activationfees=300;

                                        int sponsorwallet=30;

                                        removeActiveAmountfromWallet(Activationfees,profile,sponsorwallet);
                                        statusview.setText("ACTIVE");
                                        statusview.setEnabled(false);

                                    }else{
                                        Toast.makeText(allwork.this, "Minimum Reach 300 Rs", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if (planStatus.equals("Silver")) {
                                    if(1200 <= taskincome){
                                        statusview.setText("ACTIVE");
                                        downlinesRef.child("status").setValue("ACTIVE");
                                        int Activationfees=1200;

                                        int sponsorwallet=120;
                                        removeActiveAmountfromWallet(Activationfees,profile,sponsorwallet);
                                        statusview.setEnabled(false);

                                    }else{
                                        Toast.makeText(allwork.this, "Minimum Reach 1200 Rs", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else if (planStatus.equals("Gold")) {
                                    if(2500 <= taskincome){
                                        statusview.setText("ACTIVE");
                                        downlinesRef.child("status").setValue("ACTIVE");

                                        int Activationfees=2500;
                                        int sponsorwallet=250;

                                        removeActiveAmountfromWallet(Activationfees,profile,sponsorwallet);
                                        statusview.setEnabled(false);

                                    }else{
                                        Toast.makeText(allwork.this, "Minimum Reach 2500 Rs", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else if (planStatus.equals("Diamond")) {
                                    if(6000 <= taskincome){
                                        statusview.setText("ACTIVE");
                                        downlinesRef.child("status").setValue("ACTIVE");

                                        int Activationfees=6000;

                                        int sponsorwallet=600;
                                        removeActiveAmountfromWallet(Activationfees,profile,sponsorwallet);
                                        statusview.setEnabled(false);

                                    }else{
                                        Toast.makeText(allwork.this, "Minimum Reach 6000 Rs", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else{
                                    Toast.makeText(allwork.this, "Plan Error Contact Admin Team", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(allwork.this, "Contact Admin", Toast.LENGTH_SHORT).show();
                            }
                        });


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


        profile.setOnClickListener(view -> {
            Intent i=new Intent(allwork.this,Profile_activity.class);
            startActivity(i);
        });
        todaytask.setOnClickListener(view -> {

            Intent i=new Intent(allwork.this,Today_task.class);

            startActivity(i);
        });
        bankdetails.setOnClickListener(view -> {
            Intent i=new Intent(allwork.this,BankDetails.class);
            startActivity(i);
        });
        withdrawals.setOnClickListener(view -> {
            String task=taskwallet.getText().toString();
            String referral=referralWallet.getText().toString();
            Intent i=new Intent(allwork.this,Withdrawal_activity.class);
            i.putExtra("Task Income", task);
            i.putExtra("Referral Income", referral);
            startActivity(i);
        });
        downline.setOnClickListener(view -> {
            Intent i=new Intent(allwork.this, Downline.class);
            startActivity(i);
        });

        history.setOnClickListener(view -> {
            Intent i=new Intent(allwork.this, AmountAddedHistory.class);
            startActivity(i);
        });

        withtrawalhistory.setOnClickListener(view -> {
            Intent i=new Intent(allwork.this, activation_and_withdrawal_history.class);
            startActivity(i);
        });



    }

    private void fetchData() {
        swipeRefreshLayout.setRefreshing(false);
    }


    private void referralwaletretrieve(String proid) {
        String uid = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("WalletAvailableAmount");
        myRef.child(proid).child(uid).child("referralWallet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long referralWalletValue = dataSnapshot.getValue(Long.class);

                    if (referralWalletValue != null) {
                        // Assuming you have a method in allwork to update taskwallet in UI
                        updateReferralWalletInUI(referralWalletValue.intValue());
                        checkReferAmountOccur(referralWalletValue.intValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(allwork.this, "Error retrieving taskwallet from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void taskwalletretrieve(String proid) {
        String uid = mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("WalletAvailableAmount");

        myRef.child(proid).child(uid).child("taskwallet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long taskWalletValue = dataSnapshot.getValue(Long.class);

                    if (taskWalletValue != null) {
                        // Assuming you have a method in allwork to update taskwallet in UI
                        updateTaskWalletInUI(taskWalletValue.intValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(allwork.this, "Error retrieving taskwallet from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void statuschange() {
        profileref.child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Log the raw data for debugging
                    Log.d("Profile_activity", "Raw Data: " + dataSnapshot.getValue());

                    // Retrieve the user's data from the database
                    User user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        // Populate the EditText fields with the retrieved data

                        String name1 = user.getName();
                        String phone1= user.getPhone();
                        String email1= user.getEmail();
                        String dob1= user.getDob();
                        String address1= user.getAddress();
                        String refer = user.getSponsor();
                        String uniqueCode= user.getProfileID();
                        String currentDate= user.getRegDate();
                        String idStatus= "ACTIVE";
                        String plan= user.getPlan();

                        User user1 = new User(name1, phone1, email1, dob1, address1,refer,uniqueCode,currentDate,idStatus,plan);
                        profileref.child(Objects.requireNonNull(mAuth.getUid())).setValue(user1);
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


    private void updateReferralWalletInUI(int updatedReferralWalletAmount) {
        referralWallet.setText(String.valueOf(+updatedReferralWalletAmount));
    }

    // This method is called when the taskwallet is updated in Firebase
    private void updateTaskWalletInUI(int updatedTaskWalletAmount) {
        taskwallet.setText(String.valueOf(+updatedTaskWalletAmount));
    }


   //activation amount remove from taskwallet

    private void removeActiveAmountfromWallet(int activationFees,String profile,int sponsorwallet) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        DatabaseReference taskWalletRef = FirebaseDatabase.getInstance().getReference("WalletAvailableAmount").child(profile).child(uid);

        // Check if the taskwallet value already exists
        taskWalletRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the current taskwallet value
                    Long currentTaskWalletValue = dataSnapshot.child("taskwallet").getValue(Long.class);

                    if (currentTaskWalletValue != null) {
                        int fees = activationFees;


                        //referAmount added to database DOWNLINE path
                        int sponsorwalletadd=sponsorwallet;
                        String sponsorid1=sponsorid.getText().toString();
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        String userId = sharedPreferences.getString("userId", "");
                        downlinesRef = FirebaseDatabase.getInstance()
                                .getReference("DOWNLINES")
                                .child(sponsorid1)
                                .child(userId);
                        downlinesRef.child("ReferAmount").setValue(sponsorwalletadd);


                        int updatedTaskWalletAmount = currentTaskWalletValue.intValue() - fees;

                        // Update taskwallet in Firebase
                        taskWalletRef.child("taskwallet").setValue(updatedTaskWalletAmount);


                        String currentDate = getCurrentDate();
                        String description="Deduct Upgrade Fees";
                        DatabaseReference value = PayoutHistory.child(Objects.requireNonNull(mAuth.getUid())).child(profile).child("upgrade");
                        value.child("Task date").setValue(currentDate);
                        value.child("Amount From").setValue(description);
                        value.child("amount").setValue(activationFees);


                        // Update taskwallet in UI
                        updateTaskWalletInUI(updatedTaskWalletAmount);

                    } else {
                        Toast.makeText(allwork.this, "Taskwallet value is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(allwork.this, "DataSnapshot does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(allwork.this, "Error retrieving data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void checkReferAmountOccur(int referralWalletValue) {
        String uid=mAuth.getUid();
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
                                    for (DataSnapshot downlineSnapshot : dataSnapshot.getChildren()) {

                                        String downlineKey = downlineSnapshot.getKey();
                                        Integer amount = downlineSnapshot.child("ReferAmount").getValue(Integer.class);

                                        if (amount != null) {
                                            try {
                                                String referwalletamount = referralWallet.getText().toString();
                                                int afterReferAmountAdded = Integer.parseInt(referwalletamount);
                                                int totalAmountAdded = amount + afterReferAmountAdded;

                                                DatabaseReference taskWalletRef = FirebaseDatabase.getInstance().getReference("WalletAvailableAmount").child(profileID).child(uid);
                                                taskWalletRef.child("referralWallet").setValue(totalAmountAdded);

                                                String currentDate = getCurrentDate();
                                                String description="Referral Amount";
                                                DatabaseReference value = PayoutHistory.child(Objects.requireNonNull(mAuth.getUid())).child(profileID).child("ReferAmount");
                                                value.child("Task date").setValue(currentDate);
                                                value.child("Amount From").setValue(description);
                                                value.child("amount").setValue(amount);

                                                // Retrieve the amount, display it, and then delete the key and value
                                                Toast.makeText(allwork.this, "Referral Amount Added: " + amount, Toast.LENGTH_SHORT).show();

                                                // Delete the key and value from the database
                                                downlinesRef.child(downlineKey).child("ReferAmount").removeValue();
                                            } catch (NumberFormatException e) {
                                                Log.e("RetrieveDataActivity", "Error parsing taskwalletamount to Integer", e);
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
}