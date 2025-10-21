/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.client.model;

/**
 *
 * @author Admin
 */
public class Game_Room {
    private int id;
    private String name;
    private String description;
    private String listSong;
    private String question;

    public Game_Room() {}

    public Game_Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Game_Room(int id, String name, String description, String listSong, String question) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.listSong = listSong;
        this.question = question;
    }

    // Getter và Setter cho id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter và Setter cho listSong
    public String getListSong() {
        return listSong;
    }

    public void setListSong(String listSong) {
        this.listSong = listSong;
    }

    // Getter và Setter cho question
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Game_Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", listSong='" + listSong + '\'' +
                ", question='" + question + '\'' +
                '}';
    }
}

