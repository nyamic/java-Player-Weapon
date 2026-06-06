package Player;

import Weapon.Weapon;
import java.util.ArrayList;
import java.util.List;
import View.BattleView;

public class Player implements Attackable {
    private String name;
    private Weapon weapon = null;
    private int hp;
    protected int basePower; //기본공격력
    private PowerBuff activeBuff = null;  // 현재 걸린 버프
    public String team;
    private boolean[] isSkillUsed;
    
    private final BattleView view;
    
    public Player() {
    		this.view = new BattleView();
			System.out.println("플레이어 생성!");
    }
    
    public Player(String name, int hp, int power) {
    		this.view = new BattleView();
			System.out.println("플레이어 생성!");
    		this.name = name;
    		this.basePower = power;
    		this.hp = hp;
    }

    public void attack(){
    }
    
    public int getEffectivePower() {
        if (activeBuff != null) {
            return basePower + activeBuff.getAmount();
        }
        return basePower;
    }

    public void applyBuff(PowerBuff buff) {
        this.activeBuff = buff;
        view.appendLog(name + ": 버프 +" + buff.getAmount() + " 적용됨", "reset");
        this.setPower(basePower + buff.getAmount());
    }
    
    public void attack(Player target) {
        basePower = getEffectivePower();
        
        target.setHp(target.getHp() - this.basePower);

        if (activeBuff != null) {
            activeBuff = null;
        }
    }
    
    public void attack(Player target, Weapon weapon) {
		int damage = weapon.getFinalDamage();

		if (weapon.isLastHitCrit()) {
			view.appendLog("치명타! " + this.getName() + " 가 " + target.getName() + " 를 " + damage + " 데미지로 강타합니다! (" + weapon.getCritMultiplier() * 100 + "% 데미지배율)", "reset");
		} else {
			view.appendLog(this.getName() + " 가 " + weapon.getName() + " 로 " + target.getName() + " 를 " + damage + " 데미지로 공격합니다.", "reset");
		}
		target.setHp(target.getHp() - damage);
    }

	public void useSkill(Player[] ps, Player defaultTarget) {
		attack(defaultTarget);
	}

	public static boolean hasNormalAttacker(Player[] ps, String team) {
		for (Player p : ps) {
			if (p.isAlive() && p.team.equals(team)) {
				if (!(p instanceof 하비) && !(p instanceof 거스)) {
					return true;
				}
			}
		}
		return false;
	}

	public static List<Player> getAliveTeammates(Player[] ps, String team) {
		List<Player> list = new ArrayList<>();
		for (Player p : ps) {
			if (p.isAlive() && p.team.equals(team)) {
				list.add(p);
			}
		}
		return list;
	}
	
	//TargetTeamIndex 가져오기
	public int[] getTargetTeamIndex(BattleView view) {
        Player[] ps = view.getAllPlayers();
        if (ps == null) return new int[0];

        int myIndex = -1;
        for (int i = 0; i < ps.length; i++) {
            if (ps[i] == this) {
                myIndex = i;
                break;
            }
        }

        if (myIndex % 2 == 0) {
            return new int[]{1, 3, 5};
        } 
        else {
            return new int[]{0, 2, 4};
        }
    }
	
	//MyTeamIndex 가져오기
	public int[] getMyTeamIndex(BattleView view) {
	    Player[] ps = view.getAllPlayers();
	    if (ps == null) return new int[0];

	    int myIndex = -1;
	    for (int i = 0; i < ps.length; i++) {
	        if (ps[i] == this) {
	            myIndex = i;
	            break;
	        }
	    }

	    if (myIndex == -1) return new int[0];

	    if (myIndex % 2 == 0) {
	        return new int[]{0, 2, 4};
	    } 
	    else {
	        return new int[]{1, 3, 5};
	    }
	}
	
	public boolean isSkillUsed(int skillIdx) {
        if (isSkillUsed == null) {
            isSkillUsed = new boolean[getSkillNames().length];
        }
        return isSkillUsed[skillIdx];
    }
	
	public void setSkillUsed(int skillIdx, boolean used) {
        if (isSkillUsed == null) {
            isSkillUsed = new boolean[getSkillNames().length];
        }
        this.isSkillUsed[skillIdx] = used;
    }
	
	public String[] getSkillNames() { return new String[0]; }

	//플레이어 생존 확인 메서드
	public boolean isAlive() {
		return this.hp > 0;
	}
	
	public void useSkill(int skillIndex, BattleView view) {
    }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Weapon getWeapon() {
		return weapon;
	}
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getPower() {
		return basePower;
	}
	public void setPower(int power) {
		this.basePower = power;
	}
}
