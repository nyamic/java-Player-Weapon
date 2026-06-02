package View;

import javax.swing.*;
import java.awt.*;
import Player.Player;

public class BattleView extends JPanel {
    private UnitPanel[] leftUnits = new UnitPanel[3];
    private UnitPanel[] rightUnits = new UnitPanel[3];
    private JTextArea console;
    private Player[] players;

    public BattleView(Player[] ps) {
    	this.players = ps;
        setLayout(new BorderLayout(25, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel westPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        westPanel.setPreferredSize(new Dimension(200, 0)); 
        for (int i = 0; i < 3; i++) {
            leftUnits[i] = new UnitPanel();
            westPanel.add(leftUnits[i]);
        }

        JPanel eastPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        eastPanel.setPreferredSize(new Dimension(200, 0)); 
        for (int i = 0; i < 3; i++) {
            rightUnits[i] = new UnitPanel();
            eastPanel.add(rightUnits[i]);
        }

        console = new JTextArea("베틀 로그 창\n\n");
        console.setEditable(false); 
        console.setFont(new Font("굴림", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(console);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 

        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);
        add(scroll, BorderLayout.CENTER); 
    }

    public void updateAllTeams(Player[] ps) {
    	this.players = ps;
        // 짝수 번호는 왼쪽팀에 배정
        leftUnits[0].setPlayerInfo(ps[0]);
        leftUnits[1].setPlayerInfo(ps[2]);
        leftUnits[2].setPlayerInfo(ps[4]);

        // 홀수 번호는 오른쪽팀에 배정
        rightUnits[0].setPlayerInfo(ps[1]);
        rightUnits[1].setPlayerInfo(ps[3]);
        rightUnits[2].setPlayerInfo(ps[5]);
        
        this.repaint();
    }

    public void appendLog(String msg) { 
        console.append(msg + "\n");
        console.setCaretPosition(console.getDocument().getLength());
    }
    
    public UnitPanel[] getLeftPanels() { return leftUnits; }
    public UnitPanel[] getRightPanels() { return rightUnits; }
}