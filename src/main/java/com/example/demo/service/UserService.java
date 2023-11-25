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
        String password = user.getPassword();
        if (password == null || !isPasswordValid(user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }
        isUsernameUnique(user.getUsername());
        return userRepository.save(user);
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8) return false;
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    private void isUsernameUnique(String username) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.username = :username";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("username", username);

        long count = (long) query.getSingleResult();
        if (count > 0) {
            throw new IllegalArgumentException("Username exists");
        }
    }



}
