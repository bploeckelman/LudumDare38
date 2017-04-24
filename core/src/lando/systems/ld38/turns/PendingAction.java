package lando.systems.ld38.turns;

import lando.systems.ld38.ui.OptionButton;
import lando.systems.ld38.world.Player;
import lando.systems.ld38.world.ResourceCount;

/**
 * Created by Brian on 4/23/2017.
 */
public class PendingAction {
    public Actions action = Actions.none;
    public Player player;
    public ResourceCount cost = new ResourceCount();
    public OptionButton button;
}
