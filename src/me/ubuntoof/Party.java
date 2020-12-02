package me.ubuntoof;

import me.ubuntoof.entities.Entity;
import me.ubuntoof.utils.Colorizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Party
{
    public final String icon;
    public final String format;
    public final List<Entity> entities = new ArrayList<>(); // TODO determine if `public` modifier is necessary
    public final String name;

    public Party(String n, Entity... es)
    {
        name = n;
        icon = Colorizer.Icons.getRandomValue();
        format = Colorizer.Colors.getRandomValue();
        entities.addAll(Arrays.asList(es));
    }

    public Party(String n, String icon, String format, Entity... es)
    {
        name = n;
        this.icon = icon;
        this.format = format;
        entities.addAll(Arrays.asList(es));
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(format + icon + " " + name + " | ");
        for(int i = 0; i < entities.size(); i++)
        {
            sb.append(entities.get(i)).append(i < entities.size() - 1 ? ", " : "");
        }
        return sb.toString();
    }
}
