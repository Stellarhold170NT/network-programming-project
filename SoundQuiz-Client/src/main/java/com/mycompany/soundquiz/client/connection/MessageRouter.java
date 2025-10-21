/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.client.connection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.mycompany.soundquiz.client.dto.MessageResponse;

/**
 *
 * @author Admin
 */

public class MessageRouter {

    private static MessageRouter instance;

    // Broadcast handlers theo type (ví dụ online_players)
    private final Map<String, MessageHandler> broadcastHandlers = new ConcurrentHashMap<>();

    // Single-request handlers theo requestId
    private final Map<String, MessageHandler> requestHandlers = new ConcurrentHashMap<>();

    private MessageRouter() {}

    public static MessageRouter getInstance() {
        if (instance == null) {
            synchronized (MessageRouter.class) {
                if (instance == null) instance = new MessageRouter();
            }
        }
        return instance;
    }

    /** Broadcast */
    public void registerBroadcastHandler(String type, MessageHandler handler) {
        broadcastHandlers.put(type, handler);
    }

    public void unregisterBroadcastHandler(String type) {
        broadcastHandlers.remove(type);
    }

    /** Single-request */
    public void registerRequestHandler(String requestId, MessageHandler handler) {
        requestHandlers.put(requestId, handler);
    }
   

    public void unregisterRequestHandler(String requestId) {
        requestHandlers.remove(requestId);
    }

    /** Route incoming response */
    public void routeMessage(MessageResponse response) {
        // 1. Broadcast
        System.out.println("routeMessage " + response.getType());
        MessageHandler bHandler = broadcastHandlers.get(response.getType());
        if (bHandler != null) {
            bHandler.handle(response);
            return;
        }

        // 2. Single-request (nếu message chứa requestId)
        try {
//            JsonObject obj = JsonParser.parseString(response.getMessage()).getAsJsonObject();
            if (response.getId() != null) {
                String reqId = response.getId();
                MessageHandler sHandler = requestHandlers.get(reqId);
                if (sHandler != null) {
                    sHandler.handle(response);
                    requestHandlers.remove(reqId); // remove sau khi xử lý
                }
            }
        } catch (Exception ignored) {
            // không phải JSON hoặc không có requestId, bỏ qua
        }
    }
}
