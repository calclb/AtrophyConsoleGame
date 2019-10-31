package me.ubuntoof.Characters;


import me.ubuntoof.Action;

public class Actor {

    private Action[] actions;
    private int level;

    private double health;
    private double strength;
    private double speed;

    public Actor(Action[] actions, int level)
    {
        this.actions = actions;
        this.level = level;
    }

    public int getLevel() { return level; }

    public Action getAction(int index) { return actions[index]; }
    public void setAction(Action action, int index) { actions[index] = action; }

    public double getHealth() { return health; }
    public void setHealth(double health) { this.health = health; }

    public double getStrength() { return strength; }
    public void setStrength(double strength) { this.strength = strength; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public boolean isAlive() { return health > 0; }
    public void attack(Actor target, int actionIndex) {} // should be implemented by other classes
}
