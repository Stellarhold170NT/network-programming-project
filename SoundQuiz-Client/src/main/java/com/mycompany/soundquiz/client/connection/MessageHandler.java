/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.soundquiz.client.connection;

import com.mycompany.soundquiz.client.dto.MessageResponse;

/**
 *
 * @author Admin
 */
public interface MessageHandler {
    public void handle(MessageResponse response);
}