package View;

import javax.swing.*;
import java.awt.*;
import Player.Player;

public class UnitPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel imgLabel;
    private JProgressBar hpBar;
    private JButton attackBtn;
    private JButton weaponBtn;
    private JPanel btnPanel;
    private Player player;

    public UnitPanel() {
        setLayout(new BorderLayout(0, 5));
        setOpaque(false);

        //hp 최대 200으로 수정
        hpBar = new JProgressBar(0, 200);
        hpBar.setStringPainted(true);
        hpBar.setForeground(new Color(76, 175, 80)); 
        
        

        imgLabel = new JLabel("CHARACTER", SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(new Color(93, 64, 55)); 
        imgLabel.setForeground(Color.WHITE);
        imgLabel.setPreferredSize(new Dimension(110, 110)); 

        attackBtn = new JButton("공격");
        weaponBtn = new JButton("무기 공격");
        weaponBtn.setVisible(false);
        
        btnPanel = new JPanel(new GridLayout(1, 1, 5, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(attackBtn);
        btnPanel.add(weaponBtn);

        add(hpBar, BorderLayout.NORTH);
        add(imgLabel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void setPlayerInfo(Player player) {
        this.player = player;
        if (player == null) return;

        nameLabel = new JLabel(player.getName()); 
        imgLabel.setText(player.getName());
        
        int displayHp = Math.max(player.getHp(), 0);
        hpBar.setValue(displayHp);
        hpBar.setString(displayHp + " / 200");
        
        String imagePath = "image/" + player.getName();
        
        if (!player.isAlive()) {
        	imagePath += "_dead.png";
        	hpBar.setForeground(new Color(76, 175, 80));
        	attackBtn.setEnabled(false);
        	weaponBtn.setEnabled(false); 
        }
        else {
        	imagePath += ".png";
        	hpBar.setForeground(new Color(76, 175, 80));
        	attackBtn.setEnabled(true);
        	weaponBtn.setEnabled(true); 
        }
        
        
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImg = icon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
        imgLabel.setIcon(new ImageIcon(scaledImg));
        imgLabel.setText(player.getName());
        
        if (player.getWeapon() != null) {
            weaponBtn.setText(player.getWeapon().getName() + " 공격");
            weaponBtn.setVisible(true);
            btnPanel.setLayout(new GridLayout(1, 2, 5, 0));
        } else {
            weaponBtn.setVisible(false);
            btnPanel.setLayout(new GridLayout(1, 1, 0, 0));
        }
        btnPanel.revalidate();
        btnPanel.repaint();
    }
    
    public void setOnClickCallback(Runnable callback) {
        imgLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        imgLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                callback.run();
            }
        });
    }
    
    public void setHighlight(boolean on) {
        setBorder(on
            ? BorderFactory.createLineBorder(new Color(255, 80, 80), 3)
            : null);
    }

    public void setAttackerHighlight(boolean on) {
        setBorder(on
            ? BorderFactory.createLineBorder(new Color(80, 80, 255), 3)
            : null);
    }


    public JButton getAttackBtn() {
        return attackBtn;
    }
    
    public JButton getWeaponBtn() {
        return weaponBtn;
    }
}