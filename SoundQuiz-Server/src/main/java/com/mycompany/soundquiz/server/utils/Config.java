/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.server.utils;

import java.util.Properties;

import java.io.*;
/**
 *
 * @author Admin
 */
public class Config {
    private static final String CONFIG_PATH = "config/config.properties";
    private static Properties props = new Properties();
    
    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Không thể đọc file cấu hình: " + CONFIG_PATH);
        }
    }
    
    public static String get(String key) {
        return props.getProperty(key);
    }
    
    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(props.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
