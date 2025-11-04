/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.model;

/**
 *
 * @author Admin
 */
public class Player {
    private int id;
    private int room_id;
    private int user_id;
    private int point;

    public Player() {}

    public Player(int id, int room_id, int user_id) {
        this.id = id;
        this.room_id = room_id;
        this.user_id = user_id;
        this.point = 0;
    }

    // Getter và Setter cho id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return this.point;
    }

    // Getter và Setter cho room_id
    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    // Getter và Setter cho user_id
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", room_id=" + room_id +
                ", user_id=" + user_id +
                '}';
    }
}


