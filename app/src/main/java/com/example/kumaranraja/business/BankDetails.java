package com.example.kumaranraja.business;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
public class BankDetails extends AppCompatActivity {

    EditText accountno,confirmaccount,accHolderName,ifsccode;
    TextView bankname,branchname;
    Button submit;
    FirebaseAuth mAuth;
    ProgressBar process;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User Bank Details");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        accountno=findViewById(R.id.accno);
        confirmaccount=findViewById(R.id.confirmaccno);
        accHolderName=findViewById(R.id.accholdername);
        ifsccode=findViewById(R.id.ifsccode);
        bankname=findViewById(R.id.bankname);
        process=findViewById(R.id.process);
        branchname=findViewById(R.id.branchname);
        submit=findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        process.setVisibility(View.INVISIBLE);
        ImageView backButton = findViewById(R.id.backactivity);
        backButton.setOnClickListener(v -> finish());

        submit.setOnClickListener(v -> {

            if(accountno.length()<6 || accountno.length()>17){
                accountno.requestFocus();
                accountno.setError("Enter Correct Account Number");
            }
            else if(confirmaccount.length()<6 || confirmaccount.length()>17){
                confirmaccount.requestFocus();
                confirmaccount.setError("Enter Correct Account Number");
            }
            else if(!accountno.getText().toString().equals(confirmaccount.getText().toString())){
                confirmaccount.requestFocus();
                confirmaccount.setError("Enter Correct Account Number");
            }
            else if(accHolderName.length()<3 || accHolderName.length()>14){
                accHolderName.requestFocus();
                accHolderName.setError("Enter Correct Account Number");
            }
            else if(bankname.length() <3 || bankname.length()>17){
                bankname.requestFocus();
                bankname.setError("Enter Correct Account Number");
            }
            else if(branchname.length() <3 || branchname.length()>17){
                branchname.requestFocus();
                branchname.setError("Enter Correct Account Number");
            }
            else{

                process.setVisibility(View.INVISIBLE);
                String accountnumber = accountno.getText().toString();
                String confirmaccountno = confirmaccount.getText().toString();
                String accountholdername = accHolderName.getText().toString();
                String bankIFSCcode = ifsccode.getText().toString();
                String bank = bankname.getText().toString();
                String branch = branchname.getText().toString();
                Bank bank1 = new Bank(accountnumber, confirmaccountno, accountholdername,bankIFSCcode, bank, branch);
                myRef.child(Objects.requireNonNull(mAuth.getUid())).setValue(bank1);

                Toast.makeText(BankDetails.this ,"Bank Details Submit Sucessfully!!!",Toast.LENGTH_LONG).show();
                Intent i=new Intent(BankDetails.this,allwork.class);
                startActivity(i);
            }
        });
        myRef.child(Objects.requireNonNull(mAuth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the user's data from the database
                    Bank bank1 = dataSnapshot.getValue(Bank.class);

                    accountno.setText(bank1.getAccountnumber());
                    confirmaccount.setText(bank1.getConfirmaccountno());
                    accHolderName.setText(bank1.getAccountholdername());
                    ifsccode.setText(bank1.getBankIFSCcode());
                    bankname.setText(bank1.getBank());
                    branchname.setText(bank1.getBranch());


                } else {

                    Toast.makeText(BankDetails.this, "Edit Your Bank Details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }
}