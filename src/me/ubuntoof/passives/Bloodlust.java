package me.ubuntoof.passives;

import me.ubuntoof.Stats;
import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActionCommitEvent;
import me.ubuntoof.events.actors.ActorDeathEvent;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.modifiers.StatModifier;
import me.ubuntoof.utils.Colorizer;

import java.text.NumberFormat;

public class Bloodlust extends Condition
{
    private boolean isOwnerUsingAction;

    private static final NumberFormat pf = NumberFormat.getPercentInstance();
    private static final double mod = 1.2d;
    private static final int hpHealed = 3;

    private static final int turnDuration = 2;
    private static final StatModifier sm = new StatModifier(Stats.STRENGTH, mod, turnDuration);

    public Bloodlust()
    {
        super("Bloodlust", "Applies " + sm + " and heals " + hpHealed + " health from user when they defeat an enemy.");
    }

    public String getHeader() { return Colorizer.LIGHT_RED + name + "〉" + Colorizer.RESET; }

    public void activate(Actor owner)
    {
        double mod = 1.2d;

        String msg = (getHeader() + owner + "'s " + Stats.STRENGTH.getName() + " increased by " + pf.format(mod - 1) + " for " + turnDuration + " turns, and healed "
                + Colorizer.GREEN + hpHealed + Colorizer.RESET + " health.");
        owner.battle.println(msg);
        owner.setHealth(Math.min(owner.getStamina(), owner.getHealth() + hpHealed));

        owner.addStatModifier((StatModifier) sm.clone());
    }

    /**
     * @param owner The owner of the Condition
     * @param e The event that the Condition could activate by
     */
    @Override
    public void on(Actor owner, Event e)
    {
        if(e instanceof ActionCommitEvent) isOwnerUsingAction = ((ActionCommitEvent) e).user == owner;
        if(e instanceof ActorDeathEvent)
        {
            ActorDeathEvent ade = (ActorDeathEvent) e;
            System.out.println("Bloodlust saw ActorDeathEvent");
            if(!(ade.actor == owner || owner.isAlive() || isOwnerUsingAction)) return;
            Battle b = owner.battle;
            if(owner.getTeam() != ade.actor.getTeam()) activate(owner);
        }
    }
}
