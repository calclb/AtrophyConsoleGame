package me.ubuntoof.passives;

import me.ubuntoof.Stats;
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
    private boolean isOwnerUsingAction;

    public Bloodlust()
    {
        header = Colorizer.LIGHT_RED + "Bloodlust〉" + Colorizer.RESET;
    }

    public void activate()
    {
        double mod = 1.2d;

        int turnDuration = 2;
        int hpHealed = (owner.getStamina()/10);

        String msg = (header + owner + "'s " + Stats.STRENGTH.getName() + " increased by " + pf.format(mod - 1) + " for " + turnDuration + " turns, and healed "
                + Colorizer.GREEN + hpHealed + Colorizer.RESET + " health.");
        owner.getBattle().println(msg);
        owner.setHealth(Math.min(owner.getStamina(), owner.getHealth() + hpHealed));
        owner.addStatModifier(new StatModifier(Stats.STRENGTH, mod, turnDuration));
    }

    public void onEvent(ActionCommitEvent e)
    {
        isOwnerUsingAction = e.user == owner;
    }

    public void onEvent(ActorDeathEvent e) // called by the Actor class
    {
        if(!(e.actor == owner || owner.isAlive() || isOwnerUsingAction)) return;
        Battle battle = owner.getBattle();
        if(battle.getTeamOf(owner) != battle.getTeamOf(e.actor)) activate();
    }
}
