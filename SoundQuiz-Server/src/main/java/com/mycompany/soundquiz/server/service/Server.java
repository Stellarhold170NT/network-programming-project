/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;

import com.google.gson.Gson;
import com.mycompany.soundquiz.server.dto.MessageResponse;
import com.mycompany.soundquiz.server.utils.Config;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * @author Admin
 */
public class Server {
    private static Server instance;
    private ServerSocket serverSocket;
    private final Map<String, ClientThread> onlinePlayers = new ConcurrentHashMap<>();

    
    private Server() throws IOException{
        String ip = Config.get("server.ip");
        int port = Config.getInt("server.port", 0);
        
        
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        
        new Thread(()-> {
            try {
                System.out.println("Init ngrok");
                NgrokService.initNgrokAndPush(port);
         
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public static Server getInstance() throws IOException {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }
    
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    
    public void stop() {
        try {
            if (serverSocket != null & !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server stopped");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addPlayer(String username, ClientThread handler) {
        System.out.println(username + " " + "tham gia ung dung");
        onlinePlayers.put(username, handler);
        broadcastOnlinePlayers();
    }
    
    public void removePlayer(String username) {
        System.out.println(username + " " + "roi khoi ung dung");
        onlinePlayers.remove(username);
        broadcastOnlinePlayers();
    }

    private void broadcastOnlinePlayers() {
        List<String> players = new ArrayList<>(onlinePlayers.keySet());
        String msg = new Gson().toJson(players);

        for (ClientThread handler : onlinePlayers.values()) {
            System.out.println("Gui moi danh sach nguoi choi");
            handler.sendWithType(MessageResponse.SUCCESS, null, "online_players", msg);
        }
    }
    
    public void sendMessage(String from, String to, String message) {
        ClientThread clientThread = onlinePlayers.get(to);
        clientThread.sendWithType(MessageResponse.SUCCESS, null, "invice_game", message);
    }
    
    public void sendSelfMessage(String type, String to, String message) {
        ClientThread clientThread = onlinePlayers.get(to);
        clientThread.sendWithType(MessageResponse.SUCCESS, null, type, message);
    }
    
    public List<String> loadPlayers() {
        List<String> players = new ArrayList<>(onlinePlayers.keySet());
        return players;
    }
}
