/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.service;

import com.mycompany.soundquiz.server.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
 *
 * @author Admin
 */
public class DatabaseService {
    private static DatabaseService dbService;
    private final DatabaseConnection dbConnection;
    
    private DatabaseService() throws SQLException {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public static DatabaseService getInstance() throws SQLException {
        if (dbService == null) {
            dbService = new DatabaseService();
        }
        
        return dbService;
    }
    
    public List<Map<String, Object>> executeQuery(String sql, Object... params) throws  SQLException {
        try (Connection conn = dbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            setParameters(stmt, params);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return convertResultSetToList(rs);
            }
        }
    }
    
    public Map<String, Object> queryOne(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = executeQuery(sql, params);
        return list.isEmpty() ? null : list.get(0);
    }
    
    // dành cho INSERT/UPDATE/DELETE
    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = dbConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            setParameters(stmt, params);
            return stmt.executeUpdate();
        }
    }
    
    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }
    
    private List<Map<String, Object>> convertResultSetToList(ResultSet rs) throws  SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            rows.add(row);
        }
        
        return rows;
     
    }
    
}
