/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.client.dto;

/**
 *
 * @author Admin
 */
public class MessageRequest {
    private String id;
    private String type;
    private String username;
    private String email;
    private String password;
    private String content;

    public MessageRequest() {
    }

    public MessageRequest(String type, String username, String password, String content) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setId(String id) {
        this.id = id;
    }

}
