package Player;

public class 거스 extends Player{
	public 거스() {}
	
	public 거스(String name, int hp, int power) {
		super(name, hp, power);
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
