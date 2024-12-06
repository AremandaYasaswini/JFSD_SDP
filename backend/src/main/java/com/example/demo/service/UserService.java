package com.example.demo.service;


import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public User saveUser(User user) {
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("BUYER");  // Default role if not provided
        }
        return userRepository.save(user);  // Ensure this is saving the role correctly
    }



    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public String login(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return existingUser.getRole(); 
    }

}
