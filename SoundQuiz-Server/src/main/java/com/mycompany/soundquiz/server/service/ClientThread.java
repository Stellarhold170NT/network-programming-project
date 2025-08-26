/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;

import  com.mycompany.soundquiz.server.model.User;
/**
 *
 * @author Admin
 */


import com.google.gson.Gson;
import com.mycompany.soundquiz.server.dto.EmailMessage;
import com.mycompany.soundquiz.server.dto.MessageRequest;
import com.mycompany.soundquiz.server.dto.MessageResponse;
import java.io.*;
import java.net.*;
import java.sql.SQLException;

public class ClientThread extends Thread {
    private UserService userService;
    private MailService mailService;
    private Socket socket = null;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private boolean loggedIn = false;
    
    private static Gson gson = new Gson();
    
    public ClientThread(Socket socket) throws SQLException {
        this.socket = socket;
        this.userService = UserService.getInstance();
        this.mailService = MailService.getInstance();
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String msg;
            while ((msg = in.readLine()) != null) {
                handleMessage(msg);
            }
        } catch (IOException e) {
            System.out.println("Cient disconnected: " + username);
        } finally  {
            close();
        }
    }
    
    private void handleMessage(String msg) {
        try {
            MessageRequest request = gson.fromJson(msg, MessageRequest.class);
            
            switch (request.getType()) {
                case "register":
                    handleRegister(request);
                    break;
                case "login":
                    handleLogin(request);
                    break;
                case "challenge":
                    break;
            }
        } catch (Exception e) {
        }
    }
    
    private void handleRegister(MessageRequest request) {
       String username = request.getUsername();
       String email = request.getEmail();
       String password = request.getPassword();
       
        System.out.println("Dang ky user " + username + " " + email + " " + password);

       
        try {
            if (userService.isUsernameExists(username)) {
                send(MessageResponse.FAILED, "Username đã tồn tại");
            } else if (userService.isEmailExists(email)) {
                send(MessageResponse.FAILED, "Email đã tồn tại");
            } else {
                String codeVerify = userService.generateVerifyCode();
                EmailMessage emailMsg = mailService.sendMail(email, "Xác thực tài khoản của bạn", "Mã xác thực tài khoản của bạn là " + codeVerify);
                
                if (emailMsg.isSuccess()) {
                    send(MessageResponse.SUCCESS, "Da gui ma xac thuc email: " + emailMsg.getMessage());
                    // Đọc kết quả verify code trả về
                    MessageRequest msg = read();
                    System.out.println("Nguoi dung nhap ma: " + msg.getContent());

                    if (msg.getContent().equals(codeVerify)) {
                        userService.register(username, email, password);
                        send(MessageResponse.SUCCESS, "Đăng ký tài khoản thành công!");
                    } else {
                        send(MessageResponse.FAILED, "Đăng ký tài khoản thất bại");
                    }
                }
                else {
                    send(MessageResponse.FAILED, "Đã có lỗi khi gửi mã");
                    
                }
                
            }
        } catch (SQLException e) {
            send(MessageResponse.FAILED, "Có lỗi xảy ra ở phía Server");
            e.printStackTrace();
        }
    }
    
    private void handleLogin(MessageRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();

            User user = userService.login(email, password);
            
            if (user != null) {
                send(MessageResponse.SUCCESS, user.getUsername());
            } else {
                send(MessageResponse.FAILED, "Tên đăng nhập hoặc mật khẩu không chính xác");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void close() {
        try {
            socket.close();
        } catch (IOException ignored) {}
        
    }
    
    public void send(String status, String message) {
        MessageResponse response = new MessageResponse(status, message);
        String json = gson.toJson(response);
        out.println(json);
        System.out.println("Server gửi: " + message);
    }
    
    public MessageRequest read() {
        try {
            String message = in.readLine();
            MessageRequest msg = gson.fromJson(message, MessageRequest.class);
            return msg;
        } catch (IOException e) {
            disconnect();
        } 
        return null;
    }
    
    private void disconnect() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getUsername() {
        return username;
    }
}
