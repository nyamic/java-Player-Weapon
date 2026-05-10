import java.util.Random;

import Player.*;
import Weapon.*;

public class Main {
	public static void main(String[] args) {
		헤일리 Hailey = new 헤일리("헤일리", 200, 60);
		 
		레아 Rea = new 레아("레아", 200, 30);
		 
		로빈 Robin = new 로빈("로빈", 200, 40);
		
		마리 Mary = new 마리("마리", 200, 50);
		
		거스 Gus = new 거스("거스", 200, 10);
		
		하비 Harvey = new 하비("하비", 200, 100);
		
		Rea.setWeapon(new 카메라("카메라", 40));
		Robin.setWeapon(new 도끼("도끼", 90));
		Mary.setWeapon(new 채찍("채찍", 100));
		
		Player [] ps= {Hailey, Rea, Mary, Gus, Harvey, Robin};

		Player attacker, target;

		Random r = new Random();

		int count = ps.length; 

		while(true) {

			int i = r.nextInt(count) ;


			int j = r.nextInt(count) ;


			if (i==j)  continue;

			attacker = ps[ i ];

			target = ps[ j ];
			
			System.out.print(attacker);
			
			int w = r.nextInt(2);
			
			if(w == 1 && attacker.getWeapon() != null) {
				attacker.attack(target, attacker.getWeapon());
				System.out.println(w);
			}
			else {
				attacker.attack(target);
			}

			if (target.getHp() <=0 ) {

				ps[j] = ps[count];

				count-- ;
			}

			Player.showStatus(ps);
			if (count < 1) break;

		}

		System.out.println(ps[0].getName() + " Win!");
	}
}
