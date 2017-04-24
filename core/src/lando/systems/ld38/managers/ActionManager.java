package lando.systems.ld38.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.turns.Actions;
import lando.systems.ld38.turns.PendingAction;
import lando.systems.ld38.ui.ActionMenu;
import lando.systems.ld38.ui.OptionButton;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.SoundManager;
import lando.systems.ld38.world.Player;
import lando.systems.ld38.world.ResourceCount;
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

    public void renderTooltops(SpriteBatch batch, OrthographicCamera hudCamera){
        for (ActionMenu menu : playerOptions) {
            menu.renderTooltip(batch, hudCamera);
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

        SoundManager.playSound(SoundManager.SoundOptions.button_select);

        // determine available options from tile - for now, use all three
        Array<OptionButton> optionButtons = new Array<OptionButton>(3);

        Tile tile = player.world.getTile(player.row, player.col);
        Vector3 position = player.position;

        float x = position.x + (player.tileWidth / 2);
        float y = position.y + position.z + player.tileHeight/2;

        Rectangle buttonBounds = new Rectangle(x, y, 30, 30);

        UserResources resources = player.getResources();

        optionButtons.add(new OptionButton(Assets.arrow, buttonBounds, Actions.displayMoves, camera, "Move", resources, new ResourceCount(1, 0, 0, 0, 0), Input.Keys.M));
        optionButtons.add(new OptionButton(Assets.hammer, buttonBounds, Actions.displayBuild, camera, "Build", resources, new ResourceCount(), Input.Keys.B));
        TextureRegion harvestRegion = Assets.wait;
        String tooltip = "Forage";
        switch(tile.decoration){
            case Cow:
                harvestRegion = Assets.shotgun;
                tooltip = "Harvest Food";
                break;
            case Tree:
                harvestRegion = Assets.axe;
                tooltip = "Harvest Wood";
                break;
            case IronMine:
                harvestRegion = Assets.pickaxe;
                tooltip = "Mine Iron";
                break;
            case GoldMine:
                harvestRegion = Assets.pickaxe;
                tooltip = "Mine Gold";
                break;
            case Sand:
                harvestRegion = Assets.shovel;
                tooltip = "Dig up Sand";
                break;
            case Hut:
                harvestRegion = Assets.heart;
                tooltip = "Make Sweet Love";
                break;
        }
        optionButtons.add(new OptionButton(harvestRegion, buttonBounds, Actions.harvest, camera, tooltip, resources, new ResourceCount(), Input.Keys.N));

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

        SoundManager.playSound(SoundManager.SoundOptions.button_select);

        Vector3 position = player.position;

        float x = position.x + (player.tileWidth / 2);
        float y = position.y + position.z + player.tileHeight/2;

        Rectangle buttonBounds = new Rectangle(x, y, 30, 30);

        UserResources resources = player.getResources();

        optionButtons.add(new OptionButton(Assets.ladder, buttonBounds, Actions.build, camera, "Build Ladder", resources, new ResourceCount(0, 0, 0, 0, 3), Input.Keys.L));
        optionButtons.add(new OptionButton(Assets.raft, buttonBounds, Actions.build, camera, "Build Raft", resources, new ResourceCount(0, 0, 0, 0, 4), Input.Keys.R));
        optionButtons.add(new OptionButton(Assets.sandbags, buttonBounds, Actions.build, camera, "Build Sandbags", resources, new ResourceCount(0, 6, 0, 0, 0), Input.Keys.S));
        optionButtons.add(new OptionButton(Assets.hut, buttonBounds, Actions.build, camera, "Build Hut", resources, new ResourceCount(0, 0, 0, 2, 0), Input.Keys.H));
        optionButtons.add(new OptionButton(Assets.hammer_upgrade, buttonBounds, Actions.build, camera, "Upgrade Tools", resources, new ResourceCount(0, 0, 3, 0, 0), Input.Keys.U));
        //optionButtons.add(new OptionButton(Assets.sandbag, buttonBounds, Actions.buildSandbag, camera));
        //optionButtons.add(new OptionButton(Assets.people, buttonBounds, Actions.buildPeople, camera));
        playerOptions.add(new ActionMenu(player, optionButtons));
    }
}
