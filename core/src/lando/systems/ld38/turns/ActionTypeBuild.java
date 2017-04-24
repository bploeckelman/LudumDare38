package lando.systems.ld38.turns;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld38.LudumDare38;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.world.Decoration;
import lando.systems.ld38.world.Tile;
import lando.systems.ld38.world.World;

/**
 * Created by Brian on 4/22/2017
 */
public class ActionTypeBuild extends ActionType {

    public int toCol;
    public int toRow;
    public TextureRegion item;

    public ActionTypeBuild(TurnAction turnAction, TextureRegion item, int toCol, int toRow) {
        super(turnAction);
        this.item = item;

        this.toCol = toCol;
        this.toRow = toRow;
    }

    @Override
    public void doAction() {
        Tile tile = turnAction.player.getTile(toRow, toCol);
        if (tile != null) {
            if (item == Assets.hammer_upgrade){
                World.THE_WORLD.getResources().upgradeResourceBonus();
            } else if (item == Assets.hut){
                tile.decoration = Decoration.Hut;
                tile.decoration_tex = Assets.hut;
                World.THE_WORLD.screen.stats.huts++;
            } else {
                tile.item = item;
                if (item == Assets.raft){
                    World.THE_WORLD.screen.stats.rafts++;
                }
                if (item == Assets.ladder){
                    World.THE_WORLD.screen.stats.ladders++;
                }
                if (item == Assets.sandbags){
                    World.THE_WORLD.screen.stats.sandbags++;
                }
            }
        }
    }

    @Override
    public Tile getTargetTile(World world) {
        return world.getTile(toRow, toCol);
    }

}
