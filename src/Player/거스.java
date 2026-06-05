package Player;

import java.util.List;
import java.util.Random;

import View.BattleView;

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

	@Override
	public void attack(Player target) {
	    if (isDamageDealer) {
	        System.out.println(getName() + "가 " + target.getName() + "을 공격합니다. (데미지: " + getPower() + ")");
	        target.setHp(target.getHp() - this.getPower());
	    } else {
	        버프주기(target);
	    }
	}

	public void 버프주기(Player target) {
		System.out.println(this.getName() + "가 " + target.getName() + "의 power를 10 증가시킵니다.");

		target.applyBuff(new PowerBuff(this.getPower()));
	}

	public void 허허허사람좋은웃음짓기(BattleView view) {
		view.appendLog("거스가 [허허허~ 사람좋은웃음짓기] 스킬을 사용했습니다.");
		view.appendLog("거스 : 허허허~ 허허허~");
		view.appendLog("거스의 웃음이 모두에게 힘을 줍니다. 팀원들의 hp가 30씩 상승합니다.");
		
		Player[] ps = view.getAllPlayers();

        int[] enemyIndex = this.getMyTeamIndex(view);

        List<Integer> aliveTargets = new ArrayList<>();
        for (int idx : enemyIndex) {
            if (ps[idx].isAlive()) {
                aliveTargets.add(idx);
            }
        }

        // 스킬 로그 출력 및 광역 데미지 적용
        view.appendLog("📸 [스킬 발동] " + getName() + "이(가) [사진 찍기]를 시전했습니다!");
        
        for (int idx : aliveTargets) {
            Player target = ps[idx];
            
            target.setHp(target.getHp() + 30);
        }

        view.updateAllTeams(ps);
	}

	public void 주점운영하기(BattleView view) {
		view.appendLog("거스가 [주점운영하기] 스킬을 사용했습니다.");
		view.appendLog("북적북적(역시 거스 주점이 제일 좋아!) 하하호호 시끌벅적");
		view.appendLog("거스 : 행복해하는 사람들을 보니 나까지 다 힘이 나는 기분인걸!");
		view.appendLog("거스가 사람들의 행복한 기운을 받아 힘을 냅니다! 거스의 파워가 40 증가합니다.");
		
		this.setPower(this.getPower() + 40);

	}
	
	public String[] getSkillNames() {
        return new String[]{"허허허~ 사람좋은웃음짓기", "주점 운영하기"};
    }

    @Override
    public void useSkill(int skillIndex, BattleView view) {
        if (skillIndex == 0) {
            허허허사람좋은웃음짓기(view);
        } else if (skillIndex == 1) {
            주점운영하기(view);
        }
    }

}
