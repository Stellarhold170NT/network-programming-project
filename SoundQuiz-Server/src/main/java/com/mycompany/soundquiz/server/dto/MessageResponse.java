/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.dto;

/**
 *
 * @author Admin
 */
public class MessageResponse {
    public static final String FAILED = "failed";
    public static final String SUCCESS = "success";
    
    private String id;
    private String status;
    private String type;
    private String message;

    public MessageResponse() {}

    public MessageResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public MessageResponse(String status, String id, String type, String message) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
