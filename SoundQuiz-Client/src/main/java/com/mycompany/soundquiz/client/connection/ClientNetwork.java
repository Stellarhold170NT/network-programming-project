/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.client.connection;

import com.google.gson.Gson;
import com.mycompany.soundquiz.client.dto.MessageRequest;
import com.mycompany.soundquiz.client.dto.MessageResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Admin
 */
public class ClientNetwork {
    private static ClientNetwork instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson = new Gson();
    
    private ClientNetwork() throws IOException {
        this.socket = ClientConnection.getInstance().getSocket();
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
    }
    
    public static ClientNetwork getInstance() throws  IOException {
        if (instance == null) {
            instance = new ClientNetwork();
        }
        return instance;
    }
    
    public void sendMessage(MessageRequest request) {
        String json = gson.toJson(request);
        out.println(json);
    }
    
    public MessageResponse readMessage() {
        try {
            String line = in.readLine(); // đọc 1 dòng JSON
            if (line != null) {
                return gson.fromJson(line, MessageResponse.class);
            }
        } catch (IOException e) {
        }
        
        return new MessageResponse(MessageResponse.FAILED, "");
    }
    
    
    public void startListening(MessageHandler handler) {
        new Thread(() -> {
            try {
                System.out.println("Bat dau lang nghe server");
                String line;
                while ((line = in.readLine()) != null) {
                    MessageResponse response = gson.fromJson(line, MessageResponse.class);
                    System.out.println("startListening: " + response.getType() + " " + response.getMessage());
                    handler.handle(response); // callback xử lý
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server: " + e.getMessage());
            }
        }).start();
    }
}
