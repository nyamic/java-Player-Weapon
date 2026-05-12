package Player;

import Weapon.Weapon;

public class Player implements Attackable {
    private String name;
    private Weapon weapon = null;
    private int hp;
    protected int basePower; //기본공격력
    private PowerBuff activeBuff = null;  // 현재 걸린 버프
    public String team;
    
    public Player() {
    		System.out.println("플레이어 생성!");
    }
    
    public Player(String name, int hp, int power) {
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
        System.out.println(name + ": 버프 +" + buff.getAmount() + " 적용됨");
        this.setPower(basePower + buff.getAmount());
    }
    
    public void attack(Player target) {
        basePower = getEffectivePower();
        
        System.out.println(this.getName() + "가 " + target + "을 공격합니다.");
        
        target.setHp(target.getHp() - this.basePower);

        if (activeBuff != null) {
            activeBuff = null;
        }
    }
    
    public void attack(Player target, Weapon weapon) {
		int damage = weapon.getFinalDamage();

		if (weapon.isLastHitCrit()) {
			System.out.printf("치명타! %s 가 %s 를 %d 데미지로 강타합니다! (%.0f%% 데미지배율)",
					this.getName(), target.getName(), damage, weapon.getCritMultiplier() * 100);
		} else {
			System.out.printf("%s 가 %s 로 %s 를 %d 데미지로 공격합니다.",
					this.getName(), weapon.getName(), target.getName(), damage);
		}

		target.setHp(target.getHp() - damage);
    }

	//플레이어 생존 확인 메서드
	public boolean isAlive() {
		return this.hp > 0;
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

	//한 플레이어 정보보기
    public void showStatus() {
    		System.out.printf("%s(%3d)", this.getName(), this.basePower);
    		for(int i = 0; i < this.hp / 10; i++) {
    			System.out.print("#");
    		}
    		System.out.println();
    }
    
    //여러 플레이어 정보보기
    public static void showStatus(Player[] player) {
		for (Player p : player) {
			if (p.isAlive()) {  // 살아있는 캐릭터만 출력
				System.out.println(p.getName() + " | HP: " + p.getHp() + " | Team: " + p.team);
			}
		}
    }
    
    
}
