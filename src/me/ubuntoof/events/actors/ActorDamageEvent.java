package me.ubuntoof.events.actors;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Cancellable;
import me.ubuntoof.events.Event;

public class ActorDamageEvent implements Event, Cancellable
{
    private boolean cancelled;
    private Actor actor;
    private int damage;
    private boolean trueDamage;

    public ActorDamageEvent(Actor actor, int dmg, boolean trueDamage)
    {
        this.actor = actor;
        damage = dmg;
        this.trueDamage = trueDamage;
    }

    public Actor getActor() { return actor; }
    public int getDamage() { return damage; }
    public boolean isTrueDamage() { return trueDamage; }

    public void setActor(Actor a) { actor = a; }
    public void setDamage(int dmg) { damage = dmg; }
    public void setTrueDamage(boolean b) { trueDamage = b; }

    @Override public void perform()
    {
        actor.takeRawDamage(damage, trueDamage);
    }

    @Override public boolean isDisallowed() { return cancelled; }
    @Override public void disallow() { cancelled = true; }

    public String toString() { return String.valueOf(damage); }
}
