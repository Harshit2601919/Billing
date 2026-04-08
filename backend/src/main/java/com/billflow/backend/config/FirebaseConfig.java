package com.billflow.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    private final ResourceLoader resourceLoader;

    @Value("${firebase.service-account-path:classpath:service-account.json}")
    private String serviceAccountPath;

    @Value("${FIREBASE_CONFIG_JSON:}")
    private String firebaseConfigJson;

    public FirebaseConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount;

            // Priority 1: Use JSON string from Environment Variable (for Render/Koyeb)
            if (firebaseConfigJson != null && !firebaseConfigJson.trim().isEmpty()) {
                serviceAccount = new ByteArrayInputStream(firebaseConfigJson.getBytes(StandardCharsets.UTF_8));
                System.out.println("Initializing Firebase using FIREBASE_CONFIG_JSON environment variable.");
            } 
            // Priority 2: Use local file (for Local Development)
            else {
                Resource resource = resourceLoader.getResource(serviceAccountPath);
                serviceAccount = resource.getInputStream();
                System.out.println("Initializing Firebase using local file: " + serviceAccountPath);
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK has been initialized successfully.");
            }
        } catch (IOException e) {
            System.err.println("ERROR: Could not initialize Firebase Admin SDK. Please ensure 'service-account.json' exists or FIREBASE_CONFIG_JSON is set.");
            System.err.println("Error details: " + e.getMessage());
        }
    }
}
