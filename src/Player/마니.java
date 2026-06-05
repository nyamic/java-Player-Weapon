package Player;

import View.BattleView;

public class 마니 extends Player{
	public 마니() {}
	
	public 마니(String name, int hp, int power) {
		super(name, hp, power);
	}

	public void 밥주기(BattleView view) {
		view.appendLog("마니가 [동물 밥주기] 스킬을 사용했습니다.");
		view.appendLog("소 : 음메(너무 맛있어서 행복하소)");
		view.appendLog("마니 : 소가 행복해하니까 나도 행복해진다 >_<");
		view.appendLog("마니가 행복해져서 힘이 더 세졌습니다. 마니의 power가 15 증가합니다.");
		
		this.setPower(this.getPower() + 15);
	}
	
	public void 아이들놀아주기(BattleView view) {
		Player[] ps = view.getAllPlayers();
        if (ps == null) return;

        int[] enemyIndex = this.getTargetTeamIndex(view);

        for (int idx : enemyIndex) {
            if (ps[idx].isAlive()) {
                ps[idx].setHp(ps[idx].getHp() - 40);
                break;
            }
        }

		view.appendLog("마니가 [아이들 놀아주기] 스킬을 사용했습니다.");
		view.appendLog("아이들 : 마니 이모 괴롭히지마!");
		view.appendLog("아이들이 자신들을 놀아준 마니가 공격을 당하자 화가 났습니다. 상대 팀원 중 한 명에게 40의 데미지를 입힙니다.");

        view.updateAllTeams(ps);
	}
	
	public String[] getSkillNames() {
        return new String[]{"동물 밥주기", "아이들 놀아주기"};
    }

    @Override
    public void useSkill(int skillIndex, BattleView view) {
        if (skillIndex == 0) {
            밥주기(view);
        } else if (skillIndex == 1) {
            아이들놀아주기(view);
        }
    }

}
