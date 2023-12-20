package com.example.kumaranraja.business;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Withdrawal_activity extends AppCompatActivity {

    TextView taskincome,referralincome,taskview,referview;
    EditText enterAmount;
    Button withdrawal;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Withdrawal List");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        enterAmount = findViewById(R.id.enteramount);
        withdrawal = findViewById(R.id.withdrawal);
        mAuth = FirebaseAuth.getInstance();
        taskincome = findViewById(R.id.taskamount);

        referralincome = findViewById(R.id.referralamount);
        taskview = findViewById(R.id.taskview);
        referview = findViewById(R.id.referview);

        String taskIncomeText = taskincome.getText().toString();
        int taskincomeenable = TextUtils.isDigitsOnly(taskIncomeText) ? Integer.parseInt(taskIncomeText) : 0;
        taskincome.setEnabled(taskincomeenable > 5000);

        String referralIncomeText = referralincome.getText().toString();
        int referralincomeenable = TextUtils.isDigitsOnly(referralIncomeText) ? Integer.parseInt(referralIncomeText) : 0;
        referralincome.setEnabled(referralincomeenable > 500);

        String taskamount = getIntent().getStringExtra("Task Income");
        String referralincome1 = getIntent().getStringExtra("Referral Income");
        ImageView backButton = findViewById(R.id.backactivity);
        backButton.setOnClickListener(v -> finish());

        taskincome.setText(taskamount);
        referralincome.setText(referralincome1);


        String[] data = {"Task Wallet", "Referral Wallet"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = findViewById(R.id.spinner);

        spinner.setAdapter(adapter);
        taskview.setVisibility(View.GONE);
        taskincome.setVisibility(View.GONE);
        referview.setVisibility(View.GONE);
        referralincome.setVisibility(View.GONE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                String selectedItem = parentView.getItemAtPosition(position).toString();
                Toast.makeText(Withdrawal_activity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                if("Task Wallet".equals(selectedItem)){
                    taskview.setVisibility(View.VISIBLE);
                    taskincome.setVisibility(View.VISIBLE);

                    referview.setVisibility(View.GONE);
                    referralincome.setVisibility(View.GONE);
                } else if ("Referral Wallet".equals(selectedItem)) {
                    referview.setVisibility(View.VISIBLE);
                    referralincome.setVisibility(View.VISIBLE);

                    taskview.setVisibility(View.GONE);
                    taskincome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        //withdrawal process

        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedItem = spinner.getSelectedItem().toString();
                if(selectedItem.equals("Task Wallet")){
                    int EnterAmount1 = Integer.parseInt(enterAmount.getText().toString());
                    int taskavailableamount = Integer.parseInt(taskincome.getText().toString());
                    if (EnterAmount1 < 5000) {
                        Toast.makeText(Withdrawal_activity.this, "MINIMUM WITHDRAWAL 5000", Toast.LENGTH_LONG).show();
                    }else if (taskavailableamount < 5000) {
                        Toast.makeText(Withdrawal_activity.this, "Task Income Reach Minimum 5000", Toast.LENGTH_LONG).show();
                    } else {

                        HashMap<String, Object> result = new HashMap<>();
                        String enterreqAmount = enterAmount.getText().toString();
                        removeTaskAmountfromWallet(enterreqAmount);
                        result.put("Withdrawal Amount", enterreqAmount);
                        myRef.child("TaskWallet").child(mAuth.getUid()).setValue(result);
                        Toast.makeText(Withdrawal_activity.this, "Withdrawal Request Submit Sucessfully!!!", Toast.LENGTH_LONG).show();


                    }
                }

                else if(selectedItem.equals("Referral Wallet")){
                    int EnterAmount1 = Integer.parseInt(enterAmount.getText().toString());
                    int referavailableamount = Integer.parseInt(referralincome.getText().toString());
                    if (EnterAmount1 < 600) {
                        Toast.makeText(Withdrawal_activity.this, "MINIMUM WITHDRAWAL 600", Toast.LENGTH_LONG).show();
                    }else if (referavailableamount < 600) {
                        Toast.makeText(Withdrawal_activity.this, "Referral Income Reach Minimum 600", Toast.LENGTH_LONG).show();
                    } else {

                        HashMap<String, Object> result = new HashMap<>();
                        String enterreqAmount = enterAmount.getText().toString();
                        removeReferralAmountfromWallet(enterreqAmount);
                        result.put("Withdrawal Amount", enterreqAmount);
                        myRef.child("ReferralWallet").child(mAuth.getUid()).setValue(result);
                        Toast.makeText(Withdrawal_activity.this, "Withdrawal Request Submit Sucessfully!!!", Toast.LENGTH_LONG).show();


                    }
                }
                else{
                    Toast.makeText(Withdrawal_activity.this, "Choose Your Wallet", Toast.LENGTH_LONG).show();
                }

            }

        });

    }



    //taskAmount
    private void removeTaskAmountfromWallet(String enterreqAmount) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        DatabaseReference taskWalletRef = FirebaseDatabase.getInstance().getReference("WalletAvailableAmount").child(uid);

        // Check if the taskwallet value already exists
        taskWalletRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the current taskwallet value
                    Long currentTaskWalletValue = dataSnapshot.child("taskwallet").getValue(Long.class);

                    if (currentTaskWalletValue != null) {
                        int taskWithdrawal = Integer.parseInt(String.valueOf(enterreqAmount));
                        int updatedTaskWalletAmount = currentTaskWalletValue.intValue() - taskWithdrawal;

                        // Update taskwallet in Firebase
                        taskWalletRef.child("taskwallet").setValue(updatedTaskWalletAmount);


                    } else {
                        Toast.makeText(Withdrawal_activity.this, "Taskwallet value is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Withdrawal_activity.this, "DataSnapshot does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Withdrawal_activity.this, "Error retrieving data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //referralAmount
    private void removeReferralAmountfromWallet(String enterreqAmount) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();
        DatabaseReference referWalletRef = FirebaseDatabase.getInstance().getReference("WalletAvailableAmount").child(uid);

        // Check if the taskwallet value already exists
        referWalletRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the current taskwallet value
                    Long currentTaskWalletValue = dataSnapshot.child("referralWallet").getValue(Long.class);

                    if (currentTaskWalletValue != null) {
                        int referralWithdrawal = Integer.parseInt(String.valueOf(enterreqAmount));
                        int updatedTaskWalletAmount = currentTaskWalletValue.intValue() - referralWithdrawal;

                        // Update taskwallet in Firebase
                        referWalletRef.child("referralWallet").setValue(updatedTaskWalletAmount);


                    } else {
                        Toast.makeText(Withdrawal_activity.this, "referralWallet value is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Withdrawal_activity.this, "DataSnapshot does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Withdrawal_activity.this, "Error retrieving data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}


