package me.ubuntoof.events.state;

import me.ubuntoof.events.Event;
import me.ubuntoof.handlers.Battle;

public class BattleStartEvent implements Event
{
    public final Battle battle;

    public BattleStartEvent(Battle battle)
    {
        this.battle = battle;
        perform();
    }
}
