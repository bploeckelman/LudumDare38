package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld38.screens.GameScreen;
import lando.systems.ld38.turns.TurnAction;
import lando.systems.ld38.world.Player;

/**
 * Created by dsgraham on 4/23/17.
 */
public class PlayerHud {

    public Rectangle bounds;
    public GameScreen screen;
    public Player player;
//    private TurnAction actionRef = null;

    public PlayerHud(GameScreen screen, Player p, float y){
        this.bounds = new Rectangle(20, y, 65, 30);
        this.player = p;
        this.screen = screen;

    }

    public void update(float dt){

    }

    public void setTurnAction(TurnAction actionRef, Vector2 triggeringIconScreenPos) {
        this.player.setTurnAction(actionRef, triggeringIconScreenPos);
    }

    public void render(SpriteBatch batch){
        player.renderHud(batch, bounds.x, bounds.y);
    }

}
