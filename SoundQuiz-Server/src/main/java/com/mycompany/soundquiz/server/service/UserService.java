/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import com.mycompany.soundquiz.server.model.User;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;
/**
 *
 * @author Admin
 */
public class UserService {
    private static UserService userService;
    private DatabaseService dbService;
    
    private UserService() throws SQLException {
        this.dbService = DatabaseService.getInstance();
    }
    
    public static UserService getInstance() throws SQLException {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }
    
    public boolean register(String username, String email, String password) throws SQLException {
        if (isUsernameExists(username) || isEmailExists(email)) {
            return false;
        }
        
        String hashed = hashPassword(password);
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        int rows = dbService.executeUpdate(sql, username, email, hashed);
        return rows > 0;
    }
    
    public User login(String usernameOrEmail, String password) throws SQLException {
        System.out.println("login: "+ usernameOrEmail + " " + password);
        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";
        Map<String, Object> row = dbService.queryOne(sql, usernameOrEmail, usernameOrEmail);

        if (row == null) return null;
        System.out.println("Ton tai user");
        String storedHash = (String) row.get("password");
        if (verifyPassword(password, storedHash)) {
            return mapToUser(row);
        }
        else {
            System.out.println("Mat khau khong trung ??");
        }
        return null;
    }
    
    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";
        return dbService.queryOne(sql, username) != null;
    }

    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ?";
        return dbService.queryOne(sql, email) != null;
    }
    
    
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Không tìm thấy thuật toán hash", e);
        }
    }
    
    public boolean verifyPassword(String password, String hashed) {
        System.out.println(hashPassword(password) + " | " + hashed);
        return hashPassword(password).equals(hashed);
    }
    
    private User mapToUser(Map<String, Object> row) {
        User u = new User();
        u.setId((Integer) row.get("id"));
        u.setUsername((String) row.get("username"));
        u.setPassword((String) row.get("password"));
        u.setTotalPoints((Integer) row.getOrDefault("total_points", 0));
        u.setTotalWins((Integer) row.getOrDefault("total_wins", 0));
        u.setTotalGames((Integer) row.getOrDefault("total_games", 0));
        u.setCreatedAt(row.get("created_at") != null ? ((java.sql.Timestamp) row.get("created_at")).toLocalDateTime() : null);
        return u;
    }
    
    public String generateVerifyCode() {
        DecimalFormat df = new DecimalFormat("000000");
        Random ran = new Random();
        String code = df.format(ran.nextInt(1000000));  //  Random from 0 to 999999
        return code;
    }
}
