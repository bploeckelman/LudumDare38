package lando.systems.ld38.turns;

import lando.systems.ld38.world.Player;
import lando.systems.ld38.world.ResourceCount;

/**
 * Created by Brian on 4/22/2017
 */
public class TurnAction {

    public Player player;
    public ActionType action;
    public ResourceCount cost;

    public TurnAction(Player player, ResourceCount cost) {
        this.player = player;
        player.getResources().remove(cost);
        this.cost = cost;
    }

    public void doAction() {
        action.doAction();
    }

}
