package me.ubuntoof.Passives;

import me.ubuntoof.Characters.Actor;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;

public class Inflame extends Passive {

    boolean active;

    public Inflame(Actor owner) {
        super(owner);
        header = Colorizer.GRAY + "Inflame〉" + Colorizer.RESET;
        owner.getActorInteractions().add(this);
    }

    @Override
    public void activate()
    {
        active = true;
        Stats randomStat = Stats.VALUES.get(random.nextInt(Stats.SIZE));
        double mod = 1.2d;
        int turns = 0;

        System.out.println(header + owner + "'s " + randomStat.getName() + " increased by " + pf.format(mod) + " for " + turns + " " + (turns == 1 ? " turn." : "turns."));
        owner.getStatModifiers().add(new StatModifier(randomStat, mod, turns));
    }

    @Override
    public void onGlobalTurnEnded() { activate(); }

    @Override
    public void onBattleEnded() {}

    @Override
    public void onAnyoneHealed() {}

    @Override
    public void onAnyoneAttacked() {}
}
