package me.ubuntoof.Characters;

import me.ubuntoof.Actions.Action;
import me.ubuntoof.Actions.Action.ActionType;
import me.ubuntoof.Utils.Colorizer;

public class Druidimite extends Actor {

    private static Action a1 = new Action(ActionType.ATTACK, "Boink", "Deals 3 true damage.", false, true) {

        @Override
        public void commit(Actor user, Actor target) {
            int dmg = target.takeDamage(3, true);
            System.out.println(user + " used " + getName() + " on " + (target == user ? "itself" : target) + ", dealing " + Colorizer.RED + dmg + Colorizer.RESET + " damage.");
        }
    };

    private static Action[] actions = new Action[]{a1};

    public Druidimite(int level) {
        super("Druidimite", actions, level);

        setBaseMaxHealth((int)(1 + Math.sqrt(level)));
        setBaseHealth(getMaxHealth());
        setBaseStrength((int)(1 + Math.sqrt(level)));
        setBaseDefense(level/10);
        setBaseArmor((int)(Math.sqrt(level)));
        setBaseSpeed((int)(2 + Math.sqrt(Math.pow(level, 1.2d))));
    }

    @Override
    public void onUserTurn() {
        Actor[] potentialTargets;
        Action action = getActions()[random.nextInt(actions.length)];

        if(action.isSupportive()) potentialTargets = getBattle().getFriendlies(this);
        else potentialTargets = getBattle().getOpposition(this);

        Actor target;
        do {target = potentialTargets[random.nextInt(potentialTargets.length)]; } while(!target.isAlive());

        doAction(action, target);
    }
}
