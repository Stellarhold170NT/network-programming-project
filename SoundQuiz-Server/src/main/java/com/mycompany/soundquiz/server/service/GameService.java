/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;
import com.google.gson.Gson;
import com.mycompany.soundquiz.server.model.User;
import com.mycompany.soundquiz.server.model.Game_Room;
import com.mycompany.soundquiz.server.model.Player;
import java.util.*;


import java.sql.SQLException;


/**
 *
 * @author Admin
 */
public class GameService {
    private static GameService instance;
    private final DatabaseService dbService;
    private final UserService userService;

    private GameService() throws Exception {
        this.dbService = DatabaseService.getInstance();
        this.userService = UserService.getInstance();
    }

    public static GameService getInstance() throws Exception {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }
    
    /**
     * Tạo một phòng game mới
     * @param username Tên người dùng tạo phòng
     * @param roomName Tên phòng
     * @param listSong Danh sách bài hát (dạng JSON string hoặc định dạng khác)
     * @param description Mô tả phòng
     * @param question Câu hỏi
     * @return ID của phòng vừa tạo, hoặc -1 nếu có lỗi
     */
    public Game_Room createRoom(String username, String roomName, String listSong, String description, String question) {
        try {
            // Tìm user để lấy user_id
            User user = userService.findUserByUsername(username);
            if (user == null) {
                return null; // User không tồn tại
            }

            // Tạo phòng game mới và lấy ID được tạo (thread-safe)
            String sql = "INSERT INTO game_room (name, description, listSong, question) VALUES (?, ?, ?, ?)";
            int roomId = dbService.executeInsert(sql, roomName, description, listSong, question);
            
            Game_Room game_room = getRoomById(roomId);
            if (roomId > 0) {
                // Thêm người tạo phòng vào phòng
                addPlayer(roomId, user.getId());
                return game_room;
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Lấy thông tin phòng dựa trên ID
     * @param roomId ID của phòng
     * @return Đối tượng Game_Room hoặc null nếu không tìm thấy
     */
    public Game_Room getRoomById(int roomId) {
        try {
            String sql = "SELECT * FROM game_room WHERE id = ?";
            Map<String, Object> result = dbService.queryOne(sql, roomId);
            
            if (result == null) {
                return null;
            }
            
            Game_Room room = new Game_Room();
            room.setId(((Number) result.get("id")).intValue());
            room.setName((String) result.get("name"));
            room.setDescription((String) result.get("description"));
            room.setListSong((String) result.get("listSong"));
            room.setQuestion((String) result.get("question"));
            
            return room;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Thêm người chơi vào phòng (dùng user_id)
     * @param roomId ID của phòng
     * @param userId ID của người dùng
     * @return true nếu thêm thành công, false nếu có lỗi
     */
    public boolean addPlayer(int roomId, int userId) {
        try {
            // Kiểm tra phòng có tồn tại không
            Game_Room room = getRoomById(roomId);
            if (room == null) {
                return false;
            }

            // Kiểm tra người dùng có tồn tại không
            User user = userService.findUserById(userId);
            if (user == null) {
                return false;
            }

            // Kiểm tra người dùng đã tham gia phòng này chưa
            String checkSql = "SELECT * FROM player WHERE room_id = ? AND user_id = ?";
            Map<String, Object> existingPlayer = dbService.queryOne(checkSql, roomId, userId);

            if (existingPlayer != null) {
                return true; // Người dùng đã ở trong phòng
            }

            // Thêm người dùng vào phòng
            String sql = "INSERT INTO player (room_id, user_id) VALUES (?, ?)";
            int result = dbService.executeUpdate(sql, roomId, userId);
            
            List<User> lst = getPlayersInRoom(roomId);
            List<String> sendLst = new ArrayList<>();
            for (User u : lst) {
                sendLst.add(u.getUsername());
            }
            Gson gson = new Gson();
            for (User u : lst) {
                Server.getInstance().sendSelfMessage("attend_players", u.getUsername(), gson.toJson(sendLst));
            }

            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Thêm người chơi vào phòng (dùng username)
     * @param roomId ID của phòng
     * @param username Tên người dùng
     * @return true nếu thêm thành công, false nếu có lỗi
     */
    public boolean addPlayer(int roomId, String username) {
        try {
            // Kiểm tra người dùng có tồn tại không
            User user = userService.findUserByUsername(username);
            if (user == null) {
                return false;
            }

            // Gọi phương thức addPlayer với userId
            return addPlayer(roomId, user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lấy danh sách người chơi trong một phòng
     * @param roomId ID của phòng
     * @return Danh sách các đối tượng User đang tham gia phòng
     */
    public List<User> getPlayersInRoom(int roomId) {
        List<User> players = new ArrayList<>();
        
        try {
            // Kiểm tra phòng có tồn tại không
            Game_Room room = getRoomById(roomId);
            if (room == null) {
                return players;
            }
            
            // Lấy danh sách ID người dùng trong phòng
            String sql = "SELECT user_id FROM player WHERE room_id = ?";
            List<Map<String, Object>> results = dbService.executeQuery(sql, roomId);
            
            for (Map<String, Object> result : results) {
                int userId = ((Number) result.get("user_id")).intValue();
                User user = userService.findUserById(userId);
                if (user != null) {
                    players.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return players;
    }
    
    /**
     * Lấy danh sách tất cả các phòng game
     * @return Danh sách các đối tượng Game_Room
     */
    public List<Game_Room> getAllRooms() {
        List<Game_Room> rooms = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM game_room";
            List<Map<String, Object>> results = dbService.executeQuery(sql);
            
            for (Map<String, Object> result : results) {
                Game_Room room = new Game_Room();
                room.setId(((Number) result.get("id")).intValue());
                room.setName((String) result.get("name"));
                room.setDescription((String) result.get("description"));
                room.setListSong((String) result.get("listSong"));
                room.setQuestion((String) result.get("question"));
                
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rooms;
    }
    
    /**
     * Xóa người chơi khỏi phòng
     * @param roomId ID của phòng
     * @param userId ID của người dùng
     * @return true nếu xóa thành công, false nếu có lỗi
     */
    public boolean removePlayer(int roomId, int userId) {
        try {
            String sql = "DELETE FROM player WHERE room_id = ? AND user_id = ?";
            int result = dbService.executeUpdate(sql, roomId, userId);
            
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa phòng game
     * @param roomId ID của phòng
     * @return true nếu xóa thành công, false nếu có lỗi
     */
    public boolean deleteRoom(int roomId) {
        try {
            // Xóa tất cả người chơi trong phòng trước
            String deletePlayersSql = "DELETE FROM player WHERE room_id = ?";
            dbService.executeUpdate(deletePlayersSql, roomId);
            
            // Xóa phòng
            String deleteRoomSql = "DELETE FROM game_room WHERE id = ?";
            int result = dbService.executeUpdate(deleteRoomSql, roomId);
            
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Cập nhật thông tin phòng
     * @param room Đối tượng Game_Room chứa thông tin cập nhật
     * @return true nếu cập nhật thành công, false nếu có lỗi
     */
    public boolean updateRoom(Game_Room room) {
        try {
            String sql = "UPDATE game_room SET name = ?, description = ?, listSong = ?, question = ? WHERE id = ?";
            int result = dbService.executeUpdate(sql, room.getName(), room.getDescription(), room.getListSong(), room.getQuestion(), room.getId());
            
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}