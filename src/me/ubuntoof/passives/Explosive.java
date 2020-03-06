package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActorDeathEvent;
import me.ubuntoof.utils.Colorizer;

public class Explosive extends Passive
{

    public Explosive()
    {
        header = Colorizer.LIGHT_RED + "Explosive〉" + Colorizer.RESET;
    }

    @Override public void activate()
    {
        String msg = (header + owner + " exploded!\n");
        for(Actor a : owner.getBattle().getLivingCombatants())
        {
            int dmg = a.takeDamage(owner.getMaxHealth()/3);
            msg += a + " was hit by the explosion, taking " + Colorizer.RED + dmg + Colorizer.RESET + " damage.\n";
        }
        owner.getBattle().println(msg);
    }

    @Override public void notifyEvent(Event e) // called by the Actor class
    {
        if(e instanceof ActorDeathEvent && ((ActorDeathEvent) e).actor == owner) activate();
    }
}
