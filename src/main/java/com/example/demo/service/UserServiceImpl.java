package com.example.demo.service;

import com.example.demo.interfaces.UserService;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Value("${security.password.pepper}")
    private String pepper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AuthServiceImpl authService;

    public String getHashedPassword(String password, String salt) {
        String hashedPassword = password + pepper + salt;
        hashedPassword = authService.encryptPassword(hashedPassword);
        return hashedPassword;
    }

    public User registerUser(User user) {
        if (user.getUsername().length() < 3 || user.getUsername().contains("@")) {
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
        String salt = authService.generateSalt();
        user.setSalt(salt);

        String hashedPassword = getHashedPassword(user.getPassword(), salt);
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public Long loginUser(String usernameOrEmail, String password) {
        Long userId;
        if (usernameOrEmail.contains("@")) {
            userId = getIdByName(usernameOrEmail, "email");
        } else {
            userId = getIdByName(usernameOrEmail, "username");
        }
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new RuntimeException("User not found"));

        String salt = user.getSalt();
        String hashedPassword = getHashedPassword(password, salt);

        if (Objects.equals(user.getPassword(), hashedPassword)) {
            return user.getId();
        } else {
            throw new RuntimeException("invalid credentials");
        }
    }

    public boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isEmailValid(String email) {
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

    String assignRole(String email) {
        if (email.contains("@ServiceAdmin.pl")) {
            return "ADMIN";
        } else {
            return "GUEST";
        }
    }
}
