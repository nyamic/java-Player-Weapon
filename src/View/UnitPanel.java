package View;
import javax.swing.*;
import java.awt.*;
import Player.Player;

public class UnitPanel extends JPanel {
    private JLabel nameLabel;
    private JLabel imgLabel;
    private JProgressBar hpBar;
    private JButton attackBtn;
    private Player player;

    public UnitPanel() {
        setLayout(new BorderLayout(0, 5));
        setOpaque(false);


        hpBar = new JProgressBar(0, 100);
        hpBar.setStringPainted(true);
        hpBar.setForeground(new Color(76, 175, 80)); 


        imgLabel = new JLabel("CHARACTER", SwingConstants.CENTER);
        imgLabel.setOpaque(true);
        imgLabel.setBackground(new Color(93, 64, 55)); 
        imgLabel.setForeground(Color.WHITE);
        imgLabel.setPreferredSize(new Dimension(110, 110)); 

        attackBtn = new JButton("공격");

        add(hpBar, BorderLayout.NORTH);
        add(imgLabel, BorderLayout.CENTER);
        add(attackBtn, BorderLayout.SOUTH);
    }

    public void setPlayerInfo(Player player) {
        this.player = player;
        if (player == null) return;

        nameLabel = new JLabel(player.getName()); 
        imgLabel.setText(player.getName());
        hpBar.setValue(player.getHp());
        hpBar.setString(player.getHp() + " / 100");


        if (player.getHp() <= 0) {
            imgLabel.setBackground(Color.GRAY);
            imgLabel.setText(player.getName() + " (사망)");
            attackBtn.setEnabled(false); 
        }
    }


    public JButton getAttackBtn() {
        return attackBtn;
    }
}