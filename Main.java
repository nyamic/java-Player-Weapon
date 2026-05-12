import java.util.Random;

import Player.*;
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
	
	public static void main(String[] args) {
		//Player 생성
		헤일리 Hailey = new 헤일리("헤일리", 200, 60);
		레아 Rea = new 레아("레아", 200, 30);
		로빈 Robin = new 로빈("로빈", 200, 40);
		마리 Mary = new 마리("마리", 200, 50);
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
		
		Random r = new Random();

		int count = ps.length; 
		
		String currentTeam = "red";
		while(true) {
			int i = r.nextInt(count) ;
			int j = r.nextInt(count) ;
			if (i==j)  continue;

			attacker = ps[ i ];
			target = ps[ j ];
			
			if(attacker.team.equals(target.team)) continue;
			
			if(currentTeam.equals(attacker.team)) continue;
			
			if(!target.isAlive()) continue;

			int w = r.nextInt(2);
			
			if(w == 1 && attacker.getWeapon() != null) {
				attacker.attack(target, attacker.getWeapon());
				System.out.println(w);
			}
			else {
				attacker.attack(target);
			}
			
			currentTeam = attacker.team;

			if (target.getHp() <=0 ) {
				System.out.println(target.getName() + "가 죽었습니다.");
			}

			Player.showStatus(ps);
			
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
