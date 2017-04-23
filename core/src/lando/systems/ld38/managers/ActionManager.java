package lando.systems.ld38.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.ui.ActionMenu;
import lando.systems.ld38.ui.OptionButton;
import lando.systems.ld38.world.Player;
import lando.systems.ld38.world.Tile;

/**
 * Created by Brian on 4/23/2017.
 */

public class ActionManager {

    private Array<ActionMenu> playerOptions = new Array<ActionMenu>();

    public void update(float dt) {
        for (int i = playerOptions.size - 1; i >= 0; i--) {
            playerOptions.get(i).update(dt);
            if (playerOptions.get(i).isComplete()) {
                playerOptions.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (ActionMenu menu : playerOptions) {
            menu.render(batch);
        }
    }
    
    public void showOptions(Player player) {

        for (ActionMenu menu : playerOptions) {
            menu.hide();
        }

        if (player == null) return;

        // determine available options from tile - for now, use all three
        Array<OptionButton> optionButtons = new Array<OptionButton>(3);

        float x = player.position.x + 10;
        float y = player.position.y + 20;

        Tile tile = player.world.getTile(player.row, player.col);

        Rectangle buttonBounds = new Rectangle(x, y, 100, 20);
        optionButtons.add(new OptionButton("Move", buttonBounds));
        if (tile != null) {
            optionButtons.add(new OptionButton("Build", buttonBounds));
            optionButtons.add(new OptionButton("Harvest", buttonBounds));
        }

        playerOptions.add(new ActionMenu(player, optionButtons));
    }

    public void hideOptions() {
        showOptions(null);
    }
}
