package me.ubuntoof.modifiers.ailments;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.ailments.AilmentTriggerEvent;
import me.ubuntoof.events.state.GlobalTurnEndEvent;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.utils.Colorizer;

public class Poison extends Ailment
{
    private int turns = 0;

    public Poison()
    {
        super(Colorizer.PURPLE + "\uD83D\uDC0D", Colorizer.PURPLE + "Poison", "Inflicts damage equal to 6.25% of the victim's health, increasing in damage per turn.");
    }

    public Poison(int turns)
    {
        this();
        setDurationInTurns(turns);
    }

    @Override public void applyEffects(Actor actor)
    {
        int dmg = (int)Math.max(actor.getStamina()/(Math.max(16d-2*turns, 3d)), 1);
        actor.battle.println(icon + " " + Colorizer.PURPLE + actor.name + " took " + Colorizer.RED + dmg + Colorizer.PURPLE + " damage to " + name + "." + Colorizer.RESET);
        actor.takeDamage(dmg);
        turns++;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Poison;
    }

    /**
     * @param owner The Actor to run with the Condition
     * @param e     The event to analyze
     */
    @Override public void on(Actor owner, Event e)
    {
        if(!(e instanceof GlobalTurnEndEvent)) return;
        owner.battle.registerEvent(new AilmentTriggerEvent(this, owner));
    }
}
