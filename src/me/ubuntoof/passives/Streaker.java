package me.ubuntoof.passives;

import me.ubuntoof.characters.Actor;
import me.ubuntoof.characters.Player;
import me.ubuntoof.events.Event;
import me.ubuntoof.events.actors.ActionCommitEvent;
import me.ubuntoof.events.actors.ActorDeathEvent;
import me.ubuntoof.handlers.Battle;
import me.ubuntoof.utils.Colorizer;

public class Streaker extends Passive
{

    public Streaker(Actor owner)
    {
        super(owner);
        header = Colorizer.LIGHT_YELLOW + "Streaker〉" + Colorizer.RESET;
    }

    @Override public void activate()
    {
        Battle b = owner.getBattle();
        if(b.getOpposition(owner).length <= 1) return;
        String msg = (header + owner + "'s adrenaline skyrocketed!");
        b.println(msg);

        owner.onUserTurn();
        if(owner instanceof Player) ((Player) owner).onGlobalTurnStarted();
    }

    @Override public void notifyEvent(Event e) // called by the Actor class; TODO throws StackOverflowException
    {
        if(!owner.isAlive() || !(e instanceof ActionCommitEvent && e instanceof ActorDeathEvent)) return;
        ActionCommitEvent ace = (ActionCommitEvent) e;
        if(ace.user != owner || !ace.target.isAlive()) return;

        Battle battle = owner.getBattle();
        if(battle.getTeamOf(owner) != battle.getTeamOf(ace.target)) activate();
    }
}
