package lando.systems.ld38.turns;

import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.world.*;

/**
 * Created by Brian on 4/22/2017
 */
public class ActionTypeWait extends ActionType {

    private UserResources userResources;
    private Player player;

    public ActionTypeWait(TurnAction turnAction, UserResources resources, Player player) {
        super(turnAction);
        userResources = resources;
        this.player = player;
    }

    @Override
    public void doAction() {
        Tile tile = player.getTile();

        int offset = 0;

        boolean addRandomFood = false;
        switch (tile.decoration) {
            case Tree:
                userResources.add(new ResourceCount(0, 0, 0, 0, 3));
                player.displayResourceGather(Assets.wood, 3, offset++);
                addRandomFood = true;
                break;
            case Cow:
                userResources.add(new ResourceCount(10, 0, 0, 0, 0));
                player.displayResourceGather(Assets.food, 10, offset++);
                break;
            case IronMine:
                userResources.add(new ResourceCount(0, 0, 3, 0, 0));
                player.displayResourceGather(Assets.iron, 3, offset++);
                break;
            case GoldMine:
                userResources.add(new ResourceCount(0, 0, 0, 3, 0));
                player.displayResourceGather(Assets.gold, 3, offset++);
                break;
            case Hut:
                player.world.addPlayer(player.row, player.col);
                break;
            case None:
                if (tile.type == TileType.Sand) {
                    userResources.add(new ResourceCount(0, 3, 0, 0, 0));
                    player.displayResourceGather(Assets.sand, 3, offset++);
                    addRandomFood = true;
                }
                break;
        }
        tile.decoration = Decoration.None;
        if (addRandomFood) {
            userResources.add(new ResourceCount(1, 0, 0, 0, 0));
            player.displayResourceGather(Assets.food, 1, offset++);
        }
    }

    @Override
    public Tile getTargetTile(World world) {
        return player.getTile();
    }

}
