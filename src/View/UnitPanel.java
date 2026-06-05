package View;

import javax.swing.*;
import java.awt.*;
import Player.Player;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UnitPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel imgLabel;
    private JProgressBar hpBar;
    private JButton attackBtn;
    private JButton weaponBtn;
    private JPanel btnPanel;
    private Player player;
    private Runnable onClickCallback;

    public UnitPanel() {
        setLayout(new BorderLayout(0, 5));
        setOpaque(false);

        hpBar = new JProgressBar(0, 200);
        hpBar.setStringPainted(true);
        hpBar.setForeground(new Color(76, 175, 80));

        imgLabel = new JLabel("CHARACTER", SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(new Color(93, 64, 55));
        imgLabel.setForeground(Color.WHITE);
        imgLabel.setPreferredSize(new Dimension(110, 110));

        imgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClickCallback != null && player != null && player.isAlive()) {
                    onClickCallback.run();
                }
            }
        });

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
        hpBar.setValue(player.getHp());
        hpBar.setString(player.getHp() + " / 200");

        String imagePath = "image/" + player.getName();

        if (!player.isAlive()) {
            imagePath += "_dead.png";
            hpBar.setForeground(new Color(76, 175, 80));
            attackBtn.setEnabled(false);
            weaponBtn.setEnabled(false);
        } else {
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
        this.onClickCallback = callback;
    }

    // 타겟 강조 (노란색)
    public void setHighlight(boolean on) {
        setBorder(on ? BorderFactory.createLineBorder(Color.YELLOW, 3) : null);
    }

    // 공격자 강조 (파란색)
    public void setAttackerHighlight(boolean on) {
        setBorder(on ? BorderFactory.createLineBorder(Color.CYAN, 3) : null);
    }

    public JButton getAttackBtn() { return attackBtn; }
    public JButton getWeaponBtn() { return weaponBtn; }
}