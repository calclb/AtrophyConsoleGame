package me.ubuntoof.events.state;

import me.ubuntoof.events.Event;
import me.ubuntoof.handlers.Battle;

public class BattleStartEvent extends Event
{
    public final Battle battle;

    public BattleStartEvent(Battle battle)
    {
        this.battle = battle;
        perform();
    }

    @Override public void perform()
    {

    }
}
