package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;
import me.ubuntoof.Utils.Colorizer;
import me.ubuntoof.Utils.TextFormatter;

import java.util.Random;

public class Goblin extends Actor {

    private static Action a1 = new Action(ActionType.ATTACK, "Pummel", "Deals damage equal to half the user's level.", false, true) {

        @Override public void commit(Actor user, Actor target) {

            int dmg = target.takeDamage((int)(user.getLevel() * 0.5d), false);
            System.out.println(user + " used " + a1.getName() + " on " + (target == user ? "itself" : target.getName()) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
        }
    };

    private static Action a2 = new Action(ActionType.STATUS, "Guard", "Reduces damage taken by 50% until next turn.", true, false) {

        @Override public void commit(Actor user, Actor target)
        {

            System.out.println(user + " used " + a2.getName() + " on " + (target == user ? "itself" : target.getName()) + ", increasing defense by 50% until the next turn.");
            user.getStatModifiers().add(new StatModifier(Stats.DEFENSE, 1.5d, 0));
            // TODO use listeners to reduce damage
        }
    };

    private static Action[] actions = new Action[]{a1, a2};

    public Goblin(int level) {

        super("Goblin", actions, level);
        setBaseMaxHealth(2 + level);
        setBaseHealth(getBaseMaxHealth());
        setBaseStrength((int)(1 + Math.sqrt(2*level)));
        setBaseDefense(level/5);
        setBaseArmor((int)(1 + Math.sqrt(level)));
        setBaseSpeed((int)(2 + Math.sqrt(level)));
    }

    @Override
    public void onUserTurn() {
        Actor[] potentialTargets;
        Action action = getActions()[random.nextInt(actions.length)];
        Actor target;

        if(action.isSupportive()) potentialTargets = getBattle().getFriendlies(this);
        else potentialTargets = getBattle().getOpposition(this);

        if(action == a2) target = this;
        else target = potentialTargets[random.nextInt(potentialTargets.length)];

        doAction(action, target);

    }
}
