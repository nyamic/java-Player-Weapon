package Player;

public class 하비 extends Player{
	public 하비() {}
	
	public 하비(String name, int hp, int power) {
		super(name, hp, power);
	}
	
	public void attack(Player target) {
		치유하기(target);
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
