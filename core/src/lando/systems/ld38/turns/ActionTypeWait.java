package lando.systems.ld38.turns;

import com.badlogic.gdx.math.MathUtils;
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

        int bns = userResources.resourceBonus;
        switch (tile.decoration) {
            case Tree:
                int gain = TREE_WOOD + bns;
                userResources.add(new ResourceCount(0, 0, 0, 0, gain));
                player.displayResourceGather(Assets.wood, gain, offset++);
                player.world.screen.stats.wood += gain;
                break;
            case Cow:
                gain = COW_FOOD + bns;
                userResources.add(new ResourceCount(gain, 0, 0, 0, 0));
                player.displayResourceGather(Assets.food, gain, offset++);
                player.world.screen.stats.food += gain;
                break;
            case IronMine:
                gain = IRON + bns;
                userResources.add(new ResourceCount(0, 0, gain, 0, 0));
                player.displayResourceGather(Assets.iron, gain, offset++);
                player.world.screen.stats.iron += gain;
                break;
            case GoldMine:
                gain = GOLD + bns;
                userResources.add(new ResourceCount(0, 0, 0, gain, 0));
                player.displayResourceGather(Assets.gold, gain, offset++);
                player.world.screen.stats.gold += gain;
                break;
            case Hut:
                player.world.addPlayer(player.row, player.col);
                break;
            case None:
                if (tile.type == TileType.Sand) {
                    gain = SAND + bns;
                    userResources.add(new ResourceCount(0, gain, 0, 0, 0));
                    player.displayResourceGather(Assets.sand, gain, offset++);
                    player.world.screen.stats.sand += gain;
                }
                gain = bns + 1;
                if (MathUtils.randomBoolean()){
                    userResources.add(new ResourceCount(gain, 0, 0, 0, 0));
                    player.displayResourceGather(Assets.food, gain, offset++);
                    player.world.screen.stats.food += gain;
                } else {
                    userResources.add(new ResourceCount(0, 0, 0, 0, gain));
                    player.displayResourceGather(Assets.wood, gain, offset++);
                    player.world.screen.stats.wood += gain;
                }

                break;
        }
        tile.decoration = Decoration.None;

    }

    @Override
    public Tile getTargetTile(World world) {
        return player.getTile();
    }

}
