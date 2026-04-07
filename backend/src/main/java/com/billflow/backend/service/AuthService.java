package com.billflow.backend.service;

import com.billflow.backend.domain.User;
import com.billflow.backend.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String password, String email, String companyName, String phoneNumber, String idToken) {
        try {
            // Verify Firebase ID Token
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String verifiedPhoneNumber = (String) decodedToken.getClaims().get("phone_number");
            
            // Optional: Check if the verified phone number matches the one provided
            if (verifiedPhoneNumber == null || !verifiedPhoneNumber.contains(phoneNumber.replace(" ", ""))) {
                throw new RuntimeException("Phone number verification failed or mismatch");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired Firebase token: " + e.getMessage());
        }
        
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setCompanyName(companyName);
        user.setPhoneNumber(phoneNumber);

        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
