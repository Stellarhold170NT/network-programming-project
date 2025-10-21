/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.soundquiz.client.view;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mycompany.soundquiz.client.connection.ClientConnection;
import java.lang.reflect.Type;
import com.mycompany.soundquiz.client.connection.ClientNetwork;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.*;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.Box;
import javax.swing.SwingUtilities;
import com.mycompany.soundquiz.client.connection.MessageRouter;
import com.mycompany.soundquiz.client.dto.MessageRequest;
import com.mycompany.soundquiz.client.dto.MessageResponse;
import com.mycompany.soundquiz.client.model.Game_Room;
import java.util.UUID;
import javax.swing.JOptionPane;
//import javax.swing.*;

/**
 *
 * @author Admin
 */
public class HomeFrm extends javax.swing.JFrame {
    private ClientNetwork clientNetwork;
    
    /**
     * Creates new form HomeFrm
     */
    public HomeFrm(String username) {
        try {
            initComponents();
            this.setLocationRelativeTo(null);
            ClientConnection.getInstance().setUsername(username);
                    
            clientNetwork = ClientNetwork.getInstance();
        } catch (Exception e) {

        }
        
        txtInvice.setVisible(false);
        txtAttend.setVisible(false);
        btnAttend.setVisible(false);

        
        txtUsername.setText(username);
        editor.setContentType("text/html");
        editor.setText("<html>"
                + "<img src='https://images.icon-icons.com/3053/PNG/512/music_macos_bigsur_icon_189926.png' width='80'>"
                + "</html>");
        editor.setEditable(false);
        editor.setOpaque(false);
        editor.setBorder(null);
        editor.setHighlighter(null);

        editor.setCaret(null);
        panelOnlinePlayer.setLayout(new BoxLayout(panelOnlinePlayer, BoxLayout.Y_AXIS));
//        addOnlinePlayer("Nguyen Viet Huy");        
//        addOnlinePlayer("Lai Xuan Hieu");

 
            

           // Đăng ký broadcast handler cho online_players
        MessageRouter.getInstance().registerBroadcastHandler("online_players", response -> {
            SwingUtilities.invokeLater(() -> {
                panelOnlinePlayer.removeAll();

                java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<List<String>>() {
                }.getType();
                List<String> players = new Gson().fromJson(response.getMessage(), listType);
                for (String name : players) {
                    addOnlinePlayer(name);
                }

                panelOnlinePlayer.revalidate();
                panelOnlinePlayer.repaint();
            });
        });
        
        
          MessageRouter.getInstance().registerBroadcastHandler("invice_game", response -> {
            SwingUtilities.invokeLater(() -> {
                JsonObject obj = JsonParser.parseString(response.getMessage()).getAsJsonObject();
                
                txtInvice.setText(obj.get("fromUser").getAsString() + " want to play a game with you");
                txtInvice.setVisible(true);
                txtAttend.setVisible(true);
                btnAttend.setVisible(true);
                
                
                btnAttend.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        txtInvice.setVisible(false);
                        txtAttend.setVisible(false);
                        btnAttend.setVisible(false);
                        
                        
                        
                        
                        Gson gson = new Gson();
                        Game_Room room = gson.fromJson(obj.get("room").getAsString(), Game_Room.class);
                        
                        
                        java.awt.EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                new WaitingRoomFrm(room).setVisible(true);
                            }
                        });
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btnCreateGame.setBackground(new Color(85, 115, 220)); // hiệu ứng hover
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        btnCreateGame.setBackground(new Color(105, 135, 248)); // trở về màu gốc
                    }
                });
                
            });
        });

        loadPlayers();
        rankPlayers();
        
        
        btnCreateGame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Button clicked!");
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        new CreateGameFrm().setVisible(true);
                    }
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                 btnCreateGame.setBackground(new Color(85, 115, 220)); // hiệu ứng hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCreateGame.setBackground(new Color(105, 135, 248)); // trở về màu gốc
            }
        });

    }
    
    public void addOnlinePlayer(String name) {
        JPanel entry = new JPanel();
        entry.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // không gap
        entry.setBackground(new Color(251, 251, 251));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new java.awt.Font("Raleway Medium", 0, 14));
        entry.add(nameLabel);
        // ép kích thước để BoxLayout không kéo dãn
        entry.setMaximumSize(new Dimension(Integer.MAX_VALUE, entry.getPreferredSize().height));
        panelOnlinePlayer.add(Box.createVerticalStrut(5));
        
        
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

        panelOnlinePlayer.add(entry);
        panelOnlinePlayer.revalidate();
        panelOnlinePlayer.repaint();
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
        jLabel2 = new javax.swing.JLabel();
        btnCreateGame = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        editor = new javax.swing.JEditorPane();
        panelOnlinePlayer = new javax.swing.JPanel();
        rankingPanel = new javax.swing.JPanel();
        txtInvice = new javax.swing.JLabel();
        btnAttend = new javax.swing.JPanel();
        txtAttend = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(251, 251, 251));

        jLabel2.setFont(new java.awt.Font("Raleway", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Sound Quiz ");

        btnCreateGame.setBackground(new java.awt.Color(105, 135, 248));

        jLabel3.setFont(new java.awt.Font("Raleway", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Tạo trò chơi");

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

        jLabel4.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel4.setText("Hi, ");

        jPanel3.setBackground(new java.awt.Color(242, 242, 242));

        jTextField1.setBackground(new java.awt.Color(242, 242, 242));
        jTextField1.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField1.setText("Search");
        jTextField1.setBorder(null);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jLabel5.setBackground(new java.awt.Color(251, 251, 251));
        jLabel5.setFont(new java.awt.Font("Raleway SemiBold", 0, 14)); // NOI18N
        jLabel5.setText("online player");
        jLabel5.setOpaque(true);

        jLabel7.setFont(new java.awt.Font("Raleway", 0, 14)); // NOI18N
        jLabel7.setText("Bảng xếp hạng");

        txtUsername.setFont(new java.awt.Font("Raleway SemiBold", 0, 14)); // NOI18N
        txtUsername.setText("neptune170nt");

        jScrollPane3.setBorder(null);

        editor.setBackground(new java.awt.Color(251, 251, 251));
        editor.setBorder(null);
        jScrollPane3.setViewportView(editor);

        panelOnlinePlayer.setBackground(new java.awt.Color(251, 251, 251));

        javax.swing.GroupLayout panelOnlinePlayerLayout = new javax.swing.GroupLayout(panelOnlinePlayer);
        panelOnlinePlayer.setLayout(panelOnlinePlayerLayout);
        panelOnlinePlayerLayout.setHorizontalGroup(
            panelOnlinePlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        panelOnlinePlayerLayout.setVerticalGroup(
            panelOnlinePlayerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        rankingPanel.setBackground(new java.awt.Color(251, 251, 251));

        javax.swing.GroupLayout rankingPanelLayout = new javax.swing.GroupLayout(rankingPanel);
        rankingPanel.setLayout(rankingPanelLayout);
        rankingPanelLayout.setHorizontalGroup(
            rankingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        rankingPanelLayout.setVerticalGroup(
            rankingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
        );

        txtInvice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtInvice.setText("nt170 muốn mời bạn chơi game");

        btnAttend.setBackground(new java.awt.Color(110, 110, 110));

        txtAttend.setFont(new java.awt.Font("Raleway", 0, 16)); // NOI18N
        txtAttend.setForeground(new java.awt.Color(255, 255, 255));
        txtAttend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtAttend.setText("Tham gia");

        javax.swing.GroupLayout btnAttendLayout = new javax.swing.GroupLayout(btnAttend);
        btnAttend.setLayout(btnAttendLayout);
        btnAttendLayout.setHorizontalGroup(
            btnAttendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnAttendLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtAttend, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        btnAttendLayout.setVerticalGroup(
            btnAttendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnAttendLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtAttend)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(rankingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(96, 96, 96)
                                    .addComponent(jLabel4)
                                    .addGap(0, 0, 0)
                                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(25, 25, 25)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCreateGame, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(147, 147, 147))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtInvice, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(115, 115, 115))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAttend, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(170, 170, 170)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelOnlinePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtUsername))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(27, 27, 27)
                        .addComponent(btnCreateGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(txtInvice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAttend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(52, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(panelOnlinePlayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(rankingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

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
            java.util.logging.Logger.getLogger(HomeFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrm().setVisible(true);
            }
        });
    }
    
    private void loadPlayers() {
        MessageRequest msg = new MessageRequest();
        msg.setType("load_players");

        String reqId = UUID.randomUUID().toString();
        msg.setId(reqId); 

        // Đăng ký handler tạm thời
        MessageRouter.getInstance().registerRequestHandler(reqId, response -> {
            SwingUtilities.invokeLater(() -> {
                panelOnlinePlayer.removeAll();

                java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<List<String>>() {
                }.getType();
                List<String> players = new Gson().fromJson(response.getMessage(), listType);
                for (String name : players) {
                    addOnlinePlayer(name);
                }

                panelOnlinePlayer.revalidate();
                panelOnlinePlayer.repaint();
            });
        });


        clientNetwork.sendMessage(msg);
    }
    
    private void rankPlayers() {
        

        MessageRequest msg = new MessageRequest();
        msg.setType("ranking_players");

        String reqId = UUID.randomUUID().toString();
        msg.setId(reqId); 

        // Đăng ký handler tạm thời
        MessageRouter.getInstance().registerRequestHandler(reqId, response -> {
            SwingUtilities.invokeLater(() -> {
                java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<List<String>>() {
                }.getType();
                List<String> players = new Gson().fromJson(response.getMessage(), listType);

                rankingPanel.setLayout(new BoxLayout(rankingPanel, BoxLayout.Y_AXIS));
                for (int i = 0; i < players.size(); i++) {
                    addRankedPlayer(rankingPanel, i + 1, players.get(i));
                }

                revalidate();
                repaint();
            });
        });


        clientNetwork.sendMessage(msg);
    }
    
    
    private void addRankedPlayer(JPanel panel, int rank, String playerName) {
        JPanel entry = new JPanel();
        entry.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        entry.setBackground(new Color(251, 251, 251));

        // Tạo label hiển thị xếp hạng
        JLabel rankLabel = new JLabel("#" + rank);
        rankLabel.setFont(new java.awt.Font("Raleway", 0, 14));
        if (rank == 1)
            rankLabel.setBackground(new Color(245, 100, 120));
        else if (rank == 2) {
            rankLabel.setBackground(new Color(105, 135, 248));
        }
        else if (rank == 3) {
            rankLabel.setBackground(new Color(110, 110, 110));
        }
        if (rank <= 3) {
            rankLabel.setForeground(Color.WHITE);
            rankLabel.setOpaque(true);
        }
        rankLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rankLabel.setPreferredSize(new Dimension(24, 24));

        // Tạo label hiển thị tên người chơi
        JLabel nameLabel = new JLabel(playerName);
        nameLabel.setFont(new java.awt.Font("Raleway", 0, 14));

        // Thêm các label vào entry
        entry.add(rankLabel);
        entry.add(nameLabel);

        // Đặt kích thước tối đa để BoxLayout không kéo giãn
        entry.setMaximumSize(new Dimension(Integer.MAX_VALUE, entry.getPreferredSize().height));

        // Thêm khoảng cách giữa các entry
        panel.add(Box.createVerticalStrut(5));
        panel.add(entry);

        // Thêm hiệu ứng hover (tùy chọn)
        entry.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                entry.setBackground(new Color(230, 230, 250)); // hover effect
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                entry.setBackground(new Color(251, 251, 251)); // reset màu
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnAttend;
    private javax.swing.JPanel btnCreateGame;
    private javax.swing.JEditorPane editor;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel panelOnlinePlayer;
    private javax.swing.JPanel rankingPanel;
    private javax.swing.JLabel txtAttend;
    private javax.swing.JLabel txtInvice;
    private javax.swing.JLabel txtUsername;
    // End of variables declaration//GEN-END:variables
}
