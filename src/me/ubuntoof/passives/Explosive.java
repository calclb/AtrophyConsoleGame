package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActorDamageEvent;
import me.ubuntoof.events.actors.ActorDeathEvent;
import me.ubuntoof.utils.Colorizer;

public class Explosive extends Condition
{
    public static final String header = Colorizer.LIGHT_RED + "Explosive〉" + Colorizer.RESET;
    public static final String description = "When user is defeated, 1/3 of their base stamina is dealt as damage to all enemies.";

    public Explosive()
    {
        super("Explosive", "Inflicts damage on all enemies when the user is defeated.");
    }

    public String getHeader() { return Colorizer.LIGHT_RED + name + "〉" + Colorizer.RESET; }


    public void activate(Actor owner)
    {
        String msg = (header + owner + " exploded!\n");
        for(Actor a : owner.battle.getLivingCombatants())
        {
            ActorDamageEvent ade = a.takeDamage(owner.getStamina()/3);
            msg += a + " was hit by the explosion, taking " + Colorizer.RED + ade + Colorizer.RESET + " damage.\n";
        }
        owner.battle.println(msg);
    }

    public void onEvent(Actor owner, Event e) // called by the Actor class
    {

    }

    /**
     * @param owner The owner of the Condition
     * @param e The event that the Condition could activate by
     */
    @Override public void on(Actor owner, Event e)
    {
        if(!(e instanceof ActorDeathEvent)) return;
        ActorDeathEvent ade = (ActorDeathEvent) e;
        System.out.println(header + " saw ActorDeathEvent");
        if(ade.actor == owner) activate(owner);
    }
}
