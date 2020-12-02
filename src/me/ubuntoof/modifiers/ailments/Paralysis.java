package me.ubuntoof.modifiers.ailments;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.ailments.AilmentTriggerEvent;
import me.ubuntoof.events.state.GlobalTurnEndEvent;
import me.ubuntoof.events.state.TurnChangeEvent;
import me.ubuntoof.modifiers.Ailment;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

import java.util.Random;

public class Paralysis extends Ailment
{
    private final Random random = new Random();

    public Paralysis()
    {
        super(Colorizer.YELLOW + "\uD83D\uDDF2", Colorizer.YELLOW + "Paralysis", "Reduces target speed and has a chance of cancelling their actions.");
    }

    public Paralysis(int turns)
    {
        this();
        setDurationInTurns(turns);
    }

    @Override public void applyEffects(Actor actor)
    {
        actor.addStatModifier(new StatModifier(Stats.AGILITY, (2/3d), 1));
        if(!actor.getEligibleToAct()) return;
        if(random.nextDouble() < 0.25d)
        {
            actor.setEligibleToAct(false);
            actor.battle.println(icon + " " + Colorizer.YELLOW + actor.name + "'s nerves rendered unresponsive." + Colorizer.RESET);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Paralysis;
    }

    /**
     * @param owner The Actor to run with the Condition
     * @param e     The event to analyze
     */
    @Override
    public void on(Actor owner, Event e)
    {
        if(!(e instanceof TurnChangeEvent)) return;
        AilmentTriggerEvent ate = new AilmentTriggerEvent(this, owner);
        owner.battle.registerEvent(ate);
    }
}
