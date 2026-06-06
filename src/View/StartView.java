package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartView extends JPanel {

    private Runnable onPlayClicked;
    private Image bgImage;

    public StartView() {
        setLayout(new BorderLayout());
        bgImage = new ImageIcon("image/title.png").getImage();

        // 하단 PLAY 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JButton playBtn = new JButton("PLAY");
        playBtn.setFont(new Font("Arial", Font.BOLD, 36));
        playBtn.setForeground(Color.WHITE);
        playBtn.setBackground(new Color(180, 120, 20));
        
        playBtn.setFocusPainted(false);
        playBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        playBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                playBtn.setBackground(new Color(220, 160, 30));
            }
            @Override public void mouseExited(MouseEvent e) {
                playBtn.setBackground(new Color(180, 120, 20));
            }
        });

        playBtn.addActionListener(e -> {
            if (onPlayClicked != null) onPlayClicked.run();
        });

        bottomPanel.add(playBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void setOnPlayClicked(Runnable callback) {
        this.onPlayClicked = callback;
    }
}