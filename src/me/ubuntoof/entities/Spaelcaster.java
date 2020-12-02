package me.ubuntoof.entities;

import me.ubuntoof.StatContainer;
import me.ubuntoof.actions.Rejuvenate;
import me.ubuntoof.actions.spaelcaster.Hex;
import me.ubuntoof.actions.spaelcaster.Sting;

public class Spaelcaster extends Entity
{
    public Spaelcaster(int level)
    {
        super("Spaelcaster", level, new StatContainer(12, 3, 2));
        addActions(Sting.class, Rejuvenate.class, Hex.class);
    }

    public Spaelcaster()
    {
        this(5);
    }
}
