/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.dto;

/**
 *
 * @author Admin
 */
public class EmailMessage {
    private boolean success;
    private String message;
    
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EmailMessage(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public EmailMessage() {
    }

   
}