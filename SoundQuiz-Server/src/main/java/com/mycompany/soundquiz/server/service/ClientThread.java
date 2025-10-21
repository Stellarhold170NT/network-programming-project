/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;

import  com.mycompany.soundquiz.server.model.User;
import  com.mycompany.soundquiz.server.model.Music;
import  com.mycompany.soundquiz.server.model.Game_Room;

/**
 *
 * @author Admin
 */


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.soundquiz.server.dto.EmailMessage;
import com.mycompany.soundquiz.server.dto.MessageRequest;
import com.mycompany.soundquiz.server.dto.MessageResponse;
import java.io.*;
import java.net.*;
import java.util.*;
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
                case "getSongs":  // request 1: lấy bài theo mood
                    handleGetSongs(request);
                    break;
                case "getSegment": // request 2: lấy 5 giây ngẫu nhiên
                    handleGetSegment(request);
                    break;
                case "load_players":
                    handleLoadPlayers(request);
                    break;
                case "ranking_players":
                    handleRankingPlayers(request);
                    break;
                case "invice_user":
                    handleInviceUser(request);
                    break;
                case "accept":
                    handleAccept(request);
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
                sendWithType(MessageResponse.FAILED, request.getId(), request.getType(), "Username đã tồn tại");
            } else if (userService.isEmailExists(email)) {
                sendWithType(MessageResponse.FAILED, request.getId(), request.getType(),  "Email đã tồn tại");
            } else {
                String codeVerify = userService.generateVerifyCode();
                System.out.println("codeVerify = " + codeVerify);
                EmailMessage emailMsg = mailService.sendMail(email, "Xác thực tài khoản của bạn", "Mã xác thực tài khoản của bạn là " + codeVerify);
                
                if (emailMsg.isSuccess()) {
                    sendWithType(MessageResponse.SUCCESS,  request.getId(), request.getType(), "Da gui ma xac thuc email: " + emailMsg.getMessage());
                    // Đọc kết quả verify code trả về
                    MessageRequest msg = read();
                    System.out.println("Nguoi dung nhap ma: " + msg.getContent());

                    if (msg.getContent().equals(codeVerify)) {
                        userService.register(username, email, password);
                        sendWithType(MessageResponse.SUCCESS, msg.getId(), msg.getType(),  "Đăng ký tài khoản thành công!");
                    } else {
                        sendWithType(MessageResponse.FAILED, msg.getId(), msg.getType(),  "Đăng ký tài khoản thất bại");
                    }
                }
                else {
                    sendWithType(MessageResponse.FAILED, request.getId(), request.getType(), "Đã có lỗi khi gửi mã");
                    
                }
                
            }
        } catch (SQLException e) {
            sendWithType(MessageResponse.FAILED, request.getId(), request.getType(), "Có lỗi xảy ra ở phía Server");
            e.printStackTrace();
        }
    }
    
    private void handleLogin(MessageRequest request) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();
            System.out.println(email + " " + password);
            User user = userService.login(email, password);
            
            if (user != null) {
                sendWithType(MessageResponse.SUCCESS, request.getId(), request.getType(), user.getUsername());
                Server.getInstance().addPlayer(user.getUsername(), this);
                this.username = user.getUsername();
            } else {
                sendWithType(MessageResponse.FAILED, request.getId(), request.getType(),"Tên đăng nhập hoặc mật khẩu không chính xác");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleLoadPlayers(MessageRequest request) {
        try {
            List<String> result = Server.getInstance().loadPlayers();
            String msg = new Gson().toJson(result);
            
            sendWithType(MessageResponse.SUCCESS, request.getId(), request.getType(), msg);
            
        } catch (Exception e) {
            
        }
    }
    
    private void handleRankingPlayers(MessageRequest request) {
        try {
            List<String> result = userService.rankingPlayers();
            String msg = new Gson().toJson(result);
            
            sendWithType(MessageResponse.SUCCESS, request.getId(), request.getType(), msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void close() {
        try {
            Server.getInstance().removePlayer(username);
            socket.close();
        } catch (IOException ignored) {}
        
    }
    
    public void send(String status, String message) {
        MessageResponse response = new MessageResponse(status, message);
        String json = gson.toJson(response);
        out.println(json);
        System.out.println("Server gửi: " + message);
    }
    
    public void sendWithType(String status, String id, String type, String message) {
        MessageResponse response = new MessageResponse(status, id, type, message);
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
    
    private String extractMoodsFromText(String userInput) throws Exception {
        // Lấy danh sách mood từ DB
        List<Map<String, Object>> moods = MusicService.getInstance().getAllMoods();

        // Chuyển danh sách mood thành string dạng "id: tên"
        StringBuilder moodListStr = new StringBuilder();
        for (Map<String, Object> mood : moods) {
            moodListStr.append(mood.get("id")).append(":").append(mood.get("name")).append(", ");
        }
        if (moodListStr.length() > 2) {
            moodListStr.setLength(moodListStr.length() - 2); // bỏ dấu ", " cuối
        }

        // Tạo prompt gửi cho Gemini
        String prompt = "Extract moods from this text: \"" + userInput + "\". "
                + "Available moods are: [" + moodListStr + "]. "
                + "Return only JSON object exactly like {\"moodIds\":[1,3,5],\"quantity\":2} "
                + "Do not add any explanation or extra text.";

        // Gọi Gemini
        String response = GeminiService.chat(prompt);
        System.out.println("extractMoodsFromText: " + response);

        String jsonOnly = response;
        int start = response.indexOf("{");
        int end = response.lastIndexOf("}");
        if (start >= 0 && end >= 0 && end > start) {
            jsonOnly = response.substring(start, end + 1);
        }
        System.out.println("JSON only: " + jsonOnly);

        // VALIDATE: Kiểm tra JSON có hợp lệ không trước khi return
        try {
            Map<String, Object> test = gson.fromJson(jsonOnly, Map.class);
            if (!test.containsKey("moodIds") || !test.containsKey("quantity")) {
                throw new RuntimeException("Missing required fields in JSON");
            }
        } catch (Exception e) {
            System.err.println("Invalid JSON from Gemini: " + jsonOnly);
            // Fallback JSON khi parse lỗi
            jsonOnly = "{\"moodIds\":[1],\"quantity\":5}";
        }

        return jsonOnly;
    }
    
    private String summary(String listSong) {
        String prompt = "Danh sách bài hát: " + listSong
                + ". Viết TRỰC TIẾP một đoạn giới thiệu mơ hồ (không nói thẳng vào bài hát) "
                + "kiểu mơ mộng, khoảng 60 từ. "
                + "KHÔNG thêm lời mở đầu, KHÔNG thêm giải thích, CHỈ trả về nội dung đoạn văn.";

        // Gọi Gemini
        String response = GeminiService.chat(prompt).trim();

        // Loại bỏ phần giải thích thừa nếu có (ví dụ: "Tuyệt vời, đây là...:")
        // Nếu có dấu ":", lấy phần sau dấu ":" cuối cùng
        int lastColonIndex = response.lastIndexOf(":");
        if (lastColonIndex > 0 && lastColonIndex < response.length() - 10) {
            // Có dấu ":" và sau nó còn nhiều text (>10 ký tự) => có thể là phần giải thích thừa
            String afterColon = response.substring(lastColonIndex + 1).trim();
            if (afterColon.length() > 50) { // Nếu phần sau dài đủ
                response = afterColon;
            }
        }

        // Loại bỏ dấu ngoặc kép nếu có bao quanh
        if (response.startsWith("\"") && response.endsWith("\"")) {
            response = response.substring(1, response.length() - 1);
        }

        return response.trim();
    }
    
    private String createQuestion(String listSong) {
        String prompt = "Bài hát: " + listSong
                + ". Hãy tạo một câu hỏi trắc nghiệm về TÊN BÀI HÁT. "
                + "Câu hỏi phải là 'Tên bài hát là gì?' hoặc tương tự. "
                + "Đáp án đúng (answer) phải là tên bài hát chính xác từ dữ liệu đã cho. "
                + "3 đáp án còn lại phải là tên bài hát giả tưởng nhưng hợp lý (không trùng tên thật). "
                + "Trả về ĐÚNG định dạng JSON (KHÔNG thêm markdown ```json), ví dụ: "
                + "{\"title\": \"Tên bài hát là gì?\", "
                + "\"A\": \"Tên bài đúng\", "
                + "\"B\": \"Tên bài giả 1\", "
                + "\"C\": \"Tên bài giả 2\", "
                + "\"D\": \"Tên bài giả 3\", "
                + "\"answer\": \"A\"}";

        String response = GeminiService.chat(prompt);

        // Loại bỏ markdown wrapper ```json ... ``` nếu có
        String jsonOnly = response.trim();
        if (jsonOnly.startsWith("```json")) {
            jsonOnly = jsonOnly.substring(7); // Bỏ ```json
        } else if (jsonOnly.startsWith("```")) {
            jsonOnly = jsonOnly.substring(3); // Bỏ ```
        }
        if (jsonOnly.endsWith("```")) {
            jsonOnly = jsonOnly.substring(0, jsonOnly.length() - 3); // Bỏ ``` cuối
        }
        jsonOnly = jsonOnly.trim();

        return jsonOnly;
    }

    private void handleGetSongs(MessageRequest request) {
        try {
            String content = extractMoodsFromText(request.getContent());
            // Content là JSON string: {"moodIds":[1,3,5],"quantity":10}
            Map<String, Object> data = gson.fromJson(content, Map.class);

            // Xử lý moodIds - hỗ trợ cả List<Double> và List<Integer>
            List<Integer> moodIds = new ArrayList<>();
            Object moodIdsObj = data.get("moodIds");

            if (moodIdsObj instanceof List) {
                List<?> moodList = (List<?>) moodIdsObj;
                for (Object item : moodList) {
                    if (item instanceof Double) {
                        moodIds.add(((Double) item).intValue());
                    } else if (item instanceof Integer) {
                        moodIds.add((Integer) item);
                    } else if (item instanceof Long) {
                        moodIds.add(((Long) item).intValue());
                    }
                }
            }

            // Xử lý quantity - hỗ trợ cả Double và Integer
            int quantity;
            Object quantityObj = data.get("quantity");
            if (quantityObj instanceof Double) {
                quantity = ((Double) quantityObj).intValue();
            } else if (quantityObj instanceof Integer) {
                quantity = (Integer) quantityObj;
            } else if (quantityObj instanceof Long) {
                quantity = ((Long) quantityObj).intValue();
            } else {
                quantity = 5; // default
            }

            MusicService musicService = MusicService.getInstance();
            List<Map<String, Object>> songs = musicService.getRandomSongs(moodIds, quantity);
            
            String summarySong = summary(gson.toJson(songs));
            String questionSong = createQuestion(gson.toJson(songs));
            
            Game_Room game_room = GameService.getInstance().createRoom(username, "", gson.toJson(songs), summarySong, questionSong);
            

            sendWithType(MessageResponse.SUCCESS, request.getId(), "getSongs", gson.toJson(game_room));

        } catch (Exception e) {
            e.printStackTrace();
            sendWithType(MessageResponse.FAILED, request.getId(), "getSongs", "Lỗi khi lấy bài hát: " + e.getMessage());
        }
    }
    

    
    private void handleGetSegment(MessageRequest request) {
        String content = request.getContent();
        try {
            // Content là JSON string: {"songFile":"tinh-ve.wav","segmentSeconds":5}
            Map<String, Object> data = gson.fromJson(content, Map.class);
            String songFile = (String) data.get("songFile");
            int segmentSeconds = ((Double) data.get("segmentSeconds")).intValue();

            MusicService musicService = MusicService.getInstance();

            // Load WAV từ resources
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("audio/" + songFile)) {
                if (is == null) {
                    sendWithType(MessageResponse.FAILED, request.getId(), "getSegment", "Không tìm thấy file " + songFile);
                    return;
                }

                // MusicService cần phiên bản cutRandomWavSegment nhận InputStream
                byte[] segmentData = musicService.cutRandomWavSegment(is, segmentSeconds);

                // gửi Base64 qua JSON
                String base64Data = Base64.getEncoder().encodeToString(segmentData);
                sendWithType(MessageResponse.SUCCESS, request.getId(), "getSegment", base64Data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendWithType(MessageResponse.FAILED, request.getId(), "getSegment", "Lỗi khi lấy đoạn nhạc");
        }
    }
    
    private void handleInviceUser(MessageRequest request) {
        System.out.println("handleInviceUser call");
        try {
            JsonObject obj = JsonParser.parseString(request.getContent()).getAsJsonObject();
            int roomId = obj.get("roomId").getAsInt();
            String inviceUser = obj.get("invitee").getAsString();
            String fromUser = request.getUsername();
            Gson gson = new Gson();
            
            
            JsonObject objReturn = new JsonObject();
            objReturn.addProperty("fromUser", fromUser);
            objReturn.addProperty("room", gson.toJson(GameService.getInstance().getRoomById(roomId)));
            Server.getInstance().sendMessage(fromUser, inviceUser, objReturn.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void handleAccept(MessageRequest request) {
        System.out.println("handleAccept call");
        try {
            GameService.getInstance().addPlayer(Integer.parseInt(request.getContent()), request.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
