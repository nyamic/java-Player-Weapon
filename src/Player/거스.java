package Player;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class 거스 extends Player {
	private boolean isDamageDealer = false;
	private static final int DAMAGE_DEALER_POWER = 50;

	public 거스() {
	}

	public 거스(String name, int hp, int power) {
		super(name, hp, power);
	}

	/// <summary>딜러로 전환(power값 변경)</summary>
	public void convertToDamageDealer() {
		if (!isDamageDealer) {
			isDamageDealer = true;
			this.setPower(DAMAGE_DEALER_POWER);
			System.out.println(getName() + "가 딜러로 전환됩니다! (공격력 " + DAMAGE_DEALER_POWER + ")");
		}
	}

	public boolean isDamageDealer() {
		return isDamageDealer;
	}

	@Override
	public void useSkill(Player[] ps, Player defaultTarget) {
	    if (isDamageDealer) {
	        attack(defaultTarget);
	        return;
	    }
	    Player target = defaultTarget;
	    if (target == null) {
	        List<Player> teammates = Player.getAliveTeammates(ps, this.team);
	        List<Player> candidates = new ArrayList<>();
	        for (Player p : teammates) {
	            if (p == this) continue;
	            if (p instanceof 거스) continue;
	            candidates.add(p);
	        }
	        if (candidates.isEmpty()) return;
	        target = candidates.get(new Random().nextInt(candidates.size()));
	    }
	    버프주기(target);
	}

	public void attack(Player target) {
		버프주기(target);
	}

	public void 버프주기(Player target) {
		System.out.println(this.getName() + "가 " + target.getName() + "의 power를 10 증가시킵니다.");

		target.applyBuff(new PowerBuff(this.getPower()));
	}

	public void 허허허사람좋은웃음짓기() {

	}

	public void 미니게임하기() {

	}

}
