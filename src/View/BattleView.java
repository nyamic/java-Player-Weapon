package View;

import javax.swing.*;
import java.awt.*;
import Player.Player;

public class BattleView extends JPanel {
    private UnitPanel[] leftUnits = new UnitPanel[3];
    private UnitPanel[] rightUnits = new UnitPanel[3];
    private JTextArea console;

    public BattleView() {
        setLayout(new BorderLayout(25, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

       
        JPanel westContainer = new JPanel(new BorderLayout(0, 10));
        westContainer.setPreferredSize(new Dimension(200, 0));

        JLabel redTitle = new JLabel("[TEAM PIERRE]", SwingConstants.CENTER);
        redTitle.setFont(new Font("굴림", Font.BOLD, 18));
        westContainer.add(redTitle, BorderLayout.NORTH);

        JPanel westPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        for (int i = 0; i < 3; i++) {
            leftUnits[i] = new UnitPanel();
            westPanel.add(leftUnits[i]);
        }
        westContainer.add(westPanel, BorderLayout.CENTER);


        JPanel eastContainer = new JPanel(new BorderLayout(0, 10));
        eastContainer.setPreferredSize(new Dimension(200, 0));

        JLabel blueTitle = new JLabel("[TEAM JOJA]", SwingConstants.CENTER);
        blueTitle.setFont(new Font("굴림", Font.BOLD, 18));
        eastContainer.add(blueTitle, BorderLayout.NORTH);

        JPanel eastPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        for (int i = 0; i < 3; i++) {
            rightUnits[i] = new UnitPanel();
            eastPanel.add(rightUnits[i]);
        }
        eastContainer.add(eastPanel, BorderLayout.CENTER);

        console = new JTextArea("베틀 로그 창\n\n");
        console.setEditable(false);
        console.setFont(new Font("굴림", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(console);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(westContainer, BorderLayout.WEST);
        add(eastContainer, BorderLayout.EAST);
        add(scroll, BorderLayout.CENTER);
    }

    // ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 화면갱신 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public void updateAllTeams(Player[] ps) {
        leftUnits[0].setPlayerInfo(ps[0]);
        leftUnits[1].setPlayerInfo(ps[2]);
        leftUnits[2].setPlayerInfo(ps[4]);

        rightUnits[0].setPlayerInfo(ps[1]);
        rightUnits[1].setPlayerInfo(ps[3]);
        rightUnits[2].setPlayerInfo(ps[5]);
    }

    public void appendLog(String msg) {
        console.append(msg + "\n");
        console.setCaretPosition(console.getDocument().getLength());
    }

    // 모든 패널 강조 초기화
    public void clearAllHighlights() {
        for (UnitPanel p : leftUnits)  p.setHighlight(false);
        for (UnitPanel p : rightUnits) p.setHighlight(false);
        for (UnitPanel p : leftUnits)  p.setAttackerHighlight(false);
        for (UnitPanel p : rightUnits) p.setAttackerHighlight(false);
    }

    // ㅡㅡㅡㅡ 컨트롤러가 콜백 등록할 때 쓰는 메서드들 ㅡㅡㅡㅡㅡㅡㅡㅡ

    public void setOnAttackBtnClicked(int index, Runnable callback) {
        leftUnits[index].getAttackBtn().addActionListener(e -> callback.run());
    }

    public void setOnUnitClicked(boolean isLeft, int index, Runnable callback) {
        if (isLeft) leftUnits[index].setOnClickCallback(callback);
        else        rightUnits[index].setOnClickCallback(callback);
    }
    
    public void setOnWeaponBtnClicked(int index, Runnable callback) {
        leftUnits[index].getWeaponBtn().addActionListener(e -> callback.run());
    }

    // ㅡㅡㅡㅡㅡㅡㅡ 강조 표시 ㅡㅡㅡㅡㅡㅡㅡ

    public void highlightAsAttacker(int leftIndex) {
        clearAllHighlights();
        leftUnits[leftIndex].setAttackerHighlight(true);
    }

    public void highlightAllEnemies() {
        for (UnitPanel p : rightUnits) p.setHighlight(true);
    }

    public void highlightAllAllies(int attackerIdx) {
        for (int i = 0; i < leftUnits.length; i++) {
            if (i != attackerIdx) leftUnits[i].setHighlight(true);
        }
    }
    
    // ㅡㅡㅡㅡㅡㅡ 승패 판정 표시 ㅡㅡㅡㅡㅡㅡ
    public void showWinner(Player[] ps, String winnerTeam) {
        SwingUtilities.invokeLater(() -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog(parentFrame, "배틀 종료", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(parentFrame);

            JLabel winLabel = new JLabel("Team " + winnerTeam + " Win!", SwingConstants.CENTER);
            winLabel.setFont(new Font("Arial", Font.BOLD, 36));
            winLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            dialog.add(winLabel, BorderLayout.NORTH);

            JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
            String teamColor = winnerTeam.equals("RED") ? "red" : "blue";

            for (Player p : ps) {
                if (!p.team.equals(teamColor)) continue;

                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

                String path = "image/" + p.getName() + ".png";
                ImageIcon raw = new ImageIcon(path);
                JLabel img = new JLabel(new ImageIcon(raw.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH)));
                img.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel name = new JLabel(p.getName());
                name.setFont(new Font("굴림", Font.BOLD, 14));
                name.setAlignmentX(Component.CENTER_ALIGNMENT);

                card.add(img);
                card.add(Box.createVerticalStrut(5));
                card.add(name);
                imgPanel.add(card);
            }
            dialog.add(imgPanel, BorderLayout.CENTER);

            JButton closeBtn = new JButton("게임 종료하기");
            closeBtn.setFont(new Font("굴림", Font.PLAIN, 13));
            closeBtn.addActionListener(e -> dialog.dispose());
            JPanel btnPanel = new JPanel();
            btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            btnPanel.add(closeBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);

            dialog.setVisible(true);
        });
    }
    

    // ㅡㅡㅡㅡㅡㅡ getter ㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public UnitPanel[] getLeftPanels() { return leftUnits; }
    public UnitPanel[] getRightPanels() { return rightUnits; }
}