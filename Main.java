import java.util.Random;
import java.util.List;
import javax.swing.JFrame;
import Player.*;
import View.BattleView;
import Weapon.*;

public class Main {
	public static void shuffleArray(Player[] array) {
        Random rand = new Random();
        
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);

            Player temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
	
	public static void selectTeam(Player[] ps) {
		for(int i = 0; i < ps.length; i++) {
			if(i % 2 == 0) ps[i].team = "red";
			else ps[i].team = "blue";
		}
	}
	
	public static boolean checkDefeatTeam(Player[] ps) {
		boolean redDead = !ps[0].isAlive() && !ps[2].isAlive() && !ps[4].isAlive();
		boolean blueDead = !ps[1].isAlive() && !ps[3].isAlive() && !ps[5].isAlive();
		return redDead || blueDead;
	}

	///<summary>하비/거스만 남았는지 확인하고 딜러 전환</summary>
	public static void checkAndConvert(Player[] ps, Random r) {
		for (String team : new String[]{"red", "blue"}) {
			List<Player> alive = Player.getAliveTeammates(ps, team);
			if (alive.isEmpty()) continue;

			boolean hasNormal = Player.hasNormalAttacker(ps, team);

			하비 harvey = null;
			거스 gus = null;
			for (Player p : alive) {
				if (p instanceof 하비) harvey = (하비) p;
				if (p instanceof 거스) gus = (거스) p;
			}

			// 하비만 남음 --> 하비 딜러 전환
			if (harvey != null && gus == null && !hasNormal) {
				harvey.convertToDamageDealer();
			}

			// 거스만 남음 --> 거스 딜러 전환
			if (gus != null && harvey == null && !hasNormal) {
				gus.convertToDamageDealer();
			}

			// 하비+거스만 남음
			if (harvey != null && gus != null && !hasNormal) {
				if (!harvey.isDamageDealer() && !gus.isDamageDealer()) {
					// 랜덤 1명만 딜러 전환
					if (r.nextBoolean()) {
						harvey.convertToDamageDealer();
					} else {
						gus.convertToDamageDealer();
					}
				} else if (harvey.isDamageDealer() && !harvey.isAlive()) {
					// 딜러였던 하비 사망 --> 거스도 딜러 전환
					gus.convertToDamageDealer();
				} else if (gus.isDamageDealer() && !gus.isAlive()) {
					// 딜러였던 거스 사망 --> 하비도 딜러 전환
					harvey.convertToDamageDealer();
				}
			}
		}
	}

	public static void main(String[] args) {
		//Player 생성
		헤일리 Hailey = new 헤일리("헤일리", 200, 60);
		레아 Rea = new 레아("레아", 200, 30);
		로빈 Robin = new 로빈("로빈", 200, 40);
		마리 Mary = new 마리("마니", 200, 50);
		거스 Gus = new 거스("거스", 200, 10);
		하비 Harvey = new 하비("하비", 200, 100);
		
		//무기 생성
		Rea.setWeapon(new 카메라("카메라", 40));
		Robin.setWeapon(new 도끼("도끼", 90));
		Mary.setWeapon(new 채찍("채찍", 100));
		
		Player [] ps= {Hailey, Rea, Mary, Gus, Harvey, Robin};
		Player attacker, target;
		
		shuffleArray(ps);
		selectTeam(ps);
		
		//GUI
		JFrame frame = new JFrame("3:3 스타듀 배틀");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);

        BattleView battleView = new BattleView(ps);

        frame.add(battleView);
        frame.setVisible(true); 

		
		Random r = new Random();

		int count = ps.length; 
		
		String currentTeam = "red";
		while(true) {
			checkAndConvert(ps, r);

			int i = r.nextInt(count) ;
			int j = r.nextInt(count) ;
			if (i==j)  continue;

			attacker = ps[ i ];
			target = ps[ j ];
			
			if(attacker.team.equals(target.team)) continue;
			if(currentTeam.equals(attacker.team)) continue;
			if (!attacker.isAlive()) continue;
			if(!target.isAlive()) continue;

			int w = r.nextInt(2);
			if(w == 1 && attacker.getWeapon() != null) {
				attacker.attack(target, attacker.getWeapon());
				System.out.println(w);
			}
			else {
				attacker.useSkill(ps, target);
			}
			
			currentTeam = attacker.team;

			if (target.getHp() <=0 ) {
				System.out.println(target.getName() + "가 죽었습니다.");
			}

			Player.showStatus(ps);
			
			battleView.updateAllTeams(ps);
			
			if(checkDefeatTeam(ps)) break;
		}

		for(Player p : ps) {
			if(p.isAlive()) {
				System.out.println(p.team + " 팀 승리!");
				break;
			}
		}
	}
}
