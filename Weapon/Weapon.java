package Weapon;

public class Weapon {
	private int power;
	private String name;
	
	public Weapon() {}
	
	public Weapon(String name, int power) {
		this.name = name;
		this.power = power;
	}
	
	public int getPower() {
		return this.power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
}