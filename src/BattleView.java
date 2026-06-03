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

    public UnitPanel[] getLeftPanels() { return leftUnits; }
    public UnitPanel[] getRightPanels() { return rightUnits; }
    
    public void attack(Player[] ps) {
        java.util.Random random = new java.util.Random();

        int[] blueTeamIndices = {1, 3, 5};
        for (int i = 0; i < leftUnits.length; i++) {
            int attackerIdx = i * 2;
            
            leftUnits[i].getAttackBtn().addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Player attacker = ps[attackerIdx];
                    
                    if (Main.checkDefeatTeam(ps)) {
                        appendLog("📢 이미 게임이 종료되었습니다.");
                        return;
                    }
                    
                    java.util.List<Integer> aliveTargets = new java.util.ArrayList<>();
                    for (int idx : blueTeamIndices) {
                        if (ps[idx].isAlive()) {
                            aliveTargets.add(idx);
                        }
                    }
                    
                    if (aliveTargets.isEmpty()) return;
                    
                    int targetIdx = aliveTargets.get(random.nextInt(aliveTargets.size()));
                    Player target = ps[targetIdx];
                    
                    attacker.attack(target);
                    
                    appendLog("[RED] " + attacker.getName() + " -> [BLUE] " + target.getName() + " 공격!");
                    updateAllTeams(ps);
                    
                    if (Main.checkDefeatTeam(ps)) {
                        appendLog("\n🏆 [RED] 팀이 최종 승리했습니다! 게임 종료 🏆");
                    }
                }
            });
        }


        int[] redTeamIndices = {0, 2, 4};
        for (int i = 0; i < rightUnits.length; i++) {
            final int attackerIdx = (i * 2) + 1;
            
            rightUnits[i].getAttackBtn().addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Player attacker = ps[attackerIdx];
                    
                    if (Main.checkDefeatTeam(ps)) {
                        appendLog("이미 게임이 종료되었습니다.");
                        return;
                    }
                    
                    java.util.List<Integer> aliveTargets = new java.util.ArrayList<>();
                    for (int idx : redTeamIndices) {
                        if (ps[idx].isAlive()) {
                            aliveTargets.add(idx);
                        }
                    }
                    
                    if (aliveTargets.isEmpty()) return;
                    
                    int targetIdx = aliveTargets.get(random.nextInt(aliveTargets.size()));
                    Player target = ps[targetIdx];
                    
                    attacker.attack(target);
                    
                    appendLog("[BLUE] " + attacker.getName() + " -> [RED] " + target.getName() + " 공격!");
                    updateAllTeams(ps);
                    
                    if (Main.checkDefeatTeam(ps)) {
                        appendLog("\n[BLUE] 팀이 최종 승리했습니다! 게임 종료");
                    }
                }
            });
        }
    }
}