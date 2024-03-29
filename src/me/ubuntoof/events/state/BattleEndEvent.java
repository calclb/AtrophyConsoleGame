package me.ubuntoof.events.state;

import me.ubuntoof.Team;
import me.ubuntoof.events.Event;
import me.ubuntoof.handlers.Battle;

public class BattleEndEvent implements Event
{
    public Battle battle;
    public Team winningTeam;

    public BattleEndEvent(Battle battle, Team winningTeam)
    {
        this.battle = battle;
        this.winningTeam = winningTeam;
    }
}
