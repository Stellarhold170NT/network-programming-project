/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.soundquiz.client.view;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.soundquiz.client.connection.ClientConnection;
import com.mycompany.soundquiz.client.connection.ClientNetwork;
import com.mycompany.soundquiz.client.connection.MessageRouter;
import com.mycompany.soundquiz.client.dto.MessageRequest;
import com.mycompany.soundquiz.client.dto.MessageResponse;
import com.mycompany.soundquiz.client.model.Game_Room;
import com.mycompany.soundquiz.client.model.Music;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.UUID;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class WaitingRoomFrm extends javax.swing.JFrame {
    private ClientNetwork clientNetwork;

    /**
     * Creates new form WaitingRoomFrm
     */
    public WaitingRoomFrm(Game_Room room) {
        try {
            clientNetwork = ClientNetwork.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("WaitingRoomFrm + " + room.toString());
        initComponents();
        
        setLocationRelativeTo(null);
        
        bg.setContentType("text/html");
        bg.setText("<html>"
                + "<img src='https://i.postimg.cc/3xsnLTnS/Screenshot-2025-10-20-110646-1.png' width='300'>"
                + "</html>");
        bg.setEditable(false);
        bg.setOpaque(false);
        bg.setBorder(null);
        bg.setHighlighter(null);
        bg.setCaret(null);
        
        
        songImg.setContentType("text/html");
        songImg.setText("<html>"
                + "<img src='https://i.mydramalist.com/mODPVQ_2f.jpg' width='260'>"
                + "</html>");
        songImg.setEditable(false);
        songImg.setOpaque(false);
        songImg.setBorder(null);
        songImg.setHighlighter(null);

        songImg.setCaret(null);
        
        

        String text = room.getDescription();

        description.setContentType("text/html");
        description.setText("<html><body style='width:100%; font-size: 10px; word-wrap:break-word; overflow-wrap:break-word;'>"
        + text
        + "</body></html>");
   
        description.setEditable(false);
        description.setOpaque(false);
        description.setBorder(null);
        description.setHighlighter(null);
        description.setCaret(null);
        
        
        MessageRequest req = new MessageRequest();
        req.setUsername(ClientConnection.getInstance().getUsername());
        req.setContent(String.valueOf(room.getId()));

        String reqId = UUID.randomUUID().toString();
        req.setId(reqId);
        req.setType("accept");

        MessageRouter.getInstance().registerRequestHandler(reqId, response -> {
            System.out.println("Join game...");
        });
        
        clientNetwork.sendMessage(req);
        
        
        btnInvice.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                MessageRequest request = new MessageRequest();
                String reqId = UUID.randomUUID().toString();
                request.setId(reqId);
                request.setUsername(ClientConnection.getInstance().getUsername());
                
                JsonObject data = new JsonObject();
                data.addProperty("roomId", room.getId());           // id phòng hiện tại
                data.addProperty("invitee", inpInvice.getText());    // người được mời (ví dụ: username hoặc email)
                
                request.setContent(data.toString());
                //request.setContent(inpInvice.getText());
                
                request.setType("invice_user");
                MessageRouter.getInstance().registerRequestHandler(reqId, response -> {
                    System.out.println(response.getMessage());
                });

                clientNetwork.sendMessage(request);
            }
        });

        
        MessageRouter.getInstance().registerBroadcastHandler("attend_players", response -> {
            SwingUtilities.invokeLater(() -> {
                panelAttendance.removeAll();
                java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<List<String>>() {
                }.getType();
                List<String> players = new Gson().fromJson(response.getMessage(), listType);
                for (String u : players) {
                    addAttendance(u);
                }
            });
        });

        // Broadcast handler: start_game
        MessageRouter.getInstance().registerBroadcastHandler("start_game", response -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    JsonObject data = new Gson().fromJson(response.getMessage(), JsonObject.class);
                    String listSongJson = data.get("listSong").getAsString();
                    String questionJson = data.get("question").getAsString();
                    int roomId = data.get("roomId").getAsInt();

                    // Close waiting room and open play game
                    WaitingRoomFrm.this.dispose();
                    new PlayGameFrm(listSongJson, questionJson, roomId).setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        // Button "Bắt đầu" handler
        btnCreateGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MessageRequest request = new MessageRequest();
                String reqId = UUID.randomUUID().toString();
                request.setId(reqId);
                request.setUsername(ClientConnection.getInstance().getUsername());
                request.setType("start_game");
                request.setContent(String.valueOf(room.getId()));

                MessageRouter.getInstance().registerRequestHandler(reqId, response -> {
                    System.out.println("Game started: " + response.getMessage());
                });

                clientNetwork.sendMessage(request);
            }
        });

    }
    
    
    public void addAttendance(String name) {
        panelAttendance.setLayout(new BoxLayout(panelAttendance, BoxLayout.Y_AXIS));
        System.out.println("Attendance: " + name);
        JPanel entry = new JPanel();
        entry.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // không gap
        entry.setBackground(new Color(251, 251, 251));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new java.awt.Font("Raleway Medium", 0, 14));
        entry.add(nameLabel);
        // ép kích thước để BoxLayout không kéo dãn
        entry.setMaximumSize(new Dimension(Integer.MAX_VALUE, entry.getPreferredSize().height));
        panelAttendance.add(Box.createVerticalStrut(5));
        
        
        entry.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println("Clicked player: " + name);
                // bạn có thể mở chat, gửi invite, highlight, v.v ở đây
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                entry.setBackground(new Color(230, 230, 250)); // hover effect
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                entry.setBackground(new Color(251, 251, 251)); // reset màu
            }
        });

        panelAttendance.add(entry);
        panelAttendance.revalidate();
        panelAttendance.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        bg = new javax.swing.JEditorPane();
        btnCreateGame = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        panelAttendance = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        description = new javax.swing.JEditorPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        songImg = new javax.swing.JEditorPane();
        inpInvice = new javax.swing.JTextField();
        btnInvice = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(251, 251, 251));

        jScrollPane2.setBorder(null);

        bg.setBackground(new java.awt.Color(251, 251, 251));
        bg.setBorder(null);
        jScrollPane2.setViewportView(bg);

        btnCreateGame.setBackground(new java.awt.Color(105, 135, 248));

        jLabel3.setFont(new java.awt.Font("Raleway", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Bắt đầu");

        javax.swing.GroupLayout btnCreateGameLayout = new javax.swing.GroupLayout(btnCreateGame);
        btnCreateGame.setLayout(btnCreateGameLayout);
        btnCreateGameLayout.setHorizontalGroup(
            btnCreateGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnCreateGameLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        btnCreateGameLayout.setVerticalGroup(
            btnCreateGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnCreateGameLayout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        jLabel5.setBackground(new java.awt.Color(251, 251, 251));
        jLabel5.setFont(new java.awt.Font("Raleway SemiBold", 0, 14)); // NOI18N
        jLabel5.setText("attendance");
        jLabel5.setOpaque(true);

        panelAttendance.setBackground(new java.awt.Color(251, 251, 251));
        panelAttendance.setForeground(new java.awt.Color(251, 251, 251));

        javax.swing.GroupLayout panelAttendanceLayout = new javax.swing.GroupLayout(panelAttendance);
        panelAttendance.setLayout(panelAttendanceLayout);
        panelAttendanceLayout.setHorizontalGroup(
            panelAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 172, Short.MAX_VALUE)
        );
        panelAttendanceLayout.setVerticalGroup(
            panelAttendanceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        title.setBackground(new java.awt.Color(251, 251, 251));
        title.setFont(new java.awt.Font("Raleway SemiBold", 0, 14)); // NOI18N
        title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title.setText("Phòng chờ");
        title.setOpaque(true);

        description.setEditable(false);
        description.setBackground(new java.awt.Color(251, 251, 251));
        description.setBorder(null);
        description.setOpaque(false);
        jScrollPane1.setViewportView(description);

        songImg.setBackground(new java.awt.Color(251, 251, 251));
        songImg.setBorder(null);
        jScrollPane4.setViewportView(songImg);

        inpInvice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inpInviceActionPerformed(evt);
            }
        });

        btnInvice.setBackground(new java.awt.Color(110, 110, 110));

        jLabel4.setFont(new java.awt.Font("Raleway", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Mời");

        javax.swing.GroupLayout btnInviceLayout = new javax.swing.GroupLayout(btnInvice);
        btnInvice.setLayout(btnInviceLayout);
        btnInviceLayout.setHorizontalGroup(
            btnInviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnInviceLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        btnInviceLayout.setVerticalGroup(
            btnInviceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnInviceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inpInvice, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInvice, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(btnCreateGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(329, 329, 329))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelAttendance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inpInvice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInvice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(title)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCreateGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inpInviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inpInviceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inpInviceActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WaitingRoomFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WaitingRoomFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WaitingRoomFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WaitingRoomFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane bg;
    private javax.swing.JPanel btnCreateGame;
    private javax.swing.JPanel btnInvice;
    private javax.swing.JEditorPane description;
    private javax.swing.JTextField inpInvice;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel panelAttendance;
    private javax.swing.JEditorPane songImg;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
