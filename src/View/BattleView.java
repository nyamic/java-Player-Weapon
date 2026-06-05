package View;

import javax.swing.*;
import java.awt.*;
import Player.Player;

public class BattleView extends JPanel {
    protected static final String Main = null;
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
        
        JPanel eastContainer = new JPanel(new BorderLayout(0, 10));
        eastContainer.setPreferredSize(new Dimension(200, 0));
        
        JLabel blueTitle = new JLabel("[TEAM JOJA]", SwingConstants.CENTER);
        blueTitle.setFont(new Font("굴림", Font.BOLD, 18));
        eastContainer.add(blueTitle, BorderLayout.NORTH);
        JPanel westPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        for (int i = 0; i < 3; i++) {
            leftUnits[i] = new UnitPanel(); 
            westPanel.add(leftUnits[i]);
        }
        westContainer.add(westPanel, BorderLayout.CENTER);
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
	public boolean checkDefeatTeam(Player[] ps) {
		boolean redDead = !ps[0].isAlive() && !ps[2].isAlive() && !ps[4].isAlive();
		boolean blueDead = !ps[1].isAlive() && !ps[3].isAlive() && !ps[5].isAlive();
		return redDead || blueDead;
	}
    public void attack(Player[] ps) {
        java.util.Random random = new java.util.Random();

        int[] blueTeamIndices = {1, 3, 5};
        for (int i = 0; i < leftUnits.length; i++) {
            int attackerIdx = i * 2;
            
            leftUnits[i].getAttackBtn().addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Player attacker = ps[attackerIdx];
                    
                    if (checkDefeatTeam(ps)) {
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
                    
                    appendLog("[PIERRE] " + attacker.getName() + " -> [JOJA] " + target.getName() + " 공격!");
                    updateAllTeams(ps);
                    
                    if (checkDefeatTeam(ps)) {
                        appendLog("\n🏆 [PIERRE] 팀이 최종 승리했습니다! 게임 종료 🏆");
                        showWinner(ps, "PIERRE");
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
                    
                    if (checkDefeatTeam(ps)) {
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
                    
                    appendLog("[JOJA] " + attacker.getName() + " -> [PIERRE] " + target.getName() + " 공격!");
                    updateAllTeams(ps);
                    
                    if (checkDefeatTeam(ps)) {
                        appendLog("\n[JOJA] 팀이 최종 승리했습니다! 게임 종료");
                        showWinner(ps, "JOJA");
                    }
                }
            });
        }
    }
    
    public void showWinner(Player[] ps, String winnerTeam) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog((Frame) parentWindow, "배틀 종료", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parentWindow);

        // 상단 WIN 텍스트
        JLabel winLabel = new JLabel("Team "+winnerTeam.toUpperCase() + " Win!", SwingConstants.CENTER);
        winLabel.setFont(new Font("Arial", Font.BOLD, 36));
        winLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(winLabel, BorderLayout.NORTH);

        // 중앙 캐릭터 이미지 패널
        JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));

        for (Player p : ps) {
        	String teamColor = winnerTeam.equals("PIERRE") ? "red" : "blue";
            if (!p.team.equals(teamColor)) {
            	continue;
            }
            

            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            
            String path = System.getProperty("user.dir") + "/image/" + p.getName() + ".png";
            
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

        // 하단 버튼
        JButton closeBtn = new JButton("게임 종료하기");
        closeBtn.setFont(new Font("굴림", Font.PLAIN, 13));
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        btnPanel.add(closeBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}