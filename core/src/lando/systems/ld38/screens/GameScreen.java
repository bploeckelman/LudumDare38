package lando.systems.ld38.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld38.turns.ActionTypeMove;
import lando.systems.ld38.turns.TurnAction;
import lando.systems.ld38.utils.Assets;
import lando.systems.ld38.utils.Config;
import lando.systems.ld38.world.Player;
import lando.systems.ld38.world.UserResources;
import lando.systems.ld38.world.World;

/**
 * Created by Brian on 4/16/2017
 */
public class GameScreen extends BaseScreen{

    public Texture debugTex;
    public World world;
    public UserResources resources;

    public boolean alternate = true;
    public int turn;
    public Array<TurnAction> turnActions;

    public GameScreen(){
        super();
        world = new World();
        resources = new UserResources();
        debugTex = Assets.whitePixel;
        turn = 0;
        turnActions = new Array<TurnAction>();
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        world.update(dt);

        if (Gdx.input.justTouched()) {
            Player character = world.players.first();
            int toCol = character.col + (alternate ? 1 : 0);
            int toRow = character.row + (alternate ? 0 : 1);
            TurnAction turnAction = new TurnAction();
            turnAction.character = character;
            turnAction.action = new ActionTypeMove(turnAction, toCol, toRow);
            turnActions.add(turnAction);
            alternate = !alternate;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            endTurn();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(Config.bgColor.r, Config.bgColor.g, Config.bgColor.b, Config.bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        world.render(batch);

        batch.end();

        resources.render();
    }

    private void endTurn() {
        for (TurnAction turnAction : turnActions) {
            turnAction.doAction();
        }
        turnActions.clear();
        ++turn;
    }

}
