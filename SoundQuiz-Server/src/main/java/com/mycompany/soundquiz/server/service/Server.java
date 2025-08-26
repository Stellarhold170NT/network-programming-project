/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;

import com.mycompany.soundquiz.server.utils.Config;
import java.net.*;
import java.io.*;
/**
 *
 * @author Admin
 */
public class Server {
    private static Server instance;
    private ServerSocket serverSocket;
    
    private Server() throws IOException{
        String ip = Config.get("server.ip");
        int port = Config.getInt("server.port", 0);
        
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
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
}
