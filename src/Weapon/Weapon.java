package Weapon;

import java.util.Random;

public class Weapon {
	private int power;
	private String name;
	private double critChance;
	private double critMultiplier;
	private boolean lastHitWasCrit = false;
	private static final Random random = new Random();
	
	public Weapon() {}
	
	public Weapon(String name, int power, double critChance, double critMultiplier) {
		this.name = name;
		this.power = power;
		this.critChance = critChance;
		this.critMultiplier = critMultiplier;
	}

	private boolean rollCrit() {
		return random.nextDouble() < critChance;
	}

	public int getFinalDamage() {
		lastHitWasCrit = rollCrit();
		return lastHitWasCrit ? (int)(power * critMultiplier) : power;
	}

	public boolean isLastHitCrit() {
		return lastHitWasCrit;
	}
	
	public int getPower() {
		return this.power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	public String getName() { return name; }
	public double getCritChance() { return critChance; }
	public double getCritMultiplier() { return critMultiplier; }
}