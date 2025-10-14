/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.soundquiz.server.utils.Config;
import java.io.IOException;
import java.util.UUID;
import okhttp3.*;
import java.time.Instant;
/**
 *
 * @author Admin
 */
public class NgrokService  {
    private static String ngrok_location;
    private static String ngrok_auth_token;
    private static final OkHttpClient client = new OkHttpClient();

    private static void addAuthToken(String token) throws Exception{
        ProcessBuilder pb = new ProcessBuilder(ngrok_location + "\\ngrok.exe", "config", "add-authtoken", token);
        pb.inheritIO();
        Process p = pb.start();
        p.waitFor();
    }
    
    private static Process startNgrokTcp(int port) throws IOException{
        ProcessBuilder pb = new ProcessBuilder(ngrok_location + "\\ngrok.exe", "tcp", String.valueOf(port));
        pb.inheritIO();
        return pb.start();
    }
    
    public static String getNgrokUrl() throws Exception {
        Request request = new Request.Builder()
                .url("http://127.0.0.1:4040/api/tunnels")
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("Ngrok API failed");
            
            String json = response.body().string();
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            return obj.getAsJsonArray("tunnels")
                    .get(0).getAsJsonObject()
                    .get("public_url").getAsString();
        }
    }
    
    private static void pushToSupabase(String ngrokUrl) throws IOException {
        String supabaseUrl = Config.get("supabase.url");
        String supabaseApiKey = Config.get("supabase.apikey");
        
        JsonObject body = new JsonObject();
        body.addProperty("id", UUID.randomUUID().toString());
        body.addProperty("url", ngrokUrl);
        body.addProperty("created_at", Instant.now().toString());
        
        RequestBody requestBody = RequestBody.create(
                body.toString(), MediaType.get("application/json")
        );
        
        Request request = new Request.Builder()
                .url(supabaseUrl + "/rest/v1/ngrok_url")
                .header("apikey", supabaseApiKey)
                .header("Authorization", "Bearer " + supabaseApiKey)
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Supabase save url failed: " + response);
            }
            System.out.println("Pushed URL to Supabase: " + ngrokUrl);
        }
    }
    
    public static void initNgrokAndPush(int port) throws Exception {
        ngrok_location = Config.get("ngrok.location");
        ngrok_auth_token = Config.get("ngrok.auth.token");
        
        addAuthToken(ngrok_auth_token);
        startNgrokTcp(port);
        
        Thread.sleep(3000);
        
        String ngrokUrl = getNgrokUrl();
        System.out.println("Ngrok TCP URL = " + ngrokUrl);
        
        pushToSupabase(ngrokUrl);
    }
    
}
