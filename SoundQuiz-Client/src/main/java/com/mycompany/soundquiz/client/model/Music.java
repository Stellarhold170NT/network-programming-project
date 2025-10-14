/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.client.model;

/**
 *
 * @author Admin
 */
public class Music {
    private int id;
    private String title;
    private String file_name;
    private String image;
    private int mood_id;
    
    // Constructor mặc định
    public Music() {
    }
    
    // Constructor đầy đủ tham số
    public Music(int id, String title, String file_name, String image, int mood_id) {
        this.id = id;
        this.title = title;
        this.file_name = file_name;
        this.image = image;
        this.mood_id = mood_id;
    }
    
    // Getter và Setter
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
    
    public String getFileName() {
        return file_name;
    }
    
    public void setFileName(String file_name) {
        this.file_name = file_name;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }
    
    public int getMoodId() {
        return mood_id;
    }
    
    public void setMoodId(int mood_id) {
        this.mood_id = mood_id;
    }
    
    // toString method
    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", file_name='" + file_name + '\'' +
                ", image='" + image + '\'' +
                ", mood_id=" + mood_id +
                '}';
    }
    
    
}
