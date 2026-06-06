package Player;

import java.util.ArrayList;
import java.util.List;

import View.BattleView;
import Weapon.*;

public class 로빈 extends Player{
	public 로빈() {}
	
	public 로빈(String name, int hp, int power) {
		super(name, hp, power);
	}
	
	public 로빈(String name, int power, int hp, Weapon weapon) {
		super(name, hp, power);
	}
	
	public void 가구만들기(BattleView view) {			
		view.appendLog("로빈이 [가구 만들기] 스킬을 사용합니다.");
		view.appendLog("로빈 : 안되겠어. 가구를 만들어서 방어막으로 사용해야겠어(뚝딱뚝딱)");
		view.appendLog("로빈이 가구들로 방어막을 만들었습니다. 로빈의 Hp가 50 증가합니다.");
		
		this.setHp(this.getHp() + 50);
	}
	
	
	public void 장작패기(BattleView view) {
		Player[] ps = view.getAllPlayers();
        if (ps == null) return;

        int[] enemyIndex = this.getTargetTeamIndex(view);
        
        List<Integer> aliveTargets = new ArrayList<>();
        for (int idx : enemyIndex) {
            if (ps[idx].isAlive()) {
                aliveTargets.add(idx);
            }
        }

        view.appendLog("로빈이 [장작 패기] 스킬을 사용합니다.");
		view.appendLog("어이쿠! 실수로 장작이 저기까지 날아갔네??");
		view.appendLog("로빈의 장작이 날아가서 모두에게 상처를 냈습니다. 상대 팀원들의 hp가 10씩 감소합니다.");
		
        for (int idx : aliveTargets) {
            Player target = ps[idx];
            
            target.setHp(target.getHp() - 10);
        }

        view.updateAllTeams(ps);
		
	}
	
	public String[] getSkillNames() {
        return new String[]{"가구 만들기", "장작 패기"};
    }

    @Override
    public void useSkill(int skillIndex, BattleView view) {
        if (skillIndex == 0) {
            가구만들기(view);
        } else if (skillIndex == 1) {
            장작패기(view);
        }
    }
}
