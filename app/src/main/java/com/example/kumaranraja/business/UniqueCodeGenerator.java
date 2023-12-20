package com.example.kumaranraja.business;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UniqueCodeGenerator {

    private static int counter = 1;

    public static String generateUniqueCode() {
        String prefix = "OBNB";
        String uniqueId = UUID.randomUUID().toString().replaceAll("[^0-9]", ""); // Extract only numeric characters

        // Ensure the numeric part is exactly 5 digits long
        uniqueId = uniqueId.length() >= 5 ? uniqueId.substring(0, 5) : String.format("%-5s", uniqueId).replace(' ', '0');

        String uniqueCode = prefix + uniqueId;

        // If you've exhausted all 5-digit values, reset the counter or take appropriate action
        if (counter >= 99999) {
            counter = 1; // Reset the counter to 1
            // Alternatively, you can take a different action, such as logging an alert
        }

        counter++;
        storeUniqueCodeInDatabase(uniqueCode);
        return uniqueCode;
    }
    private static void storeUniqueCodeInDatabase(String code) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
      //  DatabaseReference codesRef = database.getReference("Profile ID");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();

        if (uid != null) {
            // Create a HashMap to store the profileId
            Map<String, Object> profileMap = new HashMap<>();
            profileMap.put("profileId", code);
            // Set the value in the database
           // codesRef.child(uid).setValue(profileMap);
            DatabaseReference checksponserid = FirebaseDatabase.getInstance().getReference("ALL USER ID");
            checksponserid.push().setValue(code);
        }
    }


}