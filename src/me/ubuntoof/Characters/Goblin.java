package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Handlers.Battle;
import me.ubuntoof.Listeners.TurnListener;
import me.ubuntoof.Modifiers.StatModifier;
import me.ubuntoof.Stats;

import java.util.Random;

public class Goblin extends Actor implements TurnListener {

    private static Action a1, a2;
    private static Action[] actions = new Action[]{a1, a2};
    Random random = new Random();

    public Goblin(Battle battle, int level) {

        super(battle,"Goblin", actions, level);

        setBaseMaxHealth(2 + level);
        setBaseHealth(getBaseMaxHealth());
        setBaseStrength(1 + Math.sqrt(2*level));
        setBaseSpeed(2 + Math.sqrt(level));

        a1 = new Action(ActionType.ATTACK, "Pummel", "Deals damage equal to half the user's level.", true) {

            @Override
            public void commit(Actor user, Actor target) {
                // display attack notification
                target.takeDamage(user.getLevel() * 0.5d);
            }
        };

        a2 = new Action(ActionType.ATTACK, "Guard", "Increases defense by 50% until the next turn.", true) {

            @Override public void commit(Actor user, Actor target)
            {
                user.getStatModifiers().add(new StatModifier(Stats.DEFENSE, 1.5d, 0));
            }
        };
    }

    @Override
    public void onUserTurn() {
        Actor[] potentialTargets = getBattle().getOpposition(this);
        doAction(potentialTargets[random.nextInt(potentialTargets.length)], random.nextInt(actions.length));
    }
}
