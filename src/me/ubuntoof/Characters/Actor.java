package me.ubuntoof.Characters;


import me.ubuntoof.Action;

public class Actor {

    private Action[] actions;
    private int level;

    private double maxHealth;
    private double health;
    private double strength;
    private double speed;

    public Actor(Action[] actions, int level)
    {
        this.actions = actions;
        this.level = level;
    }

    public int getLevel() { return level; }


    public int getActionSize() { return actions.length; }
    public Action getAction(int index) { return actions[index]; }
    protected void setAction(Action action, int index) { actions[index] = action; }

    public double getMaxHealth() { return maxHealth; }
    public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }

    public double getHealth() { return health; }
    public void setHealth(double health) { this.health = health; }
    public void takeDamage(int dmg) { health -= dmg; }

    public double getStrength() { return strength; }
    public void setStrength(double strength) { this.strength = strength; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public boolean isAlive() { return health > 0; }

    public void doAction(int actionIndex, Actor target) { actions[actionIndex].commit(this, target); }
}
