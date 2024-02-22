package com.example.demo;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.IOException;

public class Firebase {

    public static void main(String[] args) {
        initializeFirebase();

        // Example usage
        try {
            String email = "example@example.com";
            String password = "password123";

            // Register a new user
            String userId = registerUser(email, password);
            System.out.println("User registered with ID: " + userId);

            // Authenticate the user
            String token = authenticateUser(email, password);
            System.out.println("User authenticated with token: " + token);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void initializeFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("C:/Users/leofe/Downloads/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://your-firebase-project-url.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String registerUser(String email, String password) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        return userRecord.getUid();
    }

    private static String authenticateUser(String email, String password) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);

        if (userRecord != null) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
            return FirebaseAuth.getInstance().createCustomToken(userRecord.getUid());
        } else {
            throw new FirebaseAuthException("user-not-found", "User not found");
        }
    }
}
