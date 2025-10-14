/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.model;

/**
 *
 * @author Admin
 */
public class Music {
    private int id;
    private String title;
    private String file_name;
    private int mood_id;

    public Music() {}

    public Music(int id, String title, String file_name, int mood_id) {
        this.id = id;
        this.title = title;
        this.file_name = file_name;
        this.mood_id = mood_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getMood_id() {
        return mood_id;
    }

    public void setMood_id(int mood_id) {
        this.mood_id = mood_id;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", file_name='" + file_name + '\'' +
                ", mood_id=" + mood_id +
                '}';
    }
}

