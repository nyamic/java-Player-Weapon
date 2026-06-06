package Player;

import java.util.ArrayList;
import java.util.List;
import View.BattleView;

public class 헤일리 extends Player{
	public 헤일리() {}
	
	public 헤일리(String name, int hp, int power) {
		super(name, hp, power);
	}
	
	public void 머리빗기(BattleView view) {
		view.appendLog("헤일리가 [머리 빗기] 스킬을 사용했습니다.");
		view.appendLog("헤일리 : 싸울 때는 머리가 망가져서 싫단 말이야~ 이제야 내 머릿결이 좀 볼만해졌네!");
		view.appendLog("헤일리가 머리 정리로 자신감이 올라갔습니다. 헤일리의 power가 20 증가합니다.");
		
		this.setPower(this.getPower() + 20);
	}
	
	public void 거울보기(BattleView view) {
		Player[] ps = view.getAllPlayers();

        int[] enemyIndex = this.getMyTeamIndex(view);

        List<Integer> aliveTargets = new ArrayList<>();
        for (int idx : enemyIndex) {
            if (ps[idx].isAlive()) {
                aliveTargets.add(idx);
            }
        }

        view.appendLog("헤일리가 [거울 보기] 스킬을 사용했습니다.");
		view.appendLog("헤일리 : 아임 어 뷰티풀 걸! 미모는 나의 무기! 너희들도 내 무기 나눠줄게~");
		view.appendLog("헤일리가 본인의 미모를 이용해 같은 팀의 power를 15씩 증가시킵니다.");
        
        for (int idx : aliveTargets) {
            Player target = ps[idx];
            
            target.setPower(target.getPower() + 15);
        }

        view.updateAllTeams(ps);		
	}
	
	public String[] getSkillNames() {
        return new String[]{"머리 빗기", "거울 보기"}; // 레아의 스킬 이름들
    }

    @Override
    public void useSkill(int skillIndex, BattleView view) {
        if (skillIndex == 0) {
            머리빗기(view);
        } else if (skillIndex == 1) {
            거울보기(view);
        }
    }
	
}
