/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.client.connection;

/**
 *
 * @author Admin
 */
import java.io.IOException;
import java.net.Socket;

public class ClientConnection {
    private static ClientConnection instance;
    private Socket socket;

    private ClientConnection() {}

    public static ClientConnection getInstance() {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    public void connect(String ip, int port) throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(ip, port);
        }
    }
    
    public void connectViaNgrok(String ngrokUrl) throws IOException {
        String noScheme = ngrokUrl.replace("tcp://", "");
        String[] parts = noScheme.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        socket = new Socket(host, port);
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
            socket = null;
        }
    }
}

