package Player;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class 하비 extends Player{
	private boolean isDamageDealer = false;
	private static final int DAMAGE_DEALER_POWER = 50;

	public 하비() {}
	
	public 하비(String name, int hp, int power) {
		super(name, hp, power);
	}

	///<summary>딜러로 전환(power값 변경)</summary>
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
		//딜러전환o --> 적 공격
		if (isDamageDealer) {
			attack(defaultTarget);
			return;
		}

		//딜러전환x --> 랜덤 아군 힐
		List<Player> teammates = Player.getAliveTeammates(ps, this.team);

		List<Player> targets = new ArrayList<>();
		for (Player p : teammates) {
			if (p == this) continue;
			targets.add(p);
		}

		if (!targets.isEmpty()) {
			Random r = new Random();
			Player wounded = targets.get(r.nextInt(targets.size()));
			치유하기(wounded);
		}
	}

	public void attack(Player target) {
		System.out.println(getName() + "가 " + target.getName() + "을 공격합니다. (데미지: " + getPower() + ")");
		target.setHp(target.getHp() - this.getPower());
	}
	
	public void 치유하기(Player player) {
		System.out.println(this.getName() + "가 " + player.getName() + "의 hp를 100 회복시킵니다.");
		player.setHp(player.getHp() + this.getPower());
	}
	
	public void 책읽기() {
	}
	
	public void 공부하기() {
	}

}
