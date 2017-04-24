package lando.systems.ld38.turns;

import lando.systems.ld38.world.Tile;
import lando.systems.ld38.world.World;

/**
 * Created by Brian on 4/22/2017
 */
public abstract class ActionType {

    TurnAction turnAction;

    public ActionType(TurnAction turnAction) {
        this.turnAction = turnAction;
    }

    public abstract void doAction();
    public abstract Tile getTargetTile(World world);

}
