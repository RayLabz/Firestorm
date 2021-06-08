package com.raylabz.firestorm.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import java.io.IOException;
import java.util.List;

/**
 * Provides utility functions related to Firebase.
 *  @author Nicos Kasenides
 *  @version 1.1.0
 */
public class FirebaseUtils {

    public static FirebaseApp FIREBASE_APP;

    /**
     * Initializes a Firebase app with a database and storage bucket configuration.
     * @return Returns an initialized FirebaseApp.
     * @throws RuntimeException Throws a RuntimeException if no valid Firebase App configuration was found.
     * @throws IOException Throws an IOException if no application default configuration is found.
     */
    public static FirebaseApp initialize() throws RuntimeException, IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build();

        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (firebaseApps != null && !firebaseApps.isEmpty()) {
            for (FirebaseApp a : firebaseApps) {
                if (a.getName().equals(FirebaseApp.DEFAULT_APP_NAME)){
                    return a;
                }
            }
            throw new RuntimeException("Invalid Firebase App.");
        }
        else {
            FIREBASE_APP = FirebaseApp.initializeApp(options);
            return FIREBASE_APP;
        }
    }

    /**
     * Initializes a Firebase app.
     * @param serviceAccountJson A service account JSON string.
     * @return Returns an initialized FirebaseApp.
     * @throws RuntimeException Throws a RuntimeException if no valid Firebase App configuration was found.
     * @throws IOException Throws an IOException if no application default configuration is found.
     */
    public static FirebaseApp initialize(final String serviceAccountJson) throws RuntimeException, IOException {

        final FirebaseConfig config = FirebaseConfig.fromString(serviceAccountJson);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(config.toInputStream()))
                .build();

        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (firebaseApps != null && !firebaseApps.isEmpty()) {
            for (FirebaseApp a : firebaseApps) {
                if (a.getName().equals(FirebaseApp.DEFAULT_APP_NAME)){
                    return a;
                }
            }
            throw new RuntimeException("Invalid Firebase App.");
        }
        else {
            FIREBASE_APP = FirebaseApp.initializeApp(options);
            return FIREBASE_APP;
        }
    }

    /**
     * Verifies a Firebase token.
     * @param token The token.
     * @return Returns the UID of the user owning that token.
     * @throws RuntimeException when the token provided could not be validated.
     */
    public static String verifyToken(final String token) throws RuntimeException {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            return decodedToken.getUid();
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
    }

}
