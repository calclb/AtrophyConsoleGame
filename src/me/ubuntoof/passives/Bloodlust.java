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

public class Bloodlust extends Passive
{
    private final NumberFormat pf = NumberFormat.getPercentInstance();

    public Bloodlust(Actor owner)
    {
        super(owner);
        header = Colorizer.LIGHT_RED + "Bloodlust〉" + Colorizer.RESET;
    }

    @Override public void activate()
    {
        double mod = 1.2d;

        int turnDuration = 2;
        int hpHealed = (owner.getMaxHealth()/10);

        String msg = (header + owner + "'s " + Stats.STRENGTH.getName() + " increased by " + pf.format(mod - 1) + " for " + turnDuration + " turns, and healed "
                + Colorizer.GREEN + hpHealed + Colorizer.RESET + " health.");
        owner.getBattle().println(msg);
        owner.setBaseHealth(owner.getHealth() + hpHealed);
        owner.addStatModifier(new StatModifier(Stats.STRENGTH, mod, turnDuration));
    }

    @Override public void notifyEvent(Event e) // called by the Actor class
    {
        if(!owner.isAlive() || !(e instanceof ActionCommitEvent)) return;
        ActionCommitEvent ace = (ActionCommitEvent) e;
        if(ace.user != owner || ace.target.isAlive()) return;

        Battle battle = owner.getBattle();
        if(battle.getTeamOf(owner) != battle.getTeamOf(ace.target)) activate();
    }
}
