/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.model;

import java.time.LocalDateTime;

/**
 *
 * @author Admin
 */
public class User {
    private int id;
    private String username;
    private String password;
    private int totalPoints;
    private int totalWins;
    private int totalGames;
    private LocalDateTime createdAt;

    public User() {}

    public User(int id, String username, String password, int totalPoints,
                int totalWins, int totalGames, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.totalPoints = totalPoints;
        this.totalWins = totalWins;
        this.totalGames = totalGames;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }

    public int getTotalWins() { return totalWins; }
    public void setTotalWins(int totalWins) { this.totalWins = totalWins; }

    public int getTotalGames() { return totalGames; }
    public void setTotalGames(int totalGames) { this.totalGames = totalGames; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", totalPoints=" + totalPoints +
                ", totalWins=" + totalWins +
                ", totalGames=" + totalGames +
                ", createdAt=" + createdAt +
                '}';
    }
}