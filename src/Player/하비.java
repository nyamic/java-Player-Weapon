package Player;
import java.util.List;
import java.util.Random;
import View.BattleView;
import java.util.ArrayList;

public class 하비 extends Player{
	private boolean isDamageDealer = false;
	private static final int DAMAGE_DEALER_POWER = 50;
	private final BattleView view;

	public 하비() {
		this.view = new BattleView();}
	
	public 하비(String name, int hp, int power) {
		super(name, hp, power);
		this.view = new BattleView();
	}

	///<summary>딜러로 전환(power값 변경)</summary>
	public void convertToDamageDealer() {
		if (!isDamageDealer) {
			isDamageDealer = true;
			this.setPower(DAMAGE_DEALER_POWER);
			view.appendLog(getName() + "가 딜러로 전환됩니다! (공격력 " + DAMAGE_DEALER_POWER + ")", "reset");
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
	    // defaultTarget이 있으면 그 대상, 없으면 랜덤 아군
	    Player target = defaultTarget;
	    if (target == null) {
	        List<Player> teammates = Player.getAliveTeammates(ps, this.team);
	        List<Player> candidates = new ArrayList<>();
	        for (Player p : teammates) {
	            if (p == this) continue;
	            candidates.add(p);
	        }
	        if (candidates.isEmpty()) return;
	        target = candidates.get(new Random().nextInt(candidates.size()));
	    }
	    치유하기(target);
	}

	public void attack(Player target) {
		view.appendLog(getName() + "가 " + target.getName() + "을 공격합니다. (데미지: " + getPower() + ")", this.team);
		target.setHp(target.getHp() - this.getPower());
	}
	
	public void 치유하기(Player player) {
		view.appendLog(this.getName() + "가 " + player.getName() + "의 hp를 100 회복시킵니다.", this.team);
		player.setHp(player.getHp() + this.getPower());
		if(player.getHp() >= 200) {
			player.setHp(200);
			view.appendLog(player.getName() + "의 체력이 최대로 회복되어 더 이상 회복은 불가합니다.", "reset");
		}
	}
	
	public void 책읽기(BattleView view) {
		view.appendLog("하비가 [책 읽기] 스킬을 사용했습니다.", "reset");
		view.appendLog("하비 : 역시 마음 안정에는 책만 한 게 없지. 다시 여유로워졌어!", "reset");
		view.appendLog("하비가 책을 읽으며 안정을 찾았습니다. 하비의 Hp가 20 증가합니다.", "reset");
		
		this.setHp(this.getHp() + 20);
	}
	
	public void 공부하기(BattleView view) {
		Player[] ps = view.getAllPlayers();

        int[] enemyIndex = this.getMyTeamIndex(view);

        List<Integer> aliveTargets = new ArrayList<>();
        for (int idx : enemyIndex) {
            if (ps[idx].isAlive()) {
                aliveTargets.add(idx);
            }
        }

        view.appendLog("하비가 [공부하기] 스킬을 사용했습니다.", "reset");
		view.appendLog("하비 : 오호, 이렇게도 사람들을 치유할 수 있군!!", "reset");
		view.appendLog("하비가 새로운 치료술을 학습했습니다. 같은 팀원들의 Hp를 즉시 30 회복시켜줍니다.", "reset");
        
        for (int idx : aliveTargets) {
            Player target = ps[idx];
            
            target.setHp(target.getHp() + 30);
            if(target.getHp() >= 200) {
    			target.setHp(200);
    			System.out.println(target.getName() + "의 체력이 최대로 회복되어 더 이상 회복은 불가합니다.");
    		}
        }

        view.updateAllTeams(ps);
	}
	
	public String[] getSkillNames() {
        return new String[]{"책읽기", "공부하기"}; // 레아의 스킬 이름들
    }

    @Override
    public void useSkill(int skillIndex, BattleView view) {
        if (skillIndex == 0) {
            책읽기(view);
        } else if (skillIndex == 1) {
            공부하기(view);
        }
    }

}
