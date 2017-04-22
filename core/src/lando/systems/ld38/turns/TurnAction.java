package lando.systems.ld38.turns;

import lando.systems.ld38.world.Player;

/**
 * Created by Brian on 4/22/2017
 */
public class TurnAction {

    public Player character;
    public ActionType action;

    public void doAction() {
        action.doAction();
    }

}
