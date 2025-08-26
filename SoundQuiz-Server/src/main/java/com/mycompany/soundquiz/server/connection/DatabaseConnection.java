/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.connection;

import com.mycompany.soundquiz.server.utils.Config;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private DatabaseConnection() {

    }

    public void connectToDatabase() throws SQLException {
        String host = Config.get("db.host");
        String port = Config.get("db.port");
        String database = Config.get("db.name");
        String userName = Config.get("db.username");
        String password = Config.get("db.password");
        connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, userName, password);
    }

    public Connection getConnection() throws SQLException{
        connectToDatabase();
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2); 
        } catch (SQLException e) {
            return false;
        }
    }
}
