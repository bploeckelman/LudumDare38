package lando.systems.ld38.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.turns.Actions;
import lando.systems.ld38.turns.PendingAction;
import lando.systems.ld38.ui.ActionMenu;
import lando.systems.ld38.ui.OptionButton;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.world.Player;
import lando.systems.ld38.world.Tile;
import lando.systems.ld38.world.UserResources;

/**
 * Created by Brian on 4/23/2017.
 */

public class ActionManager {

    private Array<ActionMenu> playerOptions = new Array<ActionMenu>();
    private OrthographicCamera camera;

    public ActionManager(OrthographicCamera camera) {
        this.camera = camera;
    }

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
            if (menu.player == player) return;
        }

        for (ActionMenu menu : playerOptions) {
            menu.hide();
        }

        if (player == null) return;

        // determine available options from tile - for now, use all three
        Array<OptionButton> optionButtons = new Array<OptionButton>(3);

        Tile tile = player.world.getTile(player.row, player.col);
        Vector3 position = player.position;

        float x = position.x + (player.tileWidth / 2);
        float y = position.y + position.z + player.tileHeight/2;

        Rectangle buttonBounds = new Rectangle(x, y, 30, 30);
        optionButtons.add(new OptionButton(Assets.arrow, buttonBounds, Actions.displayMoves, false, camera, "Move"));
        optionButtons.add(new OptionButton(Assets.hammer, buttonBounds, Actions.displayBuild, false, camera, "Build"));
        optionButtons.add(new OptionButton(Assets.wait, buttonBounds, Actions.harvest, false, camera, "Harvest"));

        playerOptions.add(new ActionMenu(player, optionButtons));
    }

    public PendingAction handleTouch(int screenX, int screenY, int button) {

        for (ActionMenu menu : playerOptions) {
            if (Input.Buttons.RIGHT == button) {
                menu.hide();
                continue;
            }

            if (menu.handleTouch(screenX, screenY)) {
                menu.hide();
                if (menu.pendingAction.action == Actions.displayBuild) {
                    showBuildOptions(menu.pendingAction.player);
                }
                return menu.pendingAction;
            }
        }

        return null;
    }

    private void showBuildOptions(Player player) {
        Array<OptionButton> optionButtons = new Array<OptionButton>(3);

        Vector3 position = player.position;

        float x = position.x + (player.tileWidth / 2);
        float y = position.y + position.z + player.tileHeight/2;

        Rectangle buttonBounds = new Rectangle(x, y, 30, 30);

        UserResources resources = player.getResources();

        optionButtons.add(new OptionButton(Assets.ladder, buttonBounds, Actions.buildLadder, resources.canBuildLadder(), camera, "Build Ladder"));
        optionButtons.add(new OptionButton(Assets.raft, buttonBounds, Actions.buildRaft, resources.canBuildRaft(), camera, "Build Raft"));
        //optionButtons.add(new OptionButton(Assets.sandbag, buttonBounds, Actions.buildSandbag, camera));
        //optionButtons.add(new OptionButton(Assets.people, buttonBounds, Actions.buildPeople, camera));
        playerOptions.add(new ActionMenu(player, optionButtons));
    }
}
