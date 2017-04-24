package lando.systems.ld38.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
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

    public void setTurnAction(TurnAction actionRef) {
        System.out.println("setTurnAction!");
//        this.actionRef = actionRef;
        this.player.setTurnAction(actionRef);
    }

    public void render(SpriteBatch batch){
        player.renderHud(batch, bounds.x, bounds.y);
    }

}
