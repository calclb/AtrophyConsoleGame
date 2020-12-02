package me.ubuntoof.entities;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.druid.SecondWind;
import me.ubuntoof.actions.druid.SummonDruidimite;
import me.ubuntoof.actions.druid.druidimite.Bonk;

public class Druid extends Entity
{
    public Druid(int level)
    {
        super("Druid", level, new StatContainer(15, 2,7)); // TODO balance
        addActions(SummonDruidimite.class, SecondWind.class);
    }

    public Druid()
    {
        this(5);
    }
}
