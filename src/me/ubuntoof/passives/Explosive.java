package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.events.actors.ActorDeathEvent;
import me.ubuntoof.utils.Colorizer;

public class Explosive extends Passive
{

    public Explosive()
    {
        header = Colorizer.LIGHT_RED + "Explosive〉" + Colorizer.RESET;
    }

    public void activate()
    {
        String msg = (header + owner + " exploded!\n");
        for(Actor a : owner.getBattle().getLivingCombatants())
        {
            ActorDamageEvent ade = a.takeDamage(owner.getStamina()/3);
            msg += a + " was hit by the explosion, taking " + Colorizer.RED + ade + Colorizer.RESET + " damage.\n";
        }
        owner.getBattle().println(msg);
    }

    public void onEvent(ActorDeathEvent e) // called by the Actor class
    {
        if(e.actor == owner) activate();
    }
}
