package com.example.demo.service;

import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    public User registerUser(User user) {
//        String password = user.getPassword();
//        String email = user.getEmail();
        if (user.getPassword() == null || !isPasswordValid(user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }
        if (user.getEmail() == null || !isEmailValid(user.getEmail())) {
            throw new IllegalArgumentException("Incorrect email");
        }
        isValueUnique(user.getUsername(), "username");
        isValueUnique(user.getEmail(), "email");
        return userRepository.save(user);
    }

    private boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
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



}
