package lando.systems.ld38.turns;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld38.world.Tile;

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
            tile.item = item;
        }
    }
}
