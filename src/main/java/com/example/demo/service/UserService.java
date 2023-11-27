package com.example.demo.service;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;


    public User registerUser(User user) {
        if (user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Incorrect username");
        }
        if (user.getPassword() == null || !isPasswordValid(user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }
        if (user.getEmail() == null || !isEmailValid(user.getEmail())) {
            throw new IllegalArgumentException("Incorrect email");
        }
        isValueUnique(user.getUsername(), "username");
        isValueUnique(user.getEmail(), "email");

        user.setRole(assignRole(user.getEmail()));
        return userRepository.save(user);
    }

    public Long authenticateUser(String usernameOrEmail, String password) {
        Long userId;

        if (usernameOrEmail.contains("@")) {
            userId = getIdByName(usernameOrEmail, "email");
        } else {
            userId = getIdByName(usernameOrEmail, "username");
        }


        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found"));

        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user.getId();
    }


    private boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    private boolean isEmailValid(String email) {

        Pattern pattern = Pattern.compile("^(.+)@(\\S+)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void isValueUnique(String value, String fieldName) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u." + fieldName + " = :value";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("value", value);

        long count = (long) query.getSingleResult();
        if (count > 0) {
            throw new IllegalArgumentException(fieldName + " exists");
        }
    }
    private Long getIdByName(String value, String fieldName) {
        String jpql = "SELECT u.id FROM User u WHERE u." + fieldName + " = :value";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("value", value);

        List<Long> userIds = query.getResultList();

        if (userIds.isEmpty()) {
            throw new IllegalArgumentException(fieldName + "not found");
        }
        return userIds.get(0);
    }

    private String assignRole(String email) {
        if(email.contains("@ServiceAdmin.pl")){
            return "ADMIN";
        } else {
            return "GUEST";
        }
    }



}
