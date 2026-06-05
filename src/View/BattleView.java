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
    
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 화면갱신 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    public void updateAllTeams(Player[] ps) {
        // 짝수 번호는 왼쪽팀에 배정
        leftUnits[0].setPlayerInfo(ps[0]);
        leftUnits[1].setPlayerInfo(ps[2]);
        leftUnits[2].setPlayerInfo(ps[4]);

        // 홀수 번호는 오른쪽팀에 배정
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

    // 왼쪽(유저) 공격버튼 콜백 등록
    public void setOnAttackBtnClicked(int index, Runnable callback) {
        leftUnits[index].getAttackBtn().addActionListener(e -> callback.run());
    }

    // 타겟 선택 콜백 등록
    public void setOnUnitClicked(boolean isLeft, int index, Runnable callback) {
        if (isLeft) leftUnits[index].setOnClickCallback(callback);
        else        rightUnits[index].setOnClickCallback(callback);
    }
    
    // ㅡㅡㅡㅡㅡㅡㅡ 강조 표시 ㅡㅡㅡㅡㅡㅡㅡ
    
    public void highlightAsAttacker(int leftIndex) {
        clearAllHighlights();
        leftUnits[leftIndex].setAttackerHighlight(true);
    }

    public void highlightAllEnemies() {
        for (UnitPanel p : rightUnits) p.setHighlight(true);
    }
    
    //ㅡㅡㅡㅡㅡㅡ getter ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    
    public UnitPanel[] getLeftPanels() { return leftUnits; }
    public UnitPanel[] getRightPanels() { return rightUnits; }
}