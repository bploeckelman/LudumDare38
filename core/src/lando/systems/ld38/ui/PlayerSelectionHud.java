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
    public GameScreen gameScreen;
    public Rectangle bounds;
    Vector3 screenPos;
    float segmentHeight = 30;
    Array<PlayerHud> playerHuds;

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
        bounds.set(10, gameScreen.camera.viewportHeight - height - 10, 75, height);
        playerHuds.clear();
        for (int i = 0; i < gameScreen.world.players.size; i++){
            Player p = gameScreen.world.players.get(i);
            p.playerHud = new PlayerHud(gameScreen, p, gameScreen.camera.viewportHeight - (5 + offset));
            playerHuds.add(p.playerHud);
            offset += segmentHeight;
        }
    }

    public boolean handleTouch(int screenX, int screenY) {
        screenPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        screenPos = gameScreen.hudCamera.unproject(screenPos);
        if (bounds.contains(screenPos.x, screenPos.y)){
            for(PlayerHud hud : playerHuds){
                if (hud.bounds.contains(screenPos.x, screenPos.y)){
                    Player p = hud.player;
                    gameScreen.zoomToPlayer(p);                }
            }
            return true;
        }
        return false;
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
