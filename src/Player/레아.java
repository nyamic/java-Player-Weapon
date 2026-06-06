package Player;

import Weapon.*;
import View.BattleView;
import java.util.List;
import java.util.ArrayList;

public class 레아 extends Player{
	private int cameraPower = 20;
	public 레아() {}
	
	public 레아(String name, int hp, int power) {
		super(name, hp, power);
	}
	
	public 레아(String name, int power, int hp, Weapon weapon) {
		super(name, hp, power);
	}
	public void 사진찍기(BattleView view) {
        Player[] ps = view.getAllPlayers();
        if (ps == null) return;

        int[] enemyIndex = this.getTargetTeamIndex(view);
        
        List<Integer> aliveTargets = new ArrayList<>();
        for (int idx : enemyIndex) {
            if (ps[idx].isAlive()) {
                aliveTargets.add(idx);
            }
        }

        view.appendLog("레아가 [사진찍기] 스킬을 사용했습니다.", "reset");
		view.appendLog("레아 : 찰칵찰칵! 역시 플래시를 켜고 사진을 찍어야 감성있어~!", "reset");
		view.appendLog("레아의 플래시가 상대팀을 눈을 아프게 합니다. 모든 상태팀의 hp가 " + cameraPower + "씩 감소합니다.", "reset");
		
        for (int idx : aliveTargets) {
            Player target = ps[idx];
            
            target.setHp(target.getHp() - cameraPower);
        }

        view.updateAllTeams(ps);	
	}

	public void 카메라청소하기(BattleView view) {
		view.appendLog("레아가 [카메라청소하기] 스킬을 사용했습니다.", "reset");
		view.appendLog("레아 : 쓱싹쓱싹~ 카메라를 청소했으니까 플래시가 더 잘 터지겠지?!", "reset");
		view.appendLog("레아의 청소 덕분에 플래시의 위력이 커졌습니다. 사진찍기 스킬 파워가 10 증가합니다.", "reset");
		
		cameraPower += 10;
	}
	
	public String[] getSkillNames() {
        return new String[]{"사진 찍기", "카메라 정리하기"};
    }

    @Override
    public void useSkill(int skillIndex, BattleView view) {
    	if (isSkillUsed(skillIndex)) {
            view.appendLog("이미 사용한 스킬입니다!", "reset");
            return;
        }

        if (skillIndex == 0) {
            사진찍기(view);
        } else if (skillIndex == 1) {
            카메라청소하기(view);
        }
        
        setSkillUsed(skillIndex, true);
    }
	
}
