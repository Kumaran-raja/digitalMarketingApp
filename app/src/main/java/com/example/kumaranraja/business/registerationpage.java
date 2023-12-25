package com.example.kumaranraja.business;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;



public class registerationpage extends AppCompatActivity {


    EditText name, email, password, referralcode,phone;

    TextView login, alert;
    Button signup;

    ProgressBar process;
    FirebaseAuth auth;

    private RadioGroup radioGroup;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users Details");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerationpage);

        name = findViewById(R.id.name);

        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup = findViewById(R.id.signup);
        auth = FirebaseAuth.getInstance();
        alert = findViewById(R.id.alert);
        alert.setVisibility(View.INVISIBLE);
        process = findViewById(R.id.process);
        referralcode = findViewById(R.id.referralcode);
        radioGroup = findViewById(R.id.planview);
        phone=findViewById(R.id.phone);
        // Generate a unique code and display it in the TextView
        process.setVisibility(View.INVISIBLE);
        login.setOnClickListener(view -> {
            Intent i = new Intent(registerationpage.this, MainActivity.class);
            startActivity(i);
        });

        signup.setOnClickListener(view -> {
            process.setVisibility(View.VISIBLE);
            String userName = name.getText().toString();
            String Useremail = email.getText().toString();
            String userPassword = password.getText().toString();
            String sponserid = referralcode.getText().toString();
            String phonenumber =phone.getText().toString();
            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

            if (userName.isEmpty()) {
                email.setError("Enter Your Name");
                process.setVisibility(View.INVISIBLE);
            } else if (Useremail.isEmpty()) {
                email.setError("Enter Your Email");
                process.setVisibility(View.INVISIBLE);
            } else if (userPassword.isEmpty()) {
                password.setError("Enter Your Password");
                process.setVisibility(View.INVISIBLE);
            } else if (sponserid.isEmpty()) {
                referralcode.setError("Enter Your Referral Code");
                process.setVisibility(View.INVISIBLE);
            } else if (phonenumber.length()!=10) {
                phone.setError("Enter Correct Phone Number");

                process.setVisibility(View.INVISIBLE);
            } else if (selectedRadioButtonId == -1) {
                Toast.makeText(registerationpage.this, "Select yor plan", Toast.LENGTH_SHORT).show();
                process.setVisibility(View.INVISIBLE);
            }else {
                process.setVisibility(View.VISIBLE);
                phonenumberexist(phonenumber,sponserid);


                process.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void phonenumberexist(String phone,String sponserid) {
        DatabaseReference phonenocheck = FirebaseDatabase.getInstance().getReference("Phone Number Check");
        phonenocheck.orderByValue().equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Name already exists
                    Toast.makeText(registerationpage.this, "Phone Number already exists!", Toast.LENGTH_SHORT).show();
                }else{
                    checkCodeExists(sponserid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(registerationpage.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkCodeExists(String codeToCheck) {

        DatabaseReference checksponserid = FirebaseDatabase.getInstance().getReference("ALL USER ID");
        // Query the database for the specified code
        checksponserid.orderByValue().equalTo(codeToCheck).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userName = name.getText().toString();
                    String Useremail = email.getText().toString();
                    String userPassword = password.getText().toString();
                    String sponserid = referralcode.getText().toString();
                    String phonenumber =phone.getText().toString();
                    String selectedOption = radioGroup.toString();
                    signupAccount(userName,Useremail, phonenumber,userPassword, sponserid, selectedOption);
                    process.setVisibility(View.INVISIBLE);

                } else {
                    // The code does not exist in the database
                    Toast.makeText(registerationpage.this, "Sponsor ID Mismatch Contact Your Sponsor", Toast.LENGTH_SHORT).show();
                    process.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
                Toast.makeText(registerationpage.this, "Check Your Network Connections", Toast.LENGTH_SHORT).show();
                process.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void signupAccount(String userName, String useremail,String phone, String userPassword, String sponserid,String selectedOption) {
        // Sponsor code exists, proceed with registration
        auth.createUserWithEmailAndPassword(useremail, userPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendVerificationEmail();
                        
                        String refer = referralcode.getText().toString();
                        String name1 = name.getText().toString();

                        String IdStatus="INACTIVE";

                        TextView uniqueCodeTextView = findViewById(R.id.uniqueCodeTextView);
                        alert.setVisibility(View.VISIBLE);
                        String uniqueCode = UniqueCodeGenerator.generateUniqueCode();
                        uniqueCodeTextView.setText(uniqueCode);


                        DatabaseReference phonenocheck = FirebaseDatabase.getInstance().getReference("Phone Number Check");
                        phonenocheck.push().setValue(phone);

                        //downline check
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("DOWNLINES");
                        String userId=usersRef.push().getKey();
                        DatabaseReference sponsorRef = usersRef.child(sponserid).child(userId);
                        sponsorRef.child("Downline ProfileID").setValue(uniqueCode);

                        //SharedPreferences used i allwork.java
                        // Storing the userId in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId", userId);
                        editor.apply();

                        initializeTaskWalletForNewUser(uniqueCode);
                        //  initializeReferalWalletForNewUser(uniqueCode);

                        String currentDate = getCurrentDate();

                        //CHOOSING PLAN
                        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                        String selectedOption1 = "";
                        if (selectedRadioButtonId != -1) {
                                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                                selectedOption1 = selectedRadioButton.getText().toString();
                                sponsorRef.child("plan").setValue(selectedOption1);

                        } else {
                            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
                        }

                        sponsorRef.child("registrationDate").setValue(currentDate);
                        sponsorRef.child("Downline Name").setValue(name1);
                        sponsorRef.child("status").setValue(IdStatus);
                        sponsorRef.child("Phone Number").setValue(phone);
                        String userEmail = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
                        email.setText(userEmail);


                        String  dob1="";
                        String address1="";
                        String plan= selectedOption1;
                        User user = new User(name1,phone,userEmail,dob1,address1,refer,uniqueCode,currentDate,IdStatus,plan);


                        myRef.child(Objects.requireNonNull(auth.getUid())).setValue(user);
                        Toast.makeText(registerationpage.this, "Account SignUp Sucessfully!!!", Toast.LENGTH_LONG).show();
                        process.setVisibility(View.INVISIBLE);
                    } else {
                        // Registration failed
                        Toast.makeText(registerationpage.this, "Already Exist Login Your Account", Toast.LENGTH_SHORT).show();
                        process.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(registerationpage.this, "Verification email sent to Your EMAIL", Toast.LENGTH_SHORT).show();
                            // You can add code to navigate to a verification activity or handle the UI accordingly
                        } else {
                            Toast.makeText(registerationpage.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    //Task Wallet initial
    
    private void initializeTaskWalletForNewUser(String uniqueCode) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();


        if (uid != null) {
            DatabaseReference taskWalletRef = FirebaseDatabase.getInstance().getReference("WalletAvailableAmount").child(uniqueCode).child(uid);

            // Check if the taskwallet value already exists
            taskWalletRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // Set the initial value of taskwallet to 0
                        taskWalletRef.child("taskwallet").setValue(0);
                        taskWalletRef.child("referralWallet").setValue(0);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(registerationpage.this, "Your Registeration Not Complete Kindly Contact Admin Team" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }



}