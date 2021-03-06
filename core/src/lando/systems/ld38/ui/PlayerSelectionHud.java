package lando.systems.ld38.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.screens.GameScreen;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.world.Player;

/**
 * Created by dsgraham on 4/23/17.
 */
public class PlayerSelectionHud {
    public static final float BOUNDS_WIDTH = 75;
    public GameScreen gameScreen;
    public Rectangle bounds;
    Vector3 screenPos;
    float segmentHeight = 30;
    Array<PlayerHud> playerHuds;

    private int selectedIndex = -1;

    public PlayerSelectionHud (GameScreen screen){
        this.gameScreen = screen;
        bounds = new Rectangle();
        screenPos = new Vector3();
        playerHuds = new Array<PlayerHud>();
        buildPlayerHuds();
    }

    public void buildPlayerHuds(){
        float offset = 40;
        float height = segmentHeight * gameScreen.world.players.size + 20;
        bounds.set(10, gameScreen.camera.viewportHeight - height - 10, BOUNDS_WIDTH, height);
        playerHuds.clear();
        for (int i = 0; i < gameScreen.world.players.size; i++){
            Player p = gameScreen.world.players.get(i);
            p.playerHud = new PlayerHud(gameScreen, p, gameScreen.camera.viewportHeight - (5 + offset));
            playerHuds.add(p.playerHud);
            offset += segmentHeight;
        }
    }

    public void selectNext() {
        int index = ++selectedIndex;
        if (index >= playerHuds.size) {
            index = 0;
        }

        if (playerHuds.size > index) {
            selectPlayer(playerHuds.get(index).player, index);
        }
    }

    public boolean handleTouch(int screenX, int screenY) {
        screenPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        screenPos = gameScreen.hudCamera.unproject(screenPos);
        if (bounds.contains(screenPos.x, screenPos.y)){
            int index = 0;
            for(PlayerHud hud : playerHuds){
                if (hud.bounds.contains(screenPos.x, screenPos.y)){
                    selectPlayer(hud.player, index);
                }
                index++;
            }
            return true;
        }
        return false;
    }

    private void selectPlayer(Player player, int index) {
        gameScreen.zoomToPlayer(player);
        selectedIndex = index;
    }

    public void render(SpriteBatch batch){
        Assets.woodPanel.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height );
        for(PlayerHud p : playerHuds){
            p.render(batch);
        }
    }

    public PlayerHud getPlayerHudByPlayer(Player player) {
        for (PlayerHud playerHud : playerHuds) {
            if (playerHud.player == player) {
                return playerHud;
            }
        }
        return null;
    }
}
