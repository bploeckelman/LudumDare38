package lando.systems.ld38.turns;

import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.world.*;

/**
 * Created by Brian on 4/22/2017
 */
public class ActionTypeWait extends ActionType {

    private UserResources userResources;
    private Player player;

    private static int TREE_WOOD = 3;
    private static int COW_FOOD = 10;
    private static int IRON = 3;
    private static int GOLD = 1;
    private static int SAND = 2;


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
                userResources.add(new ResourceCount(0, 0, 0, 0, TREE_WOOD));
                player.displayResourceGather(Assets.wood, TREE_WOOD, offset++);
                addRandomFood = true;
                player.world.screen.stats.wood += TREE_WOOD;
                break;
            case Cow:
                userResources.add(new ResourceCount(COW_FOOD, 0, 0, 0, 0));
                player.displayResourceGather(Assets.food, COW_FOOD, offset++);
                player.world.screen.stats.food += COW_FOOD;
                break;
            case IronMine:
                userResources.add(new ResourceCount(0, 0, IRON, 0, 0));
                player.displayResourceGather(Assets.iron, IRON, offset++);
                player.world.screen.stats.iron += IRON;
                break;
            case GoldMine:
                userResources.add(new ResourceCount(0, 0, 0, GOLD, 0));
                player.displayResourceGather(Assets.gold, GOLD, offset++);
                player.world.screen.stats.gold += GOLD;
                break;
            case Hut:
                player.world.addPlayer(player.row, player.col);
                break;
            case None:
                if (tile.type == TileType.Sand) {
                    userResources.add(new ResourceCount(0, SAND, 0, 0, 0));
                    player.displayResourceGather(Assets.sand, SAND, offset++);
                    addRandomFood = true;
                    player.world.screen.stats.sand += SAND;
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
