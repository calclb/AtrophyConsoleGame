package me.ubuntoof.entities;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.goblin.Guard;
import me.ubuntoof.actions.goblin.Intimidate;
import me.ubuntoof.actions.goblin.Pummel;

public class Goblin extends Entity
{
    public Goblin(int level)
    {
        super("Goblin", level, new StatContainer(10, 4, 1));
        addActions(Intimidate.class, Guard.class, Pummel.class);
    }

    public Goblin()
    {
        this(5);
    }

}
